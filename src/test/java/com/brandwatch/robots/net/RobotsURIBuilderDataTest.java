package com.brandwatch.robots.net;

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

    private RobotsURIBuilder builder;
    private String resource;
    private String robots;

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
