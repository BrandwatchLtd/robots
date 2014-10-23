package com.brandwatch.robots.matching;

import com.google.common.base.Function;
import com.google.common.base.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.regex.Pattern.quote;

class ExpressionCompilerImpl implements ExpressionCompiler {

    private static final String RE_LITERAL_PREFIX = "(?>";
    private static final String RE_LITERAL_SUFFIX = ")";
    private static final String RE_MATCH_ONE_OR_MORE = ".*?";
    private static final String RE_MATCH_START = "^";
    private static final String RE_MATCH_END = "$";

    private static final char EXP_MATCH_START = '^';
    private static final char EXP_MATCH_END = '$';
    private static final char EXP_MATCH_ONE_OR_MORE = '*';

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
            return new EverythingMatcher<String>();
        } else {
            return new ExpressionMatcher(compileWildcardExpressionToRegex(expression), getSpecificity(expression));
        }
    }

    private double getSpecificity(String expression) {
        double specificity = 0;
        for (int i = 0; i < expression.length(); i++) {
            final char c = expression.charAt(i);
            if (!(c == '*' || c == '^' || c == '$')) {
                ++specificity;
            }
        }
        return specificity;
    }

    @Nonnull
    private Pattern compileWildcardExpressionToRegex(@Nonnull String expression) {
        final StringBuilder regex = new StringBuilder();

        final int len = expression.length();
        int start = 0;
        int end;

        if (len > 0 && expression.charAt(0) == EXP_MATCH_START) {
            regex.append(RE_MATCH_START);
            start++;
        } else if (leftBoundaryMatching) {
            regex.append(RE_MATCH_START);
        } else {
            regex.append(RE_MATCH_ONE_OR_MORE);
        }

        while ((end = expression.indexOf(EXP_MATCH_ONE_OR_MORE, start)) != -1) {
            regex.append(RE_LITERAL_PREFIX)
                    .append(quote(expression.substring(start, end)))
                    .append(RE_LITERAL_SUFFIX)
                    .append(RE_MATCH_ONE_OR_MORE);
            start = end + 1;
        }

        if (len > 0 && expression.charAt(len - 1) == EXP_MATCH_END) {
            regex.append(RE_LITERAL_PREFIX)
                    .append(quote(expression.substring(start, len - 1)))
                    .append(RE_LITERAL_SUFFIX)
                    .append(RE_MATCH_END);
        } else {
            regex.append(RE_LITERAL_PREFIX)
                    .append(quote(expression.substring(start, len)))
                    .append(RE_LITERAL_SUFFIX)
                    .append(RE_MATCH_ONE_OR_MORE);
        }

        int flags = 0;
        if (!caseSensitivity) {
            flags |= Pattern.CASE_INSENSITIVE;
        }

        return Pattern.compile(regex.toString(), flags);
    }



}
