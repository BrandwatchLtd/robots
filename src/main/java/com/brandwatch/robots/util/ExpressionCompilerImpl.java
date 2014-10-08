package com.brandwatch.robots.util;

import com.google.common.base.Function;
import com.google.common.base.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.regex.Pattern.quote;

class ExpressionCompilerImpl implements ExpressionCompiler {

    static final Matcher<String> ALL = new ConstantMatcher(true);
    static final Matcher<String> NONE = new ConstantMatcher(false);

    private final boolean caseSensitivity;
    private final boolean leftBoundaryMatching;
    private final Optional<Function<String, String>> expressionPreprocessor;

    ExpressionCompilerImpl(@Nonnull ExpressionCompilerBuilder builder) {
        this.caseSensitivity = builder.isCaseSensitivity();
        this.leftBoundaryMatching = builder.isLeftBoundaryMatching();
        this.expressionPreprocessor = builder.getExpressionPreprocessor();
    }

    @Nullable
    @Override
    public Matcher<String> compile(String expression) {
        checkNotNull(expression, "expression");

        if (expressionPreprocessor.isPresent()) {
            expression = expressionPreprocessor.get().apply(expression);
        }

        if (expression.isEmpty()) {
            return ALL;
        } else {
            return new PatternMatcher(compileWildcardExpressionToRegex(expression));
        }
    }

    @Nonnull
    private Pattern compileWildcardExpressionToRegex(@Nonnull String expression) {
        final StringBuilder regex = new StringBuilder().append("^");

        final int len = expression.length();
        int start = 0;
        int end;

        if (len > 0 && expression.charAt(0) == '^') {
            regex.append('^');
            start++;
        } else if (leftBoundaryMatching) {
            regex.append('^');
        } else {
            regex.append(".*?");
        }

        while ((end = expression.indexOf('*', start)) != -1) {
            regex.append(quote(expression.substring(start, end)))
                    .append(".*?");
            start = end + 1;
        }

        if (len > 0 && expression.charAt(len - 1) == '$') {
            regex.append(quote(expression.substring(start, len - 1)))
                    .append('$');
        } else {
            regex.append(quote(expression.substring(start, len)));
            regex.append(".*?");
        }

        int flags = 0;
        if (!caseSensitivity) {
            flags |= Pattern.CASE_INSENSITIVE;
        }

        return Pattern.compile(regex.toString(), flags);
    }


    private static final class ConstantMatcher implements Matcher<String> {

        private final boolean result;

        private ConstantMatcher(boolean result) {
            this.result = result;
        }

        @Override
        public boolean matches(@Nonnull String value) {
            return result;
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
