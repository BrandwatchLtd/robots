package com.brandwatch.robots.domain;

import com.brandwatch.robots.matching.Matchable;
import com.brandwatch.robots.matching.Matcher;
import com.google.common.base.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import static com.google.common.base.Preconditions.checkNotNull;

@Immutable
public final class AgentDirective implements Directive, Matchable<String> {

    @Nonnull
    private final String agentPattern;

    @Nonnull
    private final Matcher<String> agentMatcher;

    public AgentDirective(@Nonnull String agentPattern, @Nonnull Matcher<String> agentMatcher) {
        this.agentMatcher = checkNotNull(agentMatcher, "agentMatcher is null");
        this.agentPattern = checkNotNull(agentPattern, "agentPattern is null");
    }

    @Nonnull
    @Override
    public String getField() {
        return "user-agent";
    }

    @Nonnull
    @Override
    public String getValue() {
        return agentPattern;
    }

    @Override
    public Matcher<String> getMatcher() {
        return agentMatcher;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgentDirective that = (AgentDirective) o;
        return Objects.equal(agentPattern, that.agentPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(agentPattern);
    }

    @Nonnull
    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("agentPattern", agentPattern)
                .toString();
    }

}
