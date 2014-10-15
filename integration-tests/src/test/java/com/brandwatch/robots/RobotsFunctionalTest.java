package com.brandwatch.robots;

import org.junit.Before;
import org.junit.Test;

import java.net.URI;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class RobotsFunctionalTest {

    private RobotsService service;

    @Before
    public void setup() {
        RobotsConfig config = new RobotsConfig();
        RobotsFactory factory = new RobotsFactory(config);
        service = factory.createService();
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
