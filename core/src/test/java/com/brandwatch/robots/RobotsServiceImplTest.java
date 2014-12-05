package com.brandwatch.robots;

import com.brandwatch.robots.domain.AgentDirective;
import com.brandwatch.robots.domain.Group;
import com.brandwatch.robots.domain.PathDirective;
import com.brandwatch.robots.domain.Robots;
import com.brandwatch.robots.matching.EverythingMatcher;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
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

    @InjectMocks
    private RobotsServiceImpl instance;

    @Before
    public final void startUp() throws Exception {
        when(loader.load(any(URI.class))).thenReturn(new Robots.Builder().build());
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

    @Test(expected = IllegalArgumentException.class)
    public void givenMalformedResourceURI_whenIsAllowed_thenThrowsIAE() {
        String crawlerAgent = "magpie";
        URI resourceUri = URI.create("http://example.org/index.html");
        when(utilities.getRobotsURIForResource(any(URI.class))).thenThrow(IllegalArgumentException.class);
        instance.isAllowed(crawlerAgent, resourceUri);
    }
}
