package com.brandwatch.robots.cli.validators;

import com.beust.jcommander.ParameterException;
import org.junit.Before;
import org.junit.Test;

public class NonEmptyStringValidatorTest {

    private static final String VALID_PARAMETER_NAME = "";
    private static final String VALID_PARAMETER_VALUE = "value";

    private NonEmptyStringValidator validator;

    @Before
    public void setup() {
        validator = new NonEmptyStringValidator();
    }

    @Test(expected = NullPointerException.class)
    public void givenNullParameterName_whenValidate_thenThrowsNullPointerException() {
        validator.validate(null, VALID_PARAMETER_VALUE);
    }

    @Test(expected = NullPointerException.class)
    public void givenNullParameterValue_whenValidate_thenThrowsNullPointerException() {
        validator.validate(VALID_PARAMETER_NAME, null);
    }

    @Test(expected = ParameterException.class)
    public void givenEmptyParameter_whenValidate_thenParameterException() {
        validator.validate(VALID_PARAMETER_NAME, "");
    }

    @Test
    public void givenNonEmptyParameter_whenValidate_thenNoExceptionThrown() {
        validator.validate(VALID_PARAMETER_NAME, "X");
    }

}
