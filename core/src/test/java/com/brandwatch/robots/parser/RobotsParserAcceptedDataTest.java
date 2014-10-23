package com.brandwatch.robots.parser;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.mockito.Mockito.mock;

@RunWith(Parameterized.class)
public class RobotsParserAcceptedDataTest {

    private final InputStream data;
    private RobotsParseHandler handler;
    private RobotsParser robotsTxtParser;

    public RobotsParserAcceptedDataTest(String data) {
        this.data = new ByteArrayInputStream(data.getBytes());
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Iterable<Object[]> data() {
        return ImmutableList.<Object[]>builder()
                .add(array(""))
                .add(array("#"))
                .add(array("#\n#\r#\r\n#\n\r#"))
                .add(array("User-agent: *\nAllow: /"))
                .add(array("User-agent: *\rAllow: /"))
                .add(array("User-agent: *\n\rAllow: /"))
                .add(array("User-agent: *\nAllow: /\n"))
                .add(array("User-agent: *\nAllow: \n"))
                .add(array("User-agent: *\nAllow:#\n"))
                .add(array("User-agent: *\nAllow:\n"))
                .add(array("User-agent: *\nAllow:"))
                .add(array("User-agent: *\nAllow: "))
                .add(array("User-agent: *\nAllow:#"))
                .add(array("User-agent:\nAllow:"))
                .add(array("Things:\nStuff:"))
                .build();
    }

    private static Object[] array(String str) {
        return new Object[]{str};
    }

    @Before
    public void setup() throws IOException {
        handler = mock(RobotsParseHandler.class);
        robotsTxtParser = new RobotsParser(data);
    }

    @Test
    public void whenParse_thenNoExceptionThrown() throws IOException, ParseException {
        robotsTxtParser.parse(handler);
    }

}
