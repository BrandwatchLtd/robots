package com.brandwatch.robots;

import com.brandwatch.robots.net.RobotsCharSourceFactory;
import com.brandwatch.robots.net.RobotsCharSourceFactoryImpl;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkArgument;

public class RobotExclusionConfig {

    private final RobotsUtilities robotsUtilities = new RobotsUtilities();

    @Nonnegative
    private long cachedExpiresHours = 48;

    @Nonnegative
    private long cacheMaxSizeRecords = 10000;

    @Nonnegative
    private int maxFileSizeBytes = 192 * 1024;

    @Nonnegative
    public long getCachedExpiresHours() {
        return cachedExpiresHours;
    }

    public void setCachedExpiresHours(@Nonnegative long cachedExpiresHours) {
        checkArgument(cachedExpiresHours >= 0, "cachedExpiresHours is negative");
        this.cachedExpiresHours = cachedExpiresHours;
    }

    @Nonnegative
    public long getCacheMaxSizeRecords() {
        return cacheMaxSizeRecords;
    }

    public void setCacheMaxSizeRecords(@Nonnegative long cacheMaxSizeRecords) {
        checkArgument(cacheMaxSizeRecords >= 0, "cacheMaxSizeRecords is negative");
        this.cacheMaxSizeRecords = cacheMaxSizeRecords;
    }

    @Nonnegative
    public int getMaxFileSizeBytes() {
        return maxFileSizeBytes;
    }

    public void setMaxFileSizeBytes(@Nonnegative int maxFileSizeBytes) {
        checkArgument(maxFileSizeBytes >= 0, "maxFileSizeBytes is negative");
        this.maxFileSizeBytes = maxFileSizeBytes;
    }

    @Nonnull
    RobotsUtilities getRobotsUtilities() {
        return robotsUtilities;
    }

    @Nonnull
    RobotsCharSourceFactory getRobotsCharSourceFactory() {
        return new RobotsCharSourceFactoryImpl();
    }

    @Nonnull
    RobotsDownloader getRobotsDownloader() {
        return new RobotsDownloaderImpl(robotsUtilities);
    }

    @Nonnull
    public RobotExclusionService getRobotExclusionService() {
        RobotExclusionServiceImpl service = new RobotExclusionServiceImpl(this);
        service.startAsync();
        service.awaitRunning();
        return service;
    }

}
