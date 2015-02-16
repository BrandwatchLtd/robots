package com.brandwatch.robots;

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.charset.Charset;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class RobotsConfig {

    @Nonnegative
    private long cacheExpiresHours = 24;

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

    @Nonnegative
    private long requestTimeoutMillis = 10000;

    @Nonnegative
    private int readTimeoutMillis = 30000;

    public long getRequestTimeoutMillis() {
        return requestTimeoutMillis;
    }

    public void setRequestTimeoutMillis(long requestTimeoutMillis) {
        this.requestTimeoutMillis = requestTimeoutMillis;
    }

    public int getReadTimeoutMillis() {
        return readTimeoutMillis;
    }

    public void setReadTimeoutMillis(int readTimeoutMillis) {
        this.readTimeoutMillis = readTimeoutMillis;
    }

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


    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("cacheExpiresHours", cacheExpiresHours)
                .add("cacheMaxSizeRecords", cacheMaxSizeRecords)
                .add("maxFileSizeBytes", maxFileSizeBytes)
                .add("maxRedirectHops", maxRedirectHops)
                .add("defaultCharset", defaultCharset)
                .add("userAgent", userAgent)
                .add("requestTimeoutMillis", requestTimeoutMillis)
                .toString();
    }
}
