package com.brandwatch.robotstxt.parser;

import javax.annotation.Nonnull;

public interface RobotsTxtHandler {

    void startEntry();

    void userAgent(@Nonnull String pattern);

    void allow(@Nonnull String pattern);

    void disallow(@Nonnull String pattern);

    void endEntry();

    void siteMap(@Nonnull String url);

    void otherDirective(@Nonnull String field, @Nonnull String value);

}
