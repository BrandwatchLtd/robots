package com.brandwatch.robots.cli.converters;

import com.beust.jcommander.ParameterException;
import com.google.common.base.Charsets;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.Charset;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class CharsetConverterTest {

    private CharsetConverter converter;

    @Before
    public void setup() {
        converter = new CharsetConverter();
    }

    @Test(expected = NullPointerException.class)
    public void givenValueIsNull_whenConvert_thenThrowsNullPointerException() {
        converter.convert(null);
    }

    @Test(expected = ParameterException.class)
    public void givenValueIsEmpty_whenConvert_thenThrowsParameterException() {
        converter.convert("");
    }

    @Test
    public void givenValueIsUTF8_whenConvert_thenReturnsExpectedCharset() {
        Charset result = converter.convert("UTF8");
        assertThat(result, equalTo(Charsets.UTF_8));
    }

    @Test(expected = ParameterException.class)
    public void givenValueIsInvalidCharset_whenConvert_thenThrowsParameterException() {
        converter.convert("NOT_A_CHARSET");
    }

}
