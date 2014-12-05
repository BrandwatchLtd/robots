package com.brandwatch.robots;

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
