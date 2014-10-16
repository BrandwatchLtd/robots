package com.brandwatch.robots.util;

import javax.annotation.Nonnull;

public interface Matcher<Type> {

    boolean matches(@Nonnull Type value);

}
