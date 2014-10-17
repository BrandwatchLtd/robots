package com.brandwatch.robots.matching;

import com.google.common.base.Optional;

import javax.annotation.Nonnull;

public interface MatcherUtils {

    @Nonnull
    <T, M extends Matchable<T>, Group extends Iterable<M>>
    Optional<Group> getMostSpecificMatchingGroup(@Nonnull Iterable<Group> groups, @Nonnull T target);

    @Nonnull
    <T, M extends Matchable<T>>
    Optional<Double> getMostSpecificMatchSpecificity(@Nonnull Iterable<M> matchables, @Nonnull T target);

    @Nonnull
    <T, M extends Matchable<T>>
    Optional<M> getMostSpecificMatch(@Nonnull Iterable<M> matchables, @Nonnull T target);

    @Nonnull
    <T>
    Optional<Double> getMatchSpecificity(@Nonnull Matcher<T> matcher, @Nonnull T target);

}
