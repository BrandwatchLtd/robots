package com.brandwatch.robots;

import javax.annotation.Nonnull;

public interface Matcher<Type> {

    boolean matches(@Nonnull Type value);

}
