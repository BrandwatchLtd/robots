package com.brandwatch.robots.cli.validators;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.ParameterException;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.text.MessageFormat.format;

public class NonEmptyStringValidator implements IParameterValidator, IValueValidator<String> {

    @Override
    public void validate(@Nonnull String parameterName, @Nonnull String parameterValue) throws ParameterException {
        checkNotNull(parameterName, "parameterName");
        checkNotNull(parameterValue, "parameterValue");
        if (parameterValue.isEmpty()) {
            throw new ParameterException(format("Value of parameter {0} is the empty string.", parameterName));
        }
    }

}
