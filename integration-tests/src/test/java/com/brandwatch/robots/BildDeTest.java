package com.brandwatch.robots;

import com.brandwatch.robots.net.CharSourceSupplier;
import com.google.common.base.Charsets;
import com.google.common.io.CharSource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;

import static com.google.common.io.Resources.asCharSource;
import static com.google.common.io.Resources.getResource;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BildDeTest {

    @Spy
    private RobotsUtilities utilities;
    @Mock
    private CharSourceSupplier charSourceSupplier;

    private RobotsService service;

    private String agent = "magpie-crawler/1.1 (U; Linux x86_64; en-GB; +http://www.brandwatch.net)";

    @Before
    public void setup() {
        RobotsConfig config = new RobotsConfig();
        RobotsFactory factory = spy(new RobotsFactory(config));
        when(factory.getUtilities()).thenReturn(utilities);
        when(factory.createCharSourceSupplier()).thenReturn(charSourceSupplier);

        final CharSource fakeSource = asCharSource(
                getResource(getClass(), "http_www.bild.de_robots_20141017.txt"),
                Charsets.UTF_8);
        when(charSourceSupplier.get(any(URI.class))).thenReturn(fakeSource);

        service = factory.createService();
    }

    @Test
    public void givenRssFeedPath_isAllowed_thenReturnsTrue() throws Exception {
        boolean allowed = service.isAllowed(agent, URI.create("http://www.bild.de/rssfeeds/"));
        assertThat(allowed, is(true));
    }

    @Test
    public void givenRootPath_isAllowed_thenReturnsFalse() throws Exception {
        boolean allowed = service.isAllowed(agent, URI.create("http://www.bild.de/"));
        assertThat(allowed, is(false));
    }

}
