package com.brandwatch.robotstxt.domain;

import com.google.common.base.Objects;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;

import static com.google.common.base.Preconditions.checkNotNull;

@Immutable
public final class SiteMapDirective implements Directive {

    @Nonnull
    private final String value;

    public SiteMapDirective(@Nonnull String value) {
        this.value = checkNotNull(value, "value is null");
    }

    @Override
    @Nonnull
    public String getField() {
        return "sitemap";
    }

    @Override
    @Nonnull
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SiteMapDirective that = (SiteMapDirective) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("value", value)
                .toString();
    }
}
