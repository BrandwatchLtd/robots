package com.brandwatch.robots.domain;

import com.google.common.base.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import static com.google.common.base.Preconditions.checkNotNull;

@Immutable
public final class OtherDirective implements Directive {

    @Nonnull
    private final String field;
    @Nonnull
    private final String value;

    public OtherDirective(@Nonnull String field, @Nonnull String value) {
        this.field = checkNotNull(field, "field is null");
        this.value = checkNotNull(value, "value is null");
    }

    @Override
    @Nonnull
    public String getField() {
        return field;
    }

    @Override
    @Nonnull
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OtherDirective that = (OtherDirective) o;
        return Objects.equal(field, that.field)
                && Objects.equal(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(field, value);
    }

    @Nonnull
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("field", field)
                .add("value", value)
                .toString();
    }
}
