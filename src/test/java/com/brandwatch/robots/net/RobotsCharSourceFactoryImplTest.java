package com.brandwatch.robots.net;

import com.google.common.io.CharSource;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class RobotsCharSourceFactoryImplTest {


    private RobotsCharSourceFactoryImpl sourceFactory;

    @Before
    public void setup() {
        sourceFactory = new RobotsCharSourceFactoryImpl();
    }

    @Test(expected = NullPointerException.class)
    public void givenNullUri_whenCreateFor_thenThrowsNPE() {
        URI resourceUri = null;
        sourceFactory.createFor(resourceUri);
    }

    @Test
    public void givenValidUri_whenCreateFor_thenReturnsSource() {
        URI resourceUri = URI.create("http://www.example.com:80/");
        CharSource source = sourceFactory.createFor(resourceUri);
        assertThat(source, notNullValue());
    }

    @Test
    public void givenMissingPort_whenCreateFor_thenReturnsSource() {
        URI resourceUri = URI.create("http://www.example.com");
        CharSource source = sourceFactory.createFor(resourceUri);
        assertThat(source, notNullValue());
    }

    @Test
    public void givenMissingSchema_whenCreateFor_thenReturnsSource() throws MalformedURLException, URISyntaxException {
        URI resourceUri = URI.create("fakeschema://www.example.com:80");
        CharSource source = sourceFactory.createFor(resourceUri);
        assertThat(source, notNullValue());
    }


}
