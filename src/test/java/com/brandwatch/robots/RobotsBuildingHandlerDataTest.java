package com.brandwatch.robots;

import com.brandwatch.robots.parser.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class RobotsBuildingHandlerDataTest extends AbstractDataTest {

    private RobotsBuildingHandler handler;

    public RobotsBuildingHandlerDataTest(String resourceName) {
        super(resourceName);
    }

    @Before
    public void setup() throws IOException {
        RobotsUtilities utilities = mock(RobotsUtilities.class);
        when(utilities.compilePathExpression(anyString())).thenReturn(Pattern.compile(""));
        handler = new RobotsBuildingHandler(utilities);
    }

    @Test
    public void whenParser_thenHandlerReturnsNonNull() throws IOException, ParseException {
        robotsTxtParser.parse(handler);
        assertThat(handler.get(), notNullValue());
    }

}