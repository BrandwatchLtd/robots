package com.brandwatch.robots.parser;

/*
 * #%L
 * Robots (core)
 * %%
 * Copyright (C) 2015 Brandwatch
 * %%
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the Brandwatch nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

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
                .add(array("x"));

        // add iso control characters
        for(char i = 0x0; i <= 0x8; i++) {
            builder.add(array("" + i));
        }
        builder.add(array("" + (char) 0xb));
        for(char i = 0x10; i <= 0x1f; i++) {
            builder.add(array("" + i));
        }
        for(char i = 0x7F; i <= 0x9F; i++) {
            builder.add(array("" + i));
        }

        return builder.build();
    }

    private static Object[] array(String str) {
        return new Object[]{str};
    }

    @Before
    public void setup() throws IOException {
        handler = mock(RobotsParseHandler.class);
        robotsTxtParser = new RobotsParserImpl(data);
    }

    @Test(expected = ParseException.class)
    public void whenParse_thenThrowsParseException() throws IOException, ParseException {
        robotsTxtParser.parse(handler);
    }

}
