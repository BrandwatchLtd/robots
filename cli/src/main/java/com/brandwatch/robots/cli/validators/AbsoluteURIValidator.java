package com.brandwatch.robots.cli.validators;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.ParameterException;
import com.brandwatch.robots.cli.converters.URIConverter;

import javax.annotation.Nonnull;
import java.net.URI;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.text.MessageFormat.format;

public class AbsoluteURIValidator implements IParameterValidator, IValueValidator<URI> {

    private static final URIConverter CONVERTER = new URIConverter();

    @Override
    public void validate(@Nonnull String parameterName, @Nonnull String parameterValue) throws ParameterException {
        checkNotNull(parameterName, "parameterName is null");
        checkNotNull(parameterValue, "parameterValue is null");

        validate(parameterName, CONVERTER.convert(parameterValue));
    }

    @Override
    public void validate(@Nonnull String parameterName, @Nonnull URI parameterValue) throws ParameterException {
        checkNotNull(parameterName, "parameterName is null");
        checkNotNull(parameterValue, "parameterValue is null");

        parameterValue = parameterValue.normalize();
        if (!parameterValue.isAbsolute()) {
            throw new ParameterException(format("Parameter {0} contains relative URI: {1}", parameterName, parameterValue));
        }
        if (parameterValue.getHost() == null || parameterValue.getHost().isEmpty()) {
            throw new ParameterException(format("Parameter {0} URI missing host: {1}", parameterName, parameterValue));
        }

    }

}
