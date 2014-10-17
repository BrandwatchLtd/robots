package com.brandwatch.robots.matching;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

public class EverythingMatcher<Type> implements Matcher<Type> {

    @Override
    public boolean matches(@Nonnull Type value) {
        return true;
    }

    @Nonnegative
    @Override
    public double getSpecificity() {
        return 0;
    }

}
