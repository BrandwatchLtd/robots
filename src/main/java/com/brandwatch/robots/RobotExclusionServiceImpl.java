package com.brandwatch.robots;

import com.brandwatch.robots.domain.Group;
import com.brandwatch.robots.domain.PathDirective;
import com.brandwatch.robots.domain.Robots;
import com.brandwatch.robots.net.RobotsURIBuilder;
import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.AbstractIdleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

class RobotExclusionServiceImpl extends AbstractIdleService implements RobotExclusionService {

    private static final Logger log = LoggerFactory.getLogger(RobotExclusionServiceImpl.class);

    private final RobotExclusionConfig config;
    private final RobotsDownloader downloader;
    private final RobotsUtilities utilities;
    private LoadingCache<URI, Robots> robotsCache;

    public RobotExclusionServiceImpl(@Nonnull RobotExclusionConfig robotExclusionConfig) {
        this.config = checkNotNull(robotExclusionConfig, "robotExclusionConfig");
        this.downloader = checkNotNull(robotExclusionConfig.getRobotsDownloader(), "downloader");
        this.utilities = checkNotNull(robotExclusionConfig.getRobotsUtilities(), "utilities");
    }

    @Override
    protected void startUp() throws Exception {
        log.info("Starting up");

        log.debug("Initializing cache (maxSize: {}, expires after: {} hours)",
                config.getCacheMaxSizeRecords(), config.getCachedExpiresHours());

        robotsCache = CacheBuilder.newBuilder()
                .maximumSize(config.getCacheMaxSizeRecords())
                .expireAfterWrite(config.getCachedExpiresHours(), TimeUnit.HOURS)
                .recordStats()
                .build(new CacheLoader<URI, Robots>() {
                    @Nonnull
                    @Override
                    public Robots load(@Nonnull URI key) throws Exception {
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
    public boolean isAllowed(@Nonnull String crawlerAgentString, @Nonnull URI resourceUri) {
        checkNotNull(crawlerAgentString, "crawlerAgentString is null");
        checkNotNull(resourceUri, "resourceUri is null");

        log.debug("evaluating: {}", resourceUri);

        final URI robotsUri;
        log.debug("Resolving robot.txt URL for resource: {}", resourceUri);
        try {
            robotsUri = utilities.getRobotsURIForResource(resourceUri);
            log.debug("Resolved robot URI for resource {} to: {}", resourceUri, robotsUri);
        } catch (RuntimeException ex) {
            log.error("Failed to resolve robots.txt URL for resource {}, due to exception: {}", resourceUri, ex);
            throw ex;
        }

        final Robots robots;
        try {
            robots = robotsCache.getUnchecked(robotsUri);
        } catch (RuntimeException e) {
            log.debug("robots.txt download failure; allowing: {}", resourceUri);
            return true;
        }

        if (robots.getGroups().isEmpty()) {
            log.debug("robots.txt contains no agent groups; allowing: {}", resourceUri);
            return true;
        }

        Optional<Group> group = utilities.getBestMatchingGroup(robots.getGroups(), crawlerAgentString);

        if (!group.isPresent()) {
            log.debug("No matching groups; allowing: {}", resourceUri);
            return true;
        }

        for (PathDirective pathDirective : group.get().getDirectives(PathDirective.class)) {
            if (pathDirective.matches(resourceUri)) {
                log.debug("Path directive {} matches; {}: {}", pathDirective.getValue(),
                        pathDirective.isAllowed() ? "allowing" : "disallowing", resourceUri);
                return pathDirective.isAllowed();
            }
        }

        log.debug("No matching path directive; allowing: {}", resourceUri);
        return true;
    }


}
