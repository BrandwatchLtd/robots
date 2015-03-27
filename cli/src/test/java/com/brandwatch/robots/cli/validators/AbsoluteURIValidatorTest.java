package com.brandwatch.robots.cli.validators;

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

import com.beust.jcommander.ParameterException;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;

public class AbsoluteURIValidatorTest {

    private static final String VALID_PARAMETER_NAME = "";

    private AbsoluteURIValidator validator;

    @Before
    public void setup() {
        validator = new AbsoluteURIValidator();
    }

    @Test(expected = NullPointerException.class)
    public void givenNullParameterName_withStringValue_whenValidate_thenThrowsNullPointerException() {
        validator.validate(null, "");
    }

    @Test(expected = NullPointerException.class)
    public void givenNullParameterName_withURIValue_whenValidate_thenThrowsNullPointerException() {
        validator.validate(null, URI.create(""));
    }

    @Test(expected = NullPointerException.class)
    public void givenNullParameterStringValue_whenValidate_thenThrowsNullPointerException() {
        validator.validate(VALID_PARAMETER_NAME, (String) null);
    }

    @Test(expected = NullPointerException.class)
    public void givenNullParameterURIValue_whenValidate_thenThrowsNullPointerException() {
        validator.validate(VALID_PARAMETER_NAME, (URI) null);
    }

    @Test
    public void givenValidURIStringValue_whenValidate_thenNoExceptionThrown() {
        validator.validate(VALID_PARAMETER_NAME, "http://example.com");
    }

    @Test(expected = ParameterException.class)
    public void givenInvalidURIStringValue_whenValidate_thenThrowsParameterException() {
        validator.validate(VALID_PARAMETER_NAME, ":");
    }

    @Test(expected = ParameterException.class)
    public void givenURIMissingHostStringValue_whenValidate_thenThrowsParameterException() {
        validator.validate(VALID_PARAMETER_NAME, "http:");
    }

    @Test(expected = ParameterException.class)
    public void givenURIMissingHostAndSchemeStringValue1_whenValidate_thenThrowsParameterException() {
        validator.validate(VALID_PARAMETER_NAME, "/index.html");
    }

    @Test(expected = ParameterException.class)
    public void givenURIMissingHostAndSchemeStringValue2_whenValidate_thenThrowsParameterException() {
        validator.validate(VALID_PARAMETER_NAME, "index.html");
    }

    @Test(expected = ParameterException.class)
    public void givenURIMissingSchemeStringValue1_whenValidate_thenThrowsParameterException() {
        validator.validate(VALID_PARAMETER_NAME, ":example.com");
    }

    @Test(expected = ParameterException.class)
    public void givenURIMissingSchemeStringValue2_whenValidate_thenThrowsParameterException() {
        validator.validate(VALID_PARAMETER_NAME, "example.com");
    }

    @Test(expected = ParameterException.class)
    public void givenURIMissingSchemeStringValue3_whenValidate_thenThrowsParameterException() {
        validator.validate(VALID_PARAMETER_NAME, "//:example.com");
    }

    @Test(expected = ParameterException.class)
    public void givenURIMissingHostAndScheme1_whenValidate_thenThrowsParameterException() {
        validator.validate(VALID_PARAMETER_NAME, URI.create("/index.html"));
    }

    @Test(expected = ParameterException.class)
    public void givenURIMissingHostAndScheme2_whenValidate_thenThrowsParameterException() {
        validator.validate(VALID_PARAMETER_NAME, URI.create("index.html"));
    }

    @Test(expected = ParameterException.class)
    public void givenURIMissingScheme2_whenValidate_thenThrowsParameterException() {
        validator.validate(VALID_PARAMETER_NAME, URI.create("example.com"));
    }

    @Test(expected = ParameterException.class)
    public void givenURIMissingScheme3_whenValidate_thenThrowsParameterException() {
        validator.validate(VALID_PARAMETER_NAME, URI.create("//:example.com"));
    }

    @Test(expected = ParameterException.class)
    public void givenURIMissingHost_whenValidate_thenThrowsParameterException() {
        validator.validate(VALID_PARAMETER_NAME, URI.create("http://?query"));
    }

    @Test(expected = ParameterException.class)
    public void givenURIMissingHost2_whenValidate_thenThrowsParameterException() {
        validator.validate(VALID_PARAMETER_NAME, URI.create("http://#fragment"));
    }

}
