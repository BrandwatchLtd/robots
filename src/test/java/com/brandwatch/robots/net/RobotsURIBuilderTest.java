package com.brandwatch.robots.net;

import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.net.URL;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class RobotsURIBuilderTest {

    private static final String VALID_HOST = "www.example.com";
    private static final String VALID_SCHEME = "http";
    private RobotsURIBuilder builder;

    @Before
    public void setup() {
        builder = new RobotsURIBuilder();
    }

    @Test(expected = NullPointerException.class)
    public void givenNullHost_whenWithHost_thenThrowsNPE() {
        builder.withHost(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenEmptyHost_whenWithHost_thenThrowsIAE() {
        builder.withHost("");
    }

    @Test(expected = NullPointerException.class)
    public void givenNullUserInfo_whenWithUserInfo_thenThrowsNPE() {
        builder.withUserInfo(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenEmptyUserInfo_whenWithUserInfo_thenThrowsIAE() {
        builder.withUserInfo("");
    }


    @Test(expected = NullPointerException.class)
    public void givenNullSchema_whenWithSchema_thenThrowsNPE() {
        builder.withScheme(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenEmptyScheme_whenWithScheme_thenThrowsIAE() {
        builder.withScheme("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenNegativePort_whenWithPort_thenThrowsIAE() {
        builder.withPort(-1);
    }

    @Test
    public void givenMinPort_whenWithPort_thenNoExceptionThrown() {
        builder.withPort(0);
    }

    @Test
    public void givenMaxPort_whenWithPort_thenNoExceptionThrown() {
        builder.withPort(65535);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenOverMaxPort_whenWithPort_thenThrowsIAE() {
        builder.withPort(65536);
    }

    @Test(expected = NullPointerException.class)
    public void givenNullURL_whenCopyFrom_thenThrowsNPE() {
        builder.fromURL((URL) null);
    }

    @Test(expected = NullPointerException.class)
    public void givenNullURI_whenCopyFrom_thenThrowsNPE() {
        builder.fromUri((URI) null);
    }

    @Test(expected = IllegalStateException.class)
    public void givenHostUnset_whenBuild_thenThrowsISE() {
        builder.build();
    }

    @Test
    public void givenHostAndSchemeSet_whenBuild_thenReturnsNonNull() {
        builder.withHost(VALID_HOST).withScheme(VALID_SCHEME);
        URI result = builder.build();
        assertThat(result, notNullValue());
    }

    @Test
    public void givenValidHostAndScheme_whenBuild_thenResultHostEqualsExpected() {
        builder.withHost(VALID_HOST).withScheme(VALID_SCHEME);
        URI result = builder.build();
        assertThat(result.getHost(), equalTo(VALID_HOST));
    }

    @Test(expected = IllegalStateException.class)
    public void givenSchemaAndPortUnset_whenBuild_thenThrowsISA() {
        builder.withHost(VALID_HOST);
        URI result = builder.build();
        assertThat(result.getPort(), equalTo(80));
    }


    @Test
    public void givenPortUnset_andSchemaIsHTTPS_whenBuild_thenPortEquals443() {
        builder.withHost(VALID_HOST).withScheme("https");
        URI result = builder.build();
        assertThat(result.getPort(), equalTo(443));
    }

    @Test
    public void givenPortUnset_andSchemaIsFtp_whenBuild_thenPortEquals21() {
        builder.withHost(VALID_HOST).withScheme("ftp");
        URI result = builder.build();
        assertThat(result.getPort(), equalTo(21));
    }

    @Test(expected = IllegalStateException.class)
    public void givenSchemeUnset_andPortIs20_whenBuild_thenThrowsIllegalStateException() {
        builder.withHost(VALID_HOST).withPort(21);
        URI result = builder.build();
        assertThat(result.getScheme(), equalTo("ftp"));
    }

    @Test(expected = IllegalStateException.class)
    public void givenSchemeUnset_andPortIs9999_whenBuild_thenThrowsIllegalStateException() {
        builder.withHost(VALID_HOST).withPort(9999);
        URI result = builder.build();
        assertThat(result.getScheme(), equalTo("http"));
    }

    @Test(expected = IllegalStateException.class)
    public void givenSchemeUnset_andPortIs1234_whenBuild_thenThrowsIllegalStateException() {
        builder.withHost(VALID_HOST).withPort(1234);
        URI result = builder.build();
        assertThat(result.getScheme(), equalTo("http"));
    }

    @Test
    public void givenPortUnset_andSchemeUnknown_whenBuild_thenPortEquals80() {
        builder.withHost(VALID_HOST).withScheme("example-scheme");
        URI result = builder.build();
        assertThat(result.getPort(), equalTo(80));
    }

    @Test
    public void givenValidHostAndScheme_whenBuild_thenPathEqualsExpected() {
        builder.withHost(VALID_HOST).withScheme(VALID_SCHEME);
        URI result = builder.build();
        assertThat(result.getPath(), equalTo("/robots.txt"));
    }

}
