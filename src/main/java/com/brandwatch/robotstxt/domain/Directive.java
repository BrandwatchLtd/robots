package com.brandwatch.robotstxt.domain;

import javax.annotation.Nonnull;

public interface Directive {

    @Nonnull
    String getField();

    @Nonnull
    String getValue();

}
