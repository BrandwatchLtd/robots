package com.brandwatch.robots.util;

import javax.annotation.Nonnull;

public class Matchers {

    @Nonnull
    public static <Type> Matcher<Type> all() {
        return new AllMatcher<Type>();
    }

    @Nonnull
    public static <Type> Matcher<Type> none() {
        return new NoneMatcher<Type>();
    }

    private static class AllMatcher<Type> implements Matcher<Type> {
        @Override
        public boolean matches(@Nonnull Type value) {
            return true;
        }
    }

    private static class NoneMatcher<Type> implements Matcher<Type> {
        @Override
        public boolean matches(@Nonnull Type value) {
            return false;
        }
    }
}
