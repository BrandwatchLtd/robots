package com.brandwatch.robots.parser;

import com.brandwatch.robots.AbstractDataTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;

import static org.mockito.Mockito.mock;

@RunWith(Parameterized.class)
public class RobotsTxtParserDataTest extends AbstractDataTest {

    public RobotsTxtParserDataTest(String resourceName) {
        super(resourceName);
    }

    private RobotsTxtHandler handler;

    @Before
    public void setup() throws IOException {
        handler = mock(RobotsTxtHandler.class);
    }


    @Test
    public void whenParse_thenNoExceptionThrown() throws IOException, ParseException {
        robotsTxtParser.parse(handler);
    }


}
