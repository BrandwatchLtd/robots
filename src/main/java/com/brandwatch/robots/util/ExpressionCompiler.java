package com.brandwatch.robots.util;

import javax.annotation.Nonnull;

public interface ExpressionCompiler {

    @Nonnull
    Matcher<String> compile(@Nonnull String expression);

}
