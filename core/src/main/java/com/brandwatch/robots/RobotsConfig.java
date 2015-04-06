package com.brandwatch.robots;

/*
 * #%L
 * Robots (core)
 * %%
 * Copyright (C) 2014 - 2015 Brandwatch
 * %%
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the Brandwatch nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

import com.google.common.base.Charsets;
import com.google.common.base.MoreObjects;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.nio.charset.Charset;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Hold global configuration information for a {@link RobotsService} instance.
 * <p/>
 * <tt>RobotsConfig</tt> is mutable, so can be altered after the service has been
 * instantiated. Note however that this is a bad idea, and is unlikely to work
 * as intended.
 * <p/>
 * <tt>RobotsConfig</tt> is used to produce a {@link RobotsFactory} instance, which
 * in turn produces the service:
 * <pre>
 *  RobotsConfig config = new RobotsConfig();
 *  config.setCachedExpiresHours(48);
 *  config.setCacheMaxSizeRecords(10000);
 *  config.setMaxFileSizeBytes(192 * 1024);
 *
 *  RobotsFactory factory = new RobotsFactory(config);
 *
 *  RobotsService service = factory.createService();
 * </pre>
 */
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

    /**
     * @return time in milliseconds before requests time out
     * @see #setRequestTimeoutMillis(long)
     */
    public long getRequestTimeoutMillis() {
        return requestTimeoutMillis;
    }

    /**
     * Wait <em>at most</em> the given time in milliseconds for a response, when making a requests.
     * <p/>
     * A value of zero or less will timeout immediately.
     *
     * @param requestTimeoutMillis time in milliseconds before requests time out
     */
    public void setRequestTimeoutMillis(@Nonnegative long requestTimeoutMillis) {
        this.requestTimeoutMillis = requestTimeoutMillis;
    }

    /**
     * @return time in milliseconds before response reading times out
     * @see #setReadTimeoutMillis(int)
     */
    public int getReadTimeoutMillis() {
        return readTimeoutMillis;
    }

    /**
     * Download the payload contents for <em>at most</em> the given length of time,
     * after a response has been received.
     *
     * If the time is exceeded, download will fail.
     *
     * @param readTimeoutMillis time in milliseconds before response reading times out
     * @throws IllegalArgumentException when <tt>readTimeoutMillis</tt> is negative
     */
    public void setReadTimeoutMillis(@Nonnegative int readTimeoutMillis) {
        checkArgument(readTimeoutMillis >= 0, "readTimeoutMillis is negative");
        this.readTimeoutMillis = readTimeoutMillis;
    }

    /**
     * @return user-agent string passed by HTTP client requests
     * @see #setUserAgent(String)
     */
    @Nonnull
    public String getUserAgent() {
        return userAgent;
    }

    /**
     * The user-agent identifier used to make requests.
     *
     * This is distinct from the user-agent used for matching directives.
     *
     * @param userAgent agent string passed by HTTP client requests
     * @throws NullPointerException if <tt>userAgent</tt> is null
     */
    public void setUserAgent(@Nonnull String userAgent) {
        checkNotNull(userAgent, "userAgent is null");
        this.userAgent = userAgent;
    }

    /**
     * @return default character set used to decode received <tt>robots.txt</tt> files
     * @see #setDefaultCharset(Charset)
     */
    @Nonnull
    public Charset getDefaultCharset() {
        return defaultCharset;
    }

    /**
     * Character set used to decode the response, when downloading <tt>robots.txt</tt>
     * files, and no other information is available.
     *
     * @param defaultCharset default character set used to decode received <tt>robots.txt</tt> files
     * @throws NullPointerException when <tt>defaultCharset</tt> is null
     */
    public void setDefaultCharset(@Nonnull Charset defaultCharset) {
        checkNotNull(defaultCharset, "defaultCharset is null");
        this.defaultCharset = defaultCharset;
    }

    /**
     * @return maximum number of redirects to follow before giving up
     * @see #setMaxRedirectHops(int)
     */
    @Nonnegative
    public int getMaxRedirectHops() {
        return maxRedirectHops;
    }

    /**
     * Follow HTTP redirects up to the number of hops specified here,
     * when requesting a <tt>robots.txt</tt> file.
     *
     * A value of zero indicates that
     * no redirects will be followed.
     *
     * @param maxRedirectHops maximum number of redirects to follow before giving up
     * @throws IllegalArgumentException when <tt>maxRedirectHops</tt> is negative
     */
    public void setMaxRedirectHops(@Nonnegative int maxRedirectHops) {
        checkArgument(maxRedirectHops >= 0, "maxRedirectHops is negative");
        this.maxRedirectHops = maxRedirectHops;
    }

    /**
     * @return time in hours before cached <tt>robots.txt</tt> files are expired
     * @see #setCacheExpiresHours(long)
     */
    @Nonnegative
    public long getCacheExpiresHours() {
        return cacheExpiresHours;
    }

    /**
     * Time after which cached responses should be discarded, and new request be made.
     * <p/>
     * After retrieving and parsing a <tt>robots.txt</tt> file, the parsed response is
     * cached. This is improve performance of subsequent requests, and reduce
     * load on the target server.
     *
     * @param cacheExpiresHours time in hours before cached <tt>robots.txt</tt> files are expired
     * @throws IllegalArgumentException when <tt>cacheExpiresHours</tt> is negative
     */
    public void setCacheExpiresHours(@Nonnegative long cacheExpiresHours) {
        checkArgument(cacheExpiresHours >= 0, "cacheExpiresHours is negative");
        this.cacheExpiresHours = cacheExpiresHours;
    }

    /**
     * @return number of <tt>robots.txt</tt> records to store before evicting the oldest
     * @see #setCacheMaxSizeRecords(long)
     */
    @Nonnegative
    public long getCacheMaxSizeRecords() {
        return cacheMaxSizeRecords;
    }

    /**
     * Evict oldest cached records when the cache size exceeds this value.
     *
     * @param cacheMaxSizeRecords number of <tt>robots.txt</tt> records to store before evicting the oldest
     * @throws IllegalArgumentException whe <tt>cacheMaxSizeRecords</tt> is negative
     */
    public void setCacheMaxSizeRecords(@Nonnegative long cacheMaxSizeRecords) {
        checkArgument(cacheMaxSizeRecords >= 0, "cacheMaxSizeRecords is negative");
        this.cacheMaxSizeRecords = cacheMaxSizeRecords;
    }

    /**
     * @return maximum file size, in bytes, for downloaded <tt>robots.txt</tt> files
     * @see #setMaxFileSizeBytes(int)
     */
    @Nonnegative
    public int getMaxFileSizeBytes() {
        return maxFileSizeBytes;
    }

    /**
     * Limit file size to <em>at most</em> the given number of bytes, when
     * downloading a <tt>robots.txt</tt>.
     * <p/>
     * Data before this limit are processed normally, but the remainder are discarded.
     *
     * @param maxFileSizeBytes maximum file size, in bytes, for downloaded <tt>robots.txt</tt> files
     * @throws IllegalArgumentException when <tt>maxFileSizeBytes</tt> is negative
     */
    public void setMaxFileSizeBytes(@Nonnegative int maxFileSizeBytes) {
        checkArgument(maxFileSizeBytes >= 0, "maxFileSizeBytes is negative");
        this.maxFileSizeBytes = maxFileSizeBytes;
    }

    @Override
    @Nonnull
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("cacheExpiresHours", cacheExpiresHours)
                .add("cacheMaxSizeRecords", cacheMaxSizeRecords)
                .add("maxFileSizeBytes", maxFileSizeBytes)
                .add("maxRedirectHops", maxRedirectHops)
                .add("defaultCharset", defaultCharset)
                .add("userAgent", userAgent)
                .add("requestTimeoutMillis", requestTimeoutMillis)
                .add("readTimeoutMillis", readTimeoutMillis)
                .toString();
    }
}
