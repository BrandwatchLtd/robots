package com.brandwatch.robots.util;

import javax.annotation.Nonnull;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;

public class Matchers {

    @Nonnull
    public static <Type> Matcher<Type> all() {
        return new AllMatcher<Type>();
    }

    @Nonnull
    public static <Type> Matcher<Type> none() {
        return new NoneMatcher<Type>();
    }

    @Nonnull
    public static <Type> Matcher<Type> constant(boolean matches) {
        return matches ? Matchers.<Type>all() : Matchers.<Type>none();
    }

    @Nonnull
    public static Matcher<String> fromPattern(Pattern pattern) {
        return new PatternMatcher(pattern);
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

    private static final class PatternMatcher implements Matcher<String> {

        @Nonnull
        private final Pattern pattern;

        public PatternMatcher(@Nonnull Pattern pattern) {
            checkNotNull(pattern, "pattern");
            this.pattern = pattern;
        }

        @Override
        public boolean matches(@Nonnull String value) {
            return pattern.matcher(value).matches();
        }
    }
}
