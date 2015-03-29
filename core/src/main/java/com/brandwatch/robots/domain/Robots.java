package com.brandwatch.robots.domain;

/*
 * #%L
 * Robots (core)
 * %%
 * Copyright (C) 2015 Brandwatch
 * %%
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the Brandwatch nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Robots robots = (Robots) o;
        return Objects.equal(groups, robots.groups)
                && Objects.equal(nonGroupDirectives, robots.nonGroupDirectives);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(groups, nonGroupDirectives);
    }

    @Nonnull
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
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
