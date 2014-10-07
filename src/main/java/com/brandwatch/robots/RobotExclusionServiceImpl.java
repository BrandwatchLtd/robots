package com.brandwatch.robots;

import com.brandwatch.robots.domain.Group;
import com.brandwatch.robots.domain.PathDirective;
import com.brandwatch.robots.domain.Robots;
import com.brandwatch.robots.net.RobotsCharSourceFactory;
import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.io.CharSource;
import com.google.common.util.concurrent.AbstractIdleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

public class RobotExclusionServiceImpl extends AbstractIdleService implements RobotExclusionService {

    private static final Logger log = LoggerFactory.getLogger(RobotExclusionServiceImpl.class);

    private RobotsCharSourceFactory sourceFactory;
    private RobotsDownloader downloader;
    private RobotsUtilities utilities;
    private LoadingCache<CharSource, Robots> robotsCache;

    public RobotExclusionServiceImpl(RobotsCharSourceFactory sourceFactory, RobotsDownloader downloader, RobotsUtilities utilities) {
        this.sourceFactory = checkNotNull(sourceFactory, "sourceFactory");
        this.downloader = checkNotNull(downloader, "downloader");
        this.utilities = checkNotNull(utilities, "utilities");
    }

    @Override
    protected void startUp() throws Exception {
        log.info("Starting up");


        final long expires = 24;
        final TimeUnit expiresUnit = TimeUnit.HOURS;
        final long maxSize = 10000;

        log.debug("Initializing cache (maxSize: {}, expires after: {} {})", maxSize, expires, expiresUnit);

        robotsCache = CacheBuilder.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expires, expiresUnit)
                .recordStats()
                .build(new CacheLoader<CharSource, Robots>() {
                    @Override
                    public Robots load(CharSource key) throws Exception {
                        return downloader.load(key);
                    }
                });
    }

    @Override
    protected void shutDown() throws Exception {
        log.info("Shutting down");
        robotsCache.invalidateAll();
        robotsCache.cleanUp();
        log.debug("Cache stats: {}", robotsCache.stats().toString());
    }

    @Override
    public boolean isAllowed(@Nonnull URI resourceUri) {
        checkNotNull(resourceUri, "resourceUri is null");

        log.debug("evaluating: {}", resourceUri);
        final Robots robots;
        try {
            CharSource source = sourceFactory.createFor(resourceUri);
            robots = robotsCache.getUnchecked(source);
        } catch (RuntimeException e) {
            log.debug("robots.txt download failure; allowing: {}", resourceUri);
            return true;
        }

        if (robots.getGroups().isEmpty()) {
            log.debug("robots.txt contains no agent groups; allowing: {}", resourceUri);
            return true;
        }

        // FIXME
        String crawlerAgent = "magpie";

        Optional<Group> group = utilities.getBestMatchingGroup(robots.getGroups(), crawlerAgent);

        if (!group.isPresent()) {
            log.debug("No matching groups; allowing: {}", resourceUri);
            return true;
        }

        for (PathDirective pathDirective : group.get().getDirectives(PathDirective.class)) {
            if (pathDirective.matches(resourceUri)) {
                log.debug("Path directive {} matches; {}: {}", pathDirective.getValue(),
                        pathDirective.isAllowed() ? "allowing" : "disallowing" , resourceUri);
                return pathDirective.isAllowed();
            }
        }

        log.debug("No matching path directive; allowing: {}", resourceUri);
        return true;
    }


}
