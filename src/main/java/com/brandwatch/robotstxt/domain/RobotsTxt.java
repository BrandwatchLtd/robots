package com.brandwatch.robotstxt.domain;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Immutable
public final class RobotsTxt {

    @Nonnull
    private final ImmutableList<Group> groups;
    @Nonnull
    private final ImmutableList<Directive> nonGroupDirectives;

    private RobotsTxt(@Nonnull Builder builder) {
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
        RobotsTxt robotsTxt = (RobotsTxt) o;
        return groups.equals(robotsTxt.groups) && nonGroupDirectives.equals(robotsTxt.nonGroupDirectives);
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

        public void withGroup(@Nonnull Group group) {
            groups.add(checkNotNull(group, "group is null"));
        }

        public void withNonGroupDirective(@Nonnull Directive directive) {
            nonGroupDirectives.add(checkNotNull(directive, "directive is null"));
        }

        @Nonnull
        public RobotsTxt build() {
            return new RobotsTxt(this);
        }

    }
}
