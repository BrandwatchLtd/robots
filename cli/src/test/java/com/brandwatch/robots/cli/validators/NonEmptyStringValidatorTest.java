package com.brandwatch.robots.cli.validators;

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
