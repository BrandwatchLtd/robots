package com.brandwatch.robots;

import com.google.common.base.Charsets;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class RobotsConfigTest {

    private RobotsConfig config;

    @Before
    public void setup() {
        config = new RobotsConfig();
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNegativeValue_whenSetCacheExpiresHours_thenThrowsIAE() {
        final long invalidCacheExpiresHours = -1;
        config.setCacheExpiresHours(invalidCacheExpiresHours);
    }

    @Test
    public void givenZeroValue_whenSetCacheExpiresHours_thenValueIsStored() {
        final long validCacheExpiresHours = 0;
        config.setCacheExpiresHours(validCacheExpiresHours);
        assertThat(config.getCacheExpiresHours(), equalTo(validCacheExpiresHours));
    }

    @Test
    public void givenPositiveValue_whenSetCacheExpiresHours_thenValueIsStored() {
        final long validCacheExpiresHours = 1;
        config.setCacheExpiresHours(validCacheExpiresHours);
        assertThat(config.getCacheExpiresHours(), equalTo(validCacheExpiresHours));
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNegativeValue_whenSetCacheMaxSizeRecords_thenThrowsIAE() {
        final long validCacheMaxSizeRecords = -1;
        config.setCacheMaxSizeRecords(validCacheMaxSizeRecords);
    }

    @Test
    public void givenZeroValue_whenSetCacheMaxSizeRecords_thenValueIsStored() {
        final long validCacheMaxSizeRecords = 0;
        config.setCacheMaxSizeRecords(validCacheMaxSizeRecords);
        assertThat(config.getCacheMaxSizeRecords(), equalTo(validCacheMaxSizeRecords));
    }

    @Test
    public void givenPositiveValue_whenSetCacheMaxSizeRecords_thenValueIsStored() {
        final long validCacheMaxSizeRecords = 1;
        config.setCacheMaxSizeRecords(validCacheMaxSizeRecords);
        assertThat(config.getCacheMaxSizeRecords(), equalTo(validCacheMaxSizeRecords));
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNegativeValue_whenSetMaxFileSizeBytes_thenThrowsIAE() {
        final int maxFileSizeBytes = -1;
        config.setMaxFileSizeBytes(maxFileSizeBytes);
    }

    @Test
    public void givenZeroValue_whenSetMaxFileSizeBytes_thenValueIsStored() {
        final int maxFileSizeBytes = 0;
        config.setMaxFileSizeBytes(maxFileSizeBytes);
        assertThat(config.getMaxFileSizeBytes(), equalTo(maxFileSizeBytes));
    }

    @Test
    public void givenPositiveValue_whenSetMaxFileSizeBytes_thenValueIsStored() {
        final int maxFileSizeBytes = 1;
        config.setMaxFileSizeBytes(maxFileSizeBytes);
        assertThat(config.getMaxFileSizeBytes(), equalTo(maxFileSizeBytes));
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNegativeValue_whenSetMaxRedirectHops_thenThrowsIAE() {
        final int maxRedirectHops = -1;
        config.setMaxRedirectHops(maxRedirectHops);
    }

    @Test
    public void givenZeroValue_whenSetMaxRedirectHops_thenValueIsStored() {
        final int maxRedirectHops = 0;
        config.setMaxRedirectHops(maxRedirectHops);
        assertThat(config.getMaxRedirectHops(), equalTo(maxRedirectHops));
    }

    @Test
    public void givenPositiveValue_whenSetMaxRedirectHops_thenValueIsStored() {
        final int maxRedirectHops = 1;
        config.setMaxRedirectHops(maxRedirectHops);
        assertThat(config.getMaxRedirectHops(), equalTo(maxRedirectHops));
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNegativeValue_whenSetReadTimeoutMillis_thenThrowsIAE() {
        final int readTimeoutMillis = -1;
        config.setReadTimeoutMillis(readTimeoutMillis);
    }

    @Test
    public void givenZeroValue_whenSetReadTimeoutMillis_thenValueIsStored() {
        final int readTimeoutMillis = 0;
        config.setReadTimeoutMillis(readTimeoutMillis);
        assertThat(config.getReadTimeoutMillis(), equalTo(readTimeoutMillis));
    }

    @Test
    public void givenPositiveValue_whenSetReadTimeoutMillis_thenValueIsStored() {
        final int readTimeoutMillis = 1;
        config.setReadTimeoutMillis(readTimeoutMillis);
        assertThat(config.getReadTimeoutMillis(), equalTo(readTimeoutMillis));
    }

    @Test
    public void givenNegativeValue_whenSetRequestTimeoutMillis_thenValueIsStored() {
        final long requestTimeoutMillis = -1;
        config.setRequestTimeoutMillis(requestTimeoutMillis);
        assertThat(config.getRequestTimeoutMillis(), equalTo(requestTimeoutMillis));
    }

    @Test
    public void givenZeroValue_whenSetRequestTimeoutMillis_thenValueIsStored() {
        final long requestTimeoutMillis = 0;
        config.setRequestTimeoutMillis(requestTimeoutMillis);
        assertThat(config.getRequestTimeoutMillis(), equalTo(requestTimeoutMillis));
    }

    @Test
    public void givenPositiveValue_whenSetRequestTimeoutMillis_thenValueIsStored() {
        final long requestTimeoutMillis = 1;
        config.setRequestTimeoutMillis(requestTimeoutMillis);
        assertThat(config.getRequestTimeoutMillis(), equalTo(requestTimeoutMillis));
    }

    @Test(expected = NullPointerException.class)
    public void givenNullValue_whenSetDefaultCharset_thenThrowsNPE() {
        config.setDefaultCharset(null);
    }

    @Test
    public void givenValidCharset_whenSetDefaultCharset_thenValueIsStored() {
        final Charset charset = Charsets.UTF_16BE;
        config.setDefaultCharset(charset);
        assertThat(config.getDefaultCharset(), equalTo(charset));
    }

    @Test(expected = NullPointerException.class)
    public void givenNullValue_whenSetUserAgent_thenThrowsNPE() {
        config.setUserAgent(null);
    }

    @Test
    public void givenValidCharset_whenSetUserAgent_thenValueIsStored() {
        final String userAgent = "example agent string";
        config.setUserAgent(userAgent);
        assertThat(config.getUserAgent(), equalTo(userAgent));
    }

    @Test
    public void givenValidConfig_whenToString_thenReturnsNonNull() {
        String stringRepresentation = config.toString();
        assertThat(stringRepresentation, notNullValue());
    }

}
