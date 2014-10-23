package com.brandwatch.robots.matching;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

final class ExpressionMatcher implements Matcher<String> {

    @Nonnull
    private final Pattern pattern;

    @Nonnegative
    private final double specificity;

    public ExpressionMatcher(@Nonnull Pattern pattern, @Nonnegative double specificity) {
        this.pattern = checkNotNull(pattern, "pattern");
        checkArgument(specificity >= 0, "specificity is negative");
        this.specificity = specificity;
    }

    @Override
    public boolean matches(@Nonnull String value) {
        return pattern.matcher(value).matches();
    }

    @Override
    @Nonnegative
    public double getSpecificity() {
        return specificity;
    }
}
