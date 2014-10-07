package com.brandwatch.robots.domain;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Immutable
public final class Group {

    @Nonnull
    private final List<String> userAgents;

    @Nonnull
    private final List<Directive> directives;

    public Group(@Nonnull Builder builder) {
        this.userAgents = builder.userAgents.build();
        this.directives = builder.directives.build();
    }

    @Nonnull
    public List<String> getUserAgents() {
        return userAgents;
    }

    @Nonnull
    public List<Directive> getDirectives() {
        return directives;
    }

    @Nonnull
    public <T extends Directive> List<T> getDirectives(Class<T> directiveType) {
        ImmutableList.Builder<T> result = ImmutableList.builder();
        for (Directive directive : directives) {
            if (directiveType.isAssignableFrom(directive.getClass())) {
                result.add((T) directive);
            }
        }
        return result.build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return directives.equals(group.directives)
                && userAgents.equals(group.userAgents);
    }

    @Override
    public int hashCode() {
        int result = userAgents.hashCode();
        result = 31 * result + directives.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("userAgents", userAgents)
                .add("directives", directives)
                .toString();
    }

    public static class Builder {

        @Nonnull
        private final ImmutableList.Builder<String> userAgents = ImmutableList.builder();

        @Nonnull
        private final ImmutableList.Builder<Directive> directives = ImmutableList.builder();

        @Nonnull
        public Builder withUserAgent(@Nonnull String userAgent) {
            userAgents.add(checkNotNull(userAgent, "userAgent is null"));
            return this;
        }

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
