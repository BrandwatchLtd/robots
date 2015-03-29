package com.brandwatch.robots.cli.converters;

/*
 * #%L
 * Robots (command-line interface)
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

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.nio.charset.Charset;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class CharsetConverterDataTest {

    private CharsetConverter converter;

    private final String value;
    private final Charset expected;

    public CharsetConverterDataTest(String value, Charset expected) {
        this.value = checkNotNull(value, "value is null");
        this.expected = checkNotNull(expected, "expected is null");
    }

    @Parameterized.Parameters(name = "{index}: {0} => {1}")
    public static Iterable<Object[]> data() {
        return ImmutableList.<Object[]>builder()

                .add(new Object[]{"ISO-8859-1", Charsets.ISO_8859_1})
                .add(new Object[]{"iso-ir-100", Charsets.ISO_8859_1})
                .add(new Object[]{"csISOLatin1", Charsets.ISO_8859_1})
                .add(new Object[]{"latin1", Charsets.ISO_8859_1})
                .add(new Object[]{"l1", Charsets.ISO_8859_1})
                .add(new Object[]{"IBM819", Charsets.ISO_8859_1})
                .add(new Object[]{"CP819", Charsets.ISO_8859_1})

                .add(new Object[]{"ascii", Charsets.US_ASCII})
                .add(new Object[]{"ANSI_X3.4-1968", Charsets.US_ASCII})
                .add(new Object[]{"ISO_646.irv:1991", Charsets.US_ASCII})
                .add(new Object[]{"cp367", Charsets.US_ASCII})
                .add(new Object[]{"iso-ir-6", Charsets.US_ASCII})
                .add(new Object[]{"ASCII", Charsets.US_ASCII})
                .add(new Object[]{"us", Charsets.US_ASCII})
                .add(new Object[]{"csASCII", Charsets.US_ASCII})
                .add(new Object[]{"ANSI_X3.4-1986", Charsets.US_ASCII})
                .add(new Object[]{"ISO646-US", Charsets.US_ASCII})
                .add(new Object[]{"IBM367", Charsets.US_ASCII})

                .add(new Object[]{"UTF8", Charsets.UTF_8})
                .add(new Object[]{"UTF-8", Charsets.UTF_8})

                .add(new Object[]{"UTF_16", Charsets.UTF_16})
                .add(new Object[]{"UTF-16", Charsets.UTF_16})
                .add(new Object[]{"UTF16", Charsets.UTF_16})
                .add(new Object[]{"unicode", Charsets.UTF_16})

                .add(new Object[]{"UTF_16BE", Charsets.UTF_16BE})
                .add(new Object[]{"UTF-16BE", Charsets.UTF_16BE})

                .add(new Object[]{"UTF_16LE", Charsets.UTF_16LE})
                .add(new Object[]{"UTF-16LE", Charsets.UTF_16LE})

                .build();
    }

    @Before
    public void setup() {
        converter = new CharsetConverter();
    }

    @Test
    public void givenLatin1Value_withDash_whenConvert_thenReturnsExpectedCharset() {
        Charset result = converter.convert(value);
        assertThat(result, equalTo(expected));
    }

}
