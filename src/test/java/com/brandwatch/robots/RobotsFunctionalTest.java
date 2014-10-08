package com.brandwatch.robots;

import com.brandwatch.robots.net.RobotsCharSourceFactory;
import com.google.common.base.Charsets;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;

import static com.google.common.io.Resources.asCharSource;
import static com.google.common.io.Resources.getResource;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class RobotsFunctionalTest {

    private RobotExclusionService service;

    @Before
    public void setup() {

        // mock sourceFactory so we don't cause network IO
        RobotsCharSourceFactory sourceFactory = mock(RobotsCharSourceFactory.class);
        when(sourceFactory.createFor(any(URI.class))).thenReturn(
                asCharSource(getResource(RobotsFunctionalTest.class,
                        "http_www.brandwatch.com_robots.txt"), Charsets.UTF_8));

        RobotExclusionConfig config = spy(new RobotExclusionConfig());
        when(config.getRobotsCharSourceFactory()).thenReturn(sourceFactory);

        service = config.getRobotExclusionService();
    }

    @Test
    public void standardUseCase() throws Exception {
        URI uri = URI.create("http://www.brandwatch.com/the-team/");
        boolean allowed = service.isAllowed("magpie", uri);
        assertThat(allowed, is(true));
    }

    @Test
    public void standardDisallow() throws Exception {
        URI uri = URI.create("http://www.brandwatch.com/wp-admin/");
        boolean allowed = service.isAllowed("magpie", uri);
        assertThat(allowed, is(false));
    }

}
