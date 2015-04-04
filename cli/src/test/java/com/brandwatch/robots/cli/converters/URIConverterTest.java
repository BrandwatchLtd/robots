package com.brandwatch.robots.cli.converters;

/*
 * #%L
 * Robots (command-line interface)
 * %%
 * Copyright (C) 2014 - 2015 Brandwatch
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
