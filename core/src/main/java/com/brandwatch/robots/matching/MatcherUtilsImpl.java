package com.brandwatch.robots.matching;

import com.google.common.base.Optional;

import javax.annotation.Nonnull;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.of;
import static com.google.common.base.Preconditions.checkNotNull;

public class MatcherUtilsImpl implements MatcherUtils {

    @Override
    @Nonnull
    public <T, M extends Matchable<T>, Group extends Iterable<M>>
    Optional<Group> getMostSpecificMatchingGroup(@Nonnull Iterable<Group> groups, @Nonnull T target) {
        checkNotNull(groups, "groups is null");
        checkNotNull(target, "target is null");

        Optional<Group> argMax = absent();
        Optional<Double> maxSpecificity = absent();

        for (Group group : groups) {
            Optional<Double> specificity = getMostSpecificMatchSpecificity(group, target);
            if (specificity.isPresent() && (!maxSpecificity.isPresent() || specificity.get() > maxSpecificity.get())) {
                maxSpecificity = specificity;
                argMax = of(group);
            }
        }
        return argMax;
    }

    @Override
    public <T, M extends Matchable<T>>
    Optional<Double> getMostSpecificMatchSpecificity(@Nonnull Iterable<M> matchables, @Nonnull T target) {
        checkNotNull(matchables, "matchables is null");
        checkNotNull(target, "target is null");

        Optional<Double> maxSpecificity = absent();
        for (M matchable : matchables) {
            Optional<Double> specificity = getMatchSpecificity(matchable.getMatcher(), target);
            if (specificity.isPresent() && (!maxSpecificity.isPresent() || specificity.get() > maxSpecificity.get())) {
                maxSpecificity = specificity;
            }
        }
        return maxSpecificity;
    }

    @Override
    public <T, M extends Matchable<T>>
    Optional<M> getMostSpecificMatch(@Nonnull Iterable<M> matchables, @Nonnull T target) {
        checkNotNull(matchables, "matchables is null");
        checkNotNull(target, "target is null");

        double maxSpecificity = -1;
        Optional<M> argMax = absent();
        for (M matchable : matchables) {
            Optional<Double> specificity = getMatchSpecificity(matchable.getMatcher(), target);
            if (specificity.isPresent() && specificity.get() > maxSpecificity) {
                maxSpecificity = specificity.get();
                argMax = of(matchable);
            }
        }
        return argMax;
    }

    @Override
    public <T> Optional<Double> getMatchSpecificity(@Nonnull Matcher<T> matcher, @Nonnull T target) {
        checkNotNull(matcher, "matcher is null");
        checkNotNull(target, "target is null");

        if (matcher.matches(target)) {
            return of(matcher.getSpecificity());
        } else {
            return absent();
        }
    }

}
