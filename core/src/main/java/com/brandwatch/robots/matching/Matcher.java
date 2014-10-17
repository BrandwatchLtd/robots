package com.brandwatch.robots.matching;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public interface Matcher<Type> {

    boolean matches(@Nonnull Type value);

    @Nonnegative
    double getSpecificity();

}
