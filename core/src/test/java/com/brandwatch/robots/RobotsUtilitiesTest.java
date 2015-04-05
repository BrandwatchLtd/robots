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

import org.junit.Before;
import org.junit.Test;

import java.net.URI;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class RobotsUtilitiesTest {

    private RobotsUtilities utilities;

    @Before
    public void setup() {
        utilities = new RobotsUtilities();
    }

    @Test(expected = NullPointerException.class)
    public void givenNullResource_whenGetResourceLocalComponents_thenThrowsNPE() {
        URI resource = null;
        utilities.getResourceLocalComponents(resource);
    }

    @Test
    public void givenPath_whenGetResourceLocalComponents_thenResultEqualsPath() {
        URI resource = URI.create("http://example.com/path");
        String path = utilities.getResourceLocalComponents(resource);
        assertThat(path, equalTo("/path"));
    }

    @Test
    public void givenFragment_whenGetResourceLocalComponents_thenResultEqualsFragment() {
        URI resource = URI.create("http://example.com#fragment");
        String result = utilities.getResourceLocalComponents(resource);
        assertThat(result, equalTo("#fragment"));
    }

    @Test
    public void givenQuery_whenGetResourceLocalComponents_thenResultEqualsQuery() {
        URI resource = URI.create("http://example.com?query");
        String result = utilities.getResourceLocalComponents(resource);
        assertThat(result, equalTo("?query"));
    }

    @Test
    public void givenPathAndQuery_whenGetResourceLocalComponents_thenResultEqualsExpected() {
        URI resource = URI.create("http://example.com/path?query");
        String result = utilities.getResourceLocalComponents(resource);
        assertThat(result, equalTo("/path?query"));
    }

    @Test
    public void givenPathAndFragment_whenGetResourceLocalComponents_thenResultEqualsExpected() {
        URI resource = URI.create("http://example.com/path#fragment");
        String result = utilities.getResourceLocalComponents(resource);
        assertThat(result, equalTo("/path#fragment"));
    }

    @Test
    public void givenPathAndQueryAndFragment_whenGetResourceLocalComponents_thenResultEqualsExpected() {
        URI resource = URI.create("http://example.com/path?query#fragment");
        String result = utilities.getResourceLocalComponents(resource);
        assertThat(result, equalTo("/path?query#fragment"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenMalformedResourceURI_whenGetRobotsURIForResource_thenThrowsIAE() {
        URI resourceUri = URI.create("http://mobil..bloggplatsen.se/rss/");
        utilities.getRobotsURIForResource(resourceUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenAnotherMalformedResourceURI_whenGetRobotsURIForResource_thenThrowsIAE() {
        URI resourceUri = URI.create("http://.mattbrailsford.com/2010/07/15/10-essential-umbraco-packages-for-seo/");
        utilities.getRobotsURIForResource(resourceUri);
    }
}
