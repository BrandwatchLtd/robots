package com.brandwatch.robots;

import com.google.common.base.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

class MissingPrefixFixingFunction implements Function<String, String> {

    @Nullable
    @Override
    public String apply(@Nonnull String input) {
        checkNotNull(input, "input is null");
        if (!input.isEmpty()) {
            final char c = input.charAt(0);
            if (c != '/' && c != '*' && c != '^') {
                return '/' + input;
            }
        }
        return input;
    }
}
