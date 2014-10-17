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
public class RobotsParserRejectedDataTest {

    private final InputStream data;
    private RobotsParseHandler handler;
    private RobotsParser robotsTxtParser;

    public RobotsParserRejectedDataTest(String data) {
        this.data = new ByteArrayInputStream(data.getBytes());
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Iterable<Object[]> data() {
        ImmutableList.Builder<Object[]> builder = ImmutableList.<Object[]>builder()
                .add($("x"));

        // add iso control characters
        for(char i = 0x0; i <= 0x8; i++) {
            builder.add($("" + i));
        }
        builder.add($("" + (char)0xb));
        for(char i = 0x10; i <= 0x1f; i++) {
            builder.add($("" + i));
        }
        for(char i = 0x7F; i <= 0x9F; i++) {
            builder.add($("" + i));
        }

        return builder.build();
    }

    private static Object[] $(String str) {
        return new Object[]{str};
    }

    @Before
    public void setup() throws IOException {
        handler = mock(RobotsParseHandler.class);
        robotsTxtParser = new RobotsParser(data);
    }

    @Test(expected = ParseException.class)
    public void whenParse_thenThrowsParseException() throws IOException, ParseException {
        robotsTxtParser.parse(handler);
    }

}
