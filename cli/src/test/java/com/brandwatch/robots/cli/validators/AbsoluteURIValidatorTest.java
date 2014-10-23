package com.brandwatch.robots.cli.validators;

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
