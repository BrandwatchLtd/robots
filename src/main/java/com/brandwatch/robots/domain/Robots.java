package com.brandwatch.robots.domain;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Immutable
public final class Robots {

    @Nonnull
    private final ImmutableList<Group> groups;
    @Nonnull
    private final ImmutableList<Directive> nonGroupDirectives;

    private Robots(@Nonnull Builder builder) {
        groups = builder.groups.build();
        nonGroupDirectives = builder.nonGroupDirectives.build();
    }

    @Nonnull
    public List<Group> getGroups() {
        return groups;
    }

    @Nonnull
    public List<Directive> getNonGroupDirectives() {
        return nonGroupDirectives;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Robots robots = (Robots) o;
        return groups.equals(robots.groups) && nonGroupDirectives.equals(robots.nonGroupDirectives);
    }

    @Override
    public int hashCode() {
        int result = groups.hashCode();
        result = 31 * result + nonGroupDirectives.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("groups", groups)
                .add("nonGroupDirectives", nonGroupDirectives)
                .toString();
    }

    public static class Builder {

        @Nonnull
        private final ImmutableList.Builder<Group> groups = ImmutableList.builder();
        @Nonnull
        private final ImmutableList.Builder<Directive> nonGroupDirectives = ImmutableList.builder();

        @Nonnull
        public Builder withGroup(@Nonnull Group group) {
            groups.add(checkNotNull(group, "group is null"));
            return this;
        }

        @Nonnull
        public Builder withNonGroupDirective(@Nonnull Directive directive) {
            nonGroupDirectives.add(checkNotNull(directive, "directive is null"));
            return this;
        }

        @Nonnull
        public Robots build() {
            return new Robots(this);
        }

    }
}
