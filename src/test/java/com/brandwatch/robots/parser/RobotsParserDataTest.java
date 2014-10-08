package com.brandwatch.robots.parser;

import com.brandwatch.robots.AbstractDataTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;

import static org.mockito.Mockito.mock;

@RunWith(Parameterized.class)
public class RobotsParserDataTest extends AbstractDataTest {

    private RobotsParseHandler handler;

    public RobotsParserDataTest(String resourceName) {
        super(resourceName);
    }

    @Before
    public void setup() throws IOException {
        handler = mock(RobotsParseHandler.class);
    }


    @Test
    public void whenParse_thenNoExceptionThrown() throws IOException, ParseException {
        robotsTxtParser.parse(handler);
    }


}
