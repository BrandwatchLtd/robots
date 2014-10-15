package com.brandwatch.robots.domain;

import javax.annotation.Nonnull;

public interface Directive {

    @Nonnull
    String getField();

    @Nonnull
    String getValue();

}
