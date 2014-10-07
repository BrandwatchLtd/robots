package com.brandwatch.robots;

import com.brandwatch.robots.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class RobotsBuildingHandlerDataTest extends AbstractDataTest {

    private RobotsBuildingHandler handler;

    public RobotsBuildingHandlerDataTest(String resourceName) {
        super(resourceName);
    }

    @Before
    public void setup() throws IOException {
        handler = new RobotsBuildingHandler();
    }

    @Test
    public void whenParser_thenHandlerReturnsNonNull() throws IOException, ParseException {
        robotsTxtParser.parse(handler);
        assertThat(handler.get(), notNullValue());
    }

}