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

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.Iterator;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Immutable
public final class Group implements Iterable<AgentDirective> {

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
        return Objects.equal(directives, group.directives);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(directives);
    }

    @Nonnull
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("directives", directives)
                .toString();
    }

    @Override
    public Iterator<AgentDirective> iterator() {
        List<AgentDirective> agentDirectives = getDirectives(AgentDirective.class);
        return agentDirectives.iterator();
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
