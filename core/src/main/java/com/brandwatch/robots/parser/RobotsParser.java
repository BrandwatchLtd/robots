package com.brandwatch.robots.parser;

import javax.annotation.Nonnull;

public interface RobotsParser {

    void parse(@Nonnull RobotsParseHandler handler) throws ParseException;

}
