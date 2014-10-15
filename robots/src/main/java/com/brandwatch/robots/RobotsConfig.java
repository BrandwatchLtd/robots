package com.brandwatch.robots;

import com.brandwatch.robots.domain.Robots;
import com.brandwatch.robots.net.CharSourceSupplier;
import com.brandwatch.robots.net.CharSourceSupplierHttpClientImpl;
import com.brandwatch.robots.net.LoggingClientFilter;
import com.brandwatch.robots.util.ExpressionCompiler;
import com.brandwatch.robots.util.ExpressionCompilerBuilder;
import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class RobotsConfig {

    private static final Logger log = LoggerFactory.getLogger(RobotsConfig.class);

    private static final Function<String, String> PATH_EXPRESSION_PREPROCESSOR = new MissingPrefixFixingFunction();

    private final RobotsUtilities robotsUtilities = new RobotsUtilities();

    @Nonnegative
    private long cacheExpiresHours = 48;
    @Nonnegative
    private long cacheMaxSizeRecords = 10000;
    @Nonnegative
    private int maxFileSizeBytes = 192 * 1024;
    @Nonnegative
    private int maxRedirectHops = 5;
    @Nonnull
    private Charset defaultCharset = Charsets.UTF_8;
    @Nonnull
    private String userAgent = "robots";

    @Nonnull
    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(@Nonnull String userAgent) {
        checkNotNull(userAgent, "userAgent is null");
        this.userAgent = userAgent;
    }

    @Nonnull
    public Charset getDefaultCharset() {
        return defaultCharset;
    }

    public void setDefaultCharset(@Nonnull Charset defaultCharset) {
        checkNotNull(defaultCharset, "defaultCharset is null");
        this.defaultCharset = defaultCharset;
    }

    @Nonnegative
    public int getMaxRedirectHops() {
        return maxRedirectHops;
    }

    public void setMaxRedirectHops(@Nonnegative int maxRedirectHops) {
        checkArgument(maxRedirectHops >= 0, "maxRedirectHops is negative");
        this.maxRedirectHops = maxRedirectHops;
    }

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
    public RobotsFactory getFactory() {
        return new RobotsFactory();
    }

    @Nonnull
    RobotsLoader getLoader() {
        return new RobotsLoaderCachedImpl(
                new RobotsLoaderImpl(this, getFactory(), getCharSourceSupplier()),
                getCache());
    }

    @Nonnull
    CharSourceSupplier getCharSourceSupplier() {
        return new CharSourceSupplierHttpClientImpl(this, createClient());
    }

    @Nonnull
    ExpressionCompiler getPathExpressionCompiler() {
        return new ExpressionCompilerBuilder()
                .withLeftBoundaryMatching(true)
                .withExpressionPreprocessor(PATH_EXPRESSION_PREPROCESSOR)
                .build();
    }

    @Nonnull
    ExpressionCompiler getAgentExpressionCompiler() {
        return new ExpressionCompilerBuilder()
                .withCaseSensitivity(false)
                .build();
    }

    @Nonnull
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

    @Nonnull
    public RobotsBuildingParseHandler getRobotsBuildingHandler() {
        return new RobotsBuildingParseHandler(
                getPathExpressionCompiler(),
                getAgentExpressionCompiler());
    }

    @Nonnull
    public Client createClient() {
        return ClientBuilder.newBuilder()
                .register(new LoggingClientFilter(this.getClass()))
                .build();
    }

}
