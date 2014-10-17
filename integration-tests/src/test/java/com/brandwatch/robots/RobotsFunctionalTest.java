package com.brandwatch.robots;

import org.junit.Before;
import org.junit.Test;

import java.net.URI;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class RobotsFunctionalTest {

    @Test
    public void standardUseCase() throws Exception {
        RobotsConfig config = new RobotsConfig();
        RobotsFactory factory = new RobotsFactory(config);
        RobotsService service = factory.createService();

        URI uri = URI.create("http://www.brandwatch.com/the-team/");
        boolean allowed = service.isAllowed("magpie", uri);
        assertThat(allowed, is(true));
    }

    @Test
    public void standardDisallow() throws Exception {
        RobotsConfig config = new RobotsConfig();
        RobotsFactory factory = new RobotsFactory(config);
        RobotsService service = factory.createService();

        URI uri = URI.create("http://www.brandwatch.com/wp-admin/");
        boolean allowed = service.isAllowed("magpie", uri);
        assertThat(allowed, is(false));
    }


    @Test
    public void timeout() throws Exception {
        RobotsConfig config = new RobotsConfig();
        config.setRequestTimeoutMillis(1);
        RobotsFactory factory = new RobotsFactory(config);
        RobotsService service = factory.createService();

        URI uri = URI.create("http://www.brandwatch.com/wp-admin/");
        boolean allowed = service.isAllowed("magpie", uri);
        assertThat(allowed, is(true));
    }

}
