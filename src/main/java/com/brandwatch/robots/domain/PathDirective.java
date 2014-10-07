package com.brandwatch.robots.domain;

import com.google.common.base.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.net.URI;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;

@Immutable
public final class PathDirective implements Directive {

    @Nonnull
    private final Field field;

    @Nonnull
    private final String value;

    @Nonnull
    private final Pattern pattern;

    public PathDirective(@Nonnull Field field, @Nonnull String value, @Nonnull Pattern pattern) {
        this.pattern = checkNotNull(pattern, "pattern is null");
        this.field = checkNotNull(field, "field is null");
        this.value = checkNotNull(value, "value is null");
    }

    @Nonnull
    @Override
    public String getField() {
        return field.toString();
    }

    @Nonnull
    @Override
    public String getValue() {
        return value;
    }

    public boolean matches(@Nonnull URI uri) {
        return pattern.matcher(uri.getPath()).matches();
    }

    public boolean isAllowed() {
        return field.isAllowed();
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PathDirective that = (PathDirective) o;
        return field == that.field && value.equals(that.value);
    }

    @Override
    public int hashCode() {
        int result = field.hashCode();
        result = 31 * result + value.hashCode();
        return result;
    }

    @Nonnull
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("field", field)
                .add("value", value)
                .toString();
    }

    public enum Field {
        allow {
            @Override
            public boolean isAllowed() {
                return true;
            }
        },
        disallow {
            @Override
            public boolean isAllowed() {
                return false;
            }
        };

        public abstract boolean isAllowed();
    }
}
