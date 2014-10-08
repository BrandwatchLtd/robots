package com.brandwatch.robots.domain;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Immutable
public final class Group {

    @Nonnull
    private final List<Directive> directives;

    public Group(@Nonnull Builder builder) {
        this.directives = builder.directives.build();
    }

    @Nonnull
    public List<Directive> getDirectives() {
        return directives;
    }

    @Nonnull
    public <T extends Directive> List<T> getDirectives(@Nonnull Class<T> directiveType) {
        ImmutableList.Builder<T> result = ImmutableList.builder();
        for (Directive directive : directives) {
            if (directiveType.isAssignableFrom(directive.getClass())) {
                result.add(directiveType.cast(directive));
            }
        }
        return result.build();
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return directives.equals(group.directives);
    }

    @Override
    public int hashCode() {
        return directives.hashCode();
    }

    @Nonnull
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("directives", directives)
                .toString();
    }

    public static class Builder {

        @Nonnull
        private final ImmutableList.Builder<Directive> directives = ImmutableList.builder();

        @Nonnull
        public Builder withDirective(@Nonnull Directive directive) {
            directives.add(checkNotNull(directive, "directive is null"));
            return this;
        }

        @Nonnull
        public Group build() {
            return new Group(this);
        }
    }

}
