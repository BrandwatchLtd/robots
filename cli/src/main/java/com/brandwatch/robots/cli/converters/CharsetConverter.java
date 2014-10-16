package com.brandwatch.robots.cli.converters;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.ParameterException;

import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;

import static com.google.common.base.Preconditions.checkNotNull;

public class CharsetConverter implements IStringConverter<Charset> {

    @Override
    public Charset convert(String parameterValue) {
        checkNotNull(parameterValue, "parameterValue is null");
        try {
            return Charset.forName(parameterValue);
        } catch (UnsupportedCharsetException e) {
            throw new ParameterException("Unsupported character set: " + e.getMessage());
        } catch (IllegalCharsetNameException e) {
            throw new ParameterException("Illegal character set name: " + e.getMessage());
        }
    }

}
