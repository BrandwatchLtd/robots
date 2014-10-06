package com.brandwatch.robots;

import com.brandwatch.robots.domain.Group;
import com.brandwatch.robots.domain.Robots;
import com.brandwatch.robots.net.RobotsDownloader;
import com.brandwatch.robots.net.RobotsSource;
import com.brandwatch.robots.net.RobotsSourceFactory;
import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.AbstractIdleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

public class RobotExclusionServiceImpl extends AbstractIdleService implements RobotExclusionService {

    private static final Logger log = LoggerFactory.getLogger(RobotExclusionServiceImpl.class);

    private RobotsSourceFactory sourceFactory;
    private RobotsDownloader downloader;
    private RobotsUtilities utilities;

    private LoadingCache<RobotsSource, Robots> robotsCache;


    public RobotExclusionServiceImpl(RobotsSourceFactory sourceFactory, RobotsDownloader downloader, RobotsUtilities utilities) {
        this.sourceFactory = checkNotNull(sourceFactory, "sourceFactory");
        this.downloader = checkNotNull(downloader, "downloader");
        this.utilities = checkNotNull(utilities, "utilities");
    }

    @Override
    protected void startUp() throws Exception {
        robotsCache = CacheBuilder.newBuilder()
                .maximumSize(10000)
                .expireAfterWrite(24, TimeUnit.HOURS)
                .recordStats()
                .build(new CacheLoader<RobotsSource, Robots>() {
                    @Override
                    public Robots load(RobotsSource key) throws Exception {
                        return downloader.load(key);
                    }
                });
    }

    @Override
    protected void shutDown() throws Exception {
        robotsCache.invalidateAll();
        log.debug("Cache stats: {}", robotsCache.stats().toString());
    }

    @Override
    public boolean isAllowed(URI resourceUri) {

        final Robots robots;
        try {
            RobotsSource source = sourceFactory.createFor(resourceUri);
            robots = robotsCache.getUnchecked(source);
        } catch (RuntimeException e) {
            return true;
        }

        if (robots.getGroups().isEmpty()) {
            return true;
        }

        String crawlerAgent = "magpie";

        Optional<Group> group = utilities.getBestMatchingGroup(robots.getGroups(), crawlerAgent);

        if (!group.isPresent()) {
            return true;
        }


        //
        // match resourceUri path against path in the group
        // return whether paths match resourceUri path

        return false;
    }


}
