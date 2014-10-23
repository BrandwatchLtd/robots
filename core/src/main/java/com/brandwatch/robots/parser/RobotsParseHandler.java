package com.brandwatch.robots.parser;

import javax.annotation.Nonnull;

public interface RobotsParseHandler {

    void startEntry();

    void userAgent(@Nonnull String pattern);

    void allow(@Nonnull String pattern);

    void disallow(@Nonnull String pattern);

    void endEntry();

    void siteMap(@Nonnull String url);

    void otherDirective(@Nonnull String field, @Nonnull String value);

}
