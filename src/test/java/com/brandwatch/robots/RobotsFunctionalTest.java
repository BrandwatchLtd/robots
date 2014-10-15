package com.brandwatch.robots;

import org.junit.Before;
import org.junit.Test;

import java.net.URI;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class RobotsFunctionalTest {

    private RobotsService service;

    @Before
    public void setup() {

        // mock sourceFactory so we don't cause network IO
        RobotsUtilities utilities = spy(new RobotsUtilities());

//        doReturn(asCharSource(getResource(RobotsFunctionalTest.class,
//                "http_www.brandwatch.com_robots.txt"), Charsets.UTF_8))
//                .when(utilities).createCharSourceFor(any(URI.class), anyLong());

        RobotsConfig config = spy(new RobotsConfig());
        when(config.getUtilities()).thenReturn(utilities);

        service = config.getService();
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
