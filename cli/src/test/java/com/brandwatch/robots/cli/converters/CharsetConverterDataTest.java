package com.brandwatch.robots.cli.converters;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.nio.charset.Charset;

import static com.brandwatch.robots.cli.TestUtils.$;
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

                .add($("ISO-8859-1", Charsets.ISO_8859_1))
                .add($("iso-ir-100", Charsets.ISO_8859_1))
                .add($("csISOLatin1", Charsets.ISO_8859_1))
                .add($("latin1", Charsets.ISO_8859_1))
                .add($("l1", Charsets.ISO_8859_1))
                .add($("IBM819", Charsets.ISO_8859_1))
                .add($("CP819", Charsets.ISO_8859_1))

                .add($("ascii", Charsets.US_ASCII))
                .add($("ANSI_X3.4-1968", Charsets.US_ASCII))
                .add($("ISO_646.irv:1991", Charsets.US_ASCII))
                .add($("cp367", Charsets.US_ASCII))
                .add($("iso-ir-6", Charsets.US_ASCII))
                .add($("ASCII", Charsets.US_ASCII))
                .add($("us", Charsets.US_ASCII))
                .add($("csASCII", Charsets.US_ASCII))
                .add($("ANSI_X3.4-1986", Charsets.US_ASCII))
                .add($("ISO646-US", Charsets.US_ASCII))
                .add($("IBM367", Charsets.US_ASCII))

                .add($("UTF8", Charsets.UTF_8))
                .add($("UTF-8", Charsets.UTF_8))

                .add($("UTF_16", Charsets.UTF_16))
                .add($("UTF-16", Charsets.UTF_16))
                .add($("UTF16", Charsets.UTF_16))
                .add($("unicode", Charsets.UTF_16))

                .add($("UTF_16BE", Charsets.UTF_16BE))
                .add($("UTF-16BE", Charsets.UTF_16BE))

                .add($("UTF_16LE", Charsets.UTF_16LE))
                .add($("UTF-16LE", Charsets.UTF_16LE))

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
