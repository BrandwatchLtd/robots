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

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.net.IDN;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class RobotsURIBuilderDataTest {

    private final String resource;
    private final String robots;
    private RobotsURIBuilder builder;

    public RobotsURIBuilderDataTest(String resource, String robots) {
        this.resource = resource;
        this.robots = robots;
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Iterable<Object[]> data() {
        return ImmutableList.<Object[]>builder()
                .add(new Object[]{"http://example.com/", "http://example.com:80/robots.txt"})
                .add(new Object[]{"http://www.example.com/", "http://www.example.com:80/robots.txt"})
                .add(new Object[]{"http://www.EXAMPLE.com/", "http://www.EXAMPLE.com:80/robots.txt"})
                .add(new Object[]{"http://example.com/folder/file", "http://example.com:80/robots.txt"})
                .add(new Object[]{"http://www.müller.eu/", "http://www.müller.eu:80/robots.txt"})
                .add(new Object[]{"http://www.xn--mller-kva.eu/", "http://www.müller.eu:80/robots.txt"})
                .add(new Object[]{"ftp://example.com/", "ftp://example.com:21/robots.txt"})
                .add(new Object[]{"ftp://example.com/folder/file", "ftp://example.com:21/robots.txt"})
                .add(new Object[]{"http://212.96.82.21/", "http://212.96.82.21:80/robots.txt"})
                .add(new Object[]{"http://example.com:80/", "http://example.com:80/robots.txt"})
                .add(new Object[]{"http://example.com/", "http://example.com:80/robots.txt"})
                .add(new Object[]{"https://example.com:443/", "https://example.com:443/robots.txt"})
                .add(new Object[]{"https://example.com/", "https://example.com:443/robots.txt"})
                .add(new Object[]{"ftp://example.com:21/", "ftp://example.com:21/robots.txt"})
                .add(new Object[]{"ftp://example.com/", "ftp://example.com:21/robots.txt"})
                .add(new Object[]{"http://example.com:8181/", "http://example.com:8181/robots.txt"})
                .add(new Object[]{"http://things.example.com:80/", "http://things.example.com:80/robots.txt"})
                .add(new Object[]{"http://things.example.com/", "http://things.example.com:80/robots.txt"})
                .add(new Object[]{"http://example.com/?query", "http://example.com:80/robots.txt"})
                .add(new Object[]{"http://example.com/#fragement", "http://example.com:80/robots.txt"})
                .add(new Object[]{"http://www.example.com", "http://www.example.com:80/robots.txt"})
                .build();
    }

    @Before
    public void setup() {
        builder = new RobotsURIBuilder();
    }

    @Test
    public void givenUriString_whenBuild_thenResultEqualsExpected() {
        builder.fromUriString(resource);
        URI result = builder.build();
        assertThat(IDN.toUnicode(result.toString()), equalTo(robots));
    }

    @Test
    public void givenUri_whenBuild_thenResultEqualsExpected() {
        builder.fromUri(URI.create(IDN.toASCII(resource)));
        URI result = builder.build();
        assertThat(IDN.toUnicode(result.toString()), equalTo(robots));
    }

    @Test
    public void givenUrl_whenBuild_thenResultEqualsExpected() throws MalformedURLException {
        builder.fromURL(new URL(IDN.toASCII(resource)));
        URI result = builder.build();
        assertThat(IDN.toUnicode(result.toString()), equalTo(robots));
    }

    @Test
    public void givenRobotsUriString_whenBuild_thenResultEqualsRobots() {
        builder.fromUriString(robots);
        URI result = builder.build();
        assertThat(IDN.toUnicode(result.toString()), equalTo(robots));
    }
}
