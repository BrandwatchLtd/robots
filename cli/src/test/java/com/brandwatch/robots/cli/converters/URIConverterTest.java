package com.brandwatch.robots.cli.converters;

import com.beust.jcommander.ParameterException;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class URIConverterTest {

    private URIConverter converter;

    @Before
    public void setup() {
        converter = new URIConverter();
    }

    @Test(expected = NullPointerException.class)
    public void givenValueIsNull_whenConvert_thenThrowsNullPointerException() {
        converter.convert(null);
    }

    @Test
    public void givenBlankURI_whenConvert_thenReturnsExpectedURI() {
        URI result = converter.convert("");
        assertThat(result, equalTo(URI.create("")));
    }

    @Test(expected = ParameterException.class)
    public void givenInvalidURI_whenConvert_thenThrowsParameterException() {
        converter.convert(":");
    }

    @Test
    public void givenValidURI_whenConvert_thenReturnsExpectedURI() {
        URI result = converter.convert("http://www.example.com/index.html");
        assertThat(result, equalTo(URI.create("http://www.example.com/index.html")));
    }

    @Test
    public void givenValidIDN_whenConvert_thenReturnsExpectedURI() {
        URI result = converter.convert("http://detrèsbonsdomaines.com");
        assertThat(result, equalTo(URI.create("http://xn--detrsbonsdomaines-vsb.com")));
    }

    @Test
    public void givenValidIDN2_whenConvert_thenReturnsExpectedURI() {
        URI result = converter.convert("http://名がドメイン.com");
        assertThat(result, equalTo(URI.create("http://xn--v8jxj3d1dzdz08w.com")));
    }


    @Test
    public void givenValidIDN3_whenConvert_thenReturnsExpectedURI() {
        URI result = converter.convert("http://detrèsbonsdomaines.com");
        assertThat(result.toString(), equalTo("http://xn--detrsbonsdomaines-vsb.com"));
    }


}
