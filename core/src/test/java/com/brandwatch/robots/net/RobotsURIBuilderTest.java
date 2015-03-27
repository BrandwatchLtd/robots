package com.brandwatch.robots.net;

/*
 * #%L
 * Robots (core)
 * %%
 * Copyright (C) 2015 Brandwatch
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

import org.junit.Before;
import org.junit.Test;

import javax.annotation.Nonnull;
import java.net.URI;
import java.net.URL;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class RobotsURIBuilderTest {

    @Nonnull
    private static final String VALID_HOST = "www.example.com";
    @Nonnull
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
        builder.fromURL(null);
    }

    @Test(expected = NullPointerException.class)
    public void givenNullURI_whenCopyFrom_thenThrowsNPE() {
        builder.fromUri(null);
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
