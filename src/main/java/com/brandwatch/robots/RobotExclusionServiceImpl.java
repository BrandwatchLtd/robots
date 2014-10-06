package com.brandwatch.robots;

import com.brandwatch.robots.domain.Robots;
import com.brandwatch.robots.net.RobotsDownloader;
import com.brandwatch.robots.net.RobotsSource;
import com.brandwatch.robots.net.RobotsSourceFactory;
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

    private final RobotsSourceFactory sourceFactory;
    private final RobotsDownloader downloader;

    private LoadingCache<RobotsSource, Robots> robotsCache;


    public RobotExclusionServiceImpl(RobotsSourceFactory sourceFactory, RobotsDownloader downloader) {
        this.sourceFactory = checkNotNull(sourceFactory, "sourceFactory");
        this.downloader = checkNotNull(downloader, "downloader");
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

        // TODO:
        // get group that best applies to our user agent
        // match resourceUri path against path in the group
        // return whether paths match resourceUri path

        return false;
    }


}
