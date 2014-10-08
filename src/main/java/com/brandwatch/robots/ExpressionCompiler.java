package com.brandwatch.robots;

import com.google.common.base.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.regex.Pattern.compile;
import static java.util.regex.Pattern.quote;

public class ExpressionCompiler implements Function<String, Matcher<String>> {

    static final Matcher<String> ALL = new ConstantMatcher(true);
    static final Matcher<String> NONE = new ConstantMatcher(false);

    @Nullable
    @Override
    public Matcher<String> apply(String expression) {
        checkNotNull(expression, "expression");
        if (expression.isEmpty()) {
            return ALL;
        } else {
            return new PatternMatcher(compileWildcardExpression(expression));
        }
    }

    @Nonnull
    private Pattern compileWildcardExpression(@Nonnull String expression) {
        final StringBuilder regex = new StringBuilder().append("^");

        if (expression.isEmpty()) {
            return Pattern.compile(".*?");
        }

        final int len = expression.length();
        int start = 0;
        int end;
        while ((end = expression.indexOf('*', start)) != -1) {
            regex.append(quote(expression.substring(start, end)))
                    .append(".*?");
            start = end + 1;
        }

        if (expression.charAt(len - 1) == '$') {
            regex.append(quote(expression.substring(start, len - 1)))
                    .append('$');
        } else {
            regex.append(quote(expression.substring(start, len)))
                    .append(".*?");
        }

        return compile(regex.toString());
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
