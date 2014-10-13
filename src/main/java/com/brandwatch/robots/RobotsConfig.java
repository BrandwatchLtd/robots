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

public class RobotsConfig {

    private static final Logger log = LoggerFactory.getLogger(RobotsConfig.class);

    private static final Function<String, String> PATH_EXPRESSION_PREPROCESSOR = new Function<String, String>() {
        @Nullable
        @Override
        public String apply(String input) {
            if (!input.isEmpty()) {
                final char c = input.charAt(0);
                if (c != '/' && c != '*' && c != '^') {
                    return '/' + input;
                }
            }
            return input;
        }
    };

    private final RobotsUtilities robotsUtilities = new RobotsUtilities();
    @Nonnegative
    private long cacheExpiresHours = 48;
    @Nonnegative
    private long cacheMaxSizeRecords = 10000;
    @Nonnegative
    private int maxFileSizeBytes = 192 * 1024;

    @Nonnegative
    public long getCacheExpiresHours() {
        return cacheExpiresHours;
    }

    public void setCacheExpiresHours(@Nonnegative long cacheExpiresHours) {
        checkArgument(cacheExpiresHours >= 0, "cacheExpiresHours is negative");
        this.cacheExpiresHours = cacheExpiresHours;
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
                getCacheMaxSizeRecords(), this.getCacheExpiresHours());

        return CacheBuilder.newBuilder()
                .maximumSize(getCacheMaxSizeRecords())
                .expireAfterWrite(getCacheExpiresHours(), TimeUnit.HOURS)
                .recordStats()
                .build();
    }

    @Nonnull
    public RobotsService getService() {
        RobotsServiceImpl service = new RobotsServiceImpl(this);
        service.startAsync();
        service.awaitRunning();
        return service;
    }

    public RobotsBuildingParseHandler getRobotsBuildingHandler() {
        return new RobotsBuildingParseHandler(
                getPathExpressionCompiler(),
                getAgentExpressionCompiler());
    }
}
