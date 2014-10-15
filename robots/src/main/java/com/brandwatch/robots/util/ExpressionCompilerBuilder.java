package com.brandwatch.robots.util;

import com.google.common.base.Function;
import com.google.common.base.Optional;

import javax.annotation.Nonnull;

public class ExpressionCompilerBuilder {

    private boolean caseSensitivity = true;
    private boolean leftBoundaryMatching = false;
    private Optional<Function<String, String>> expressionPreprocessor = Optional.absent();

    boolean isCaseSensitivity() {
        return caseSensitivity;
    }

    boolean isLeftBoundaryMatching() {
        return leftBoundaryMatching;
    }

    Optional<Function<String, String>> getExpressionPreprocessor() {
        return expressionPreprocessor;
    }

    public ExpressionCompilerBuilder withCaseSensitivity(boolean caseSensitivity) {
        this.caseSensitivity = caseSensitivity;
        return this;
    }

    public ExpressionCompilerBuilder withLeftBoundaryMatching(boolean leftBoundaryMatching) {
        this.leftBoundaryMatching = leftBoundaryMatching;
        return this;
    }

    public ExpressionCompilerBuilder withExpressionPreprocessor(@Nonnull Function<String, String> preprocessor) {
        this.expressionPreprocessor = Optional.of(preprocessor);
        return this;
    }

    public ExpressionCompiler build() {
        return new ExpressionCompilerImpl(this);
    }
}
