package com.brandwatch.robots.matching;

import javax.annotation.Nonnull;

public interface ExpressionCompiler {

    @Nonnull
    Matcher<String> compile(@Nonnull String expression);

}
