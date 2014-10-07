package com.brandwatch.robots;

import com.brandwatch.robots.net.RobotsCharSourceFactory;
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
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class RobotExclusionServiceImplTest {

    @Mock
    private RobotsCharSourceFactory sourceFactory;

    @Mock
    private RobotsDownloader downloader;

    @Mock
    private RobotsUtilities utilities;

    @InjectMocks
    private RobotExclusionServiceImpl instance;

    @Before
    public final void startUp() throws Exception {
        instance.startUp();
    }

    @After
    public final void shutDown() throws Exception {
        instance.shutDown();
    }

    @Test(expected = NullPointerException.class)
    public void givenNullUri_whenIsAllowed_thenThrowsNPE() {
        URI resourceUri = null;
        instance.isAllowed(resourceUri);
    }

    @Test
    public void givenExampleUri_whenIsAllowed_thenReturnsTrue() {
        URI resourceUri = URI.create("http://example.org/absolute/URI/with/absolute/path/to/resource.txt");
        boolean result = instance.isAllowed(resourceUri);
        assertThat(result, is(true));
    }

    @Test
    public void givenExampleUri_whenIsAllowed_thenSourceFactoryIsInvoked() {
        URI resourceUri = URI.create("http://example.org/index.html");
        instance.isAllowed(resourceUri);
        verify(sourceFactory).createFor(resourceUri);
    }
}
