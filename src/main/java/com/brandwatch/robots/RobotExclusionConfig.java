package com.brandwatch.robots;

import com.brandwatch.robots.domain.Robots;
import com.brandwatch.robots.util.ExpressionCompiler;
import com.brandwatch.robots.util.ExpressionCompilerBuilder;
import com.google.common.base.Function;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkArgument;

public class RobotExclusionConfig {

    private static final Logger log = LoggerFactory.getLogger(RobotExclusionConfig.class);

    private static final Function<String, String> PATH_EXPRESSION_PREPROCESSOR = new Function<String, String>() {
        @Nullable
        @Override
        public String apply(String input) {
            if (!input.isEmpty()) {
                switch (input.charAt(0)) {
                    default:
                        return '/' + input;
                    case '/':
                    case '*':
                    case '^':
                }
            }
            return input;
        }
    };

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
    RobotsUtilities getUtilities() {
        return robotsUtilities;
    }

    @Nonnull
    RobotsLoader getLoader() {
        return new RobotsLoaderCachedImpl(
                new RobotsLoaderDefaultImpl(this),
                getCache());
    }

    ExpressionCompiler getPathExpressionCompiler() {
        return new ExpressionCompilerBuilder()
                .withLeftBoundaryMatching(true)
                .withExpressionPreprocessor(PATH_EXPRESSION_PREPROCESSOR)
                .build();
    }

    ExpressionCompiler getAgentExpressionCompiler() {
        return new ExpressionCompilerBuilder()
                .withCaseSensitivity(false)
                .build();
    }

    private Cache<URI, Robots> getCache() {

        log.debug("Initializing cache (maxSize: {}, expires after: {} hours)",
                getCacheMaxSizeRecords(), this.getCachedExpiresHours());

        return CacheBuilder.newBuilder()
                .maximumSize(getCacheMaxSizeRecords())
                .expireAfterWrite(getCachedExpiresHours(), TimeUnit.HOURS)
                .recordStats()
                .build();
    }

    @Nonnull
    public RobotExclusionService getService() {
        RobotExclusionServiceImpl service = new RobotExclusionServiceImpl(this);
        service.startAsync();
        service.awaitRunning();
        return service;
    }

    public RobotsBuildingHandler getRobotsBuildingHandler() {
        return new RobotsBuildingHandler(
                getPathExpressionCompiler(),
                getAgentExpressionCompiler());
    }
}
