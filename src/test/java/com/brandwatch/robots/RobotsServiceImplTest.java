package com.brandwatch.robots;

import com.brandwatch.robots.domain.Robots;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URI;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RobotsServiceImplTest {

    @Mock
    private RobotsLoader loader;

    @Mock
    private RobotsUtilities utilities;

    @Mock
    private RobotsConfig config;

    private RobotsServiceImpl instance;

    @Before
    public final void startUp() throws Exception {

        when(config.getService()).thenReturn(instance);
        when(config.getLoader()).thenReturn(loader);
        when(config.getUtilities()).thenReturn(utilities);

        when(loader.load(any(URI.class))).thenReturn(new Robots.Builder().build());
        instance = new RobotsServiceImpl(config);
        instance.startAsync();
        instance.awaitRunning();
    }

    @After
    public final void shutDown() throws Exception {
        instance.stopAsync();
        instance.awaitTerminated();
    }

    @Test(expected = NullPointerException.class)
    public void givenNullUri_whenIsAllowed_thenThrowsNPE() {
        String crawlerAgent = "magpie";
        URI resourceUri = null;
        instance.isAllowed(crawlerAgent, resourceUri);
    }

    @Test(expected = NullPointerException.class)
    public void givenNullAgent_whenIsAllowed_thenThrowsNPE() {
        String crawlerAgent = null;
        URI resourceUri = URI.create("http://example.org/index.html");
        instance.isAllowed(crawlerAgent, resourceUri);
    }

    @Test
    public void givenExampleUri_whenIsAllowed_thenReturnsTrue() {
        String crawlerAgent = "magpie";
        URI resourceUri = URI.create("http://example.org/absolute/URI/with/absolute/path/to/resource.txt");
        boolean result = instance.isAllowed(crawlerAgent, resourceUri);
        assertThat(result, is(true));
    }

    @Test
    public void givenExampleUri_whenIsAllowed_thenSourceFactoryIsInvoked() {
        String crawlerAgent = "magpie";
        URI resourceUri = URI.create("http://example.org/index.html");
        instance.isAllowed(crawlerAgent, resourceUri);
        verify(utilities).getRobotsURIForResource(resourceUri);
    }
}
