package com.brandwatch.robots;

import com.brandwatch.robots.domain.Group;
import com.google.common.base.Optional;

import javax.annotation.Nonnull;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class RobotsUtilities {

    @Nonnull
    public Optional<Group> getBestMatchingGroup(@Nonnull List<Group> groups, @Nonnull String agentString) {
        checkNotNull(groups, "groups is null");
        checkNotNull(agentString, "agentString is null");
        checkArgument(!groups.isEmpty(), "groups is empty");

        Optional<Group> bestGroup = Optional.absent();
        int longestMatch = -1;

        for (Group group : groups) {
            int matchLength = getLongestAgentMatchLength(group, agentString);
            if (longestMatch < matchLength) {
                longestMatch = matchLength;
                bestGroup = Optional.of(group);
            }
        }

        return bestGroup;
    }


    public int getLongestAgentMatchLength(@Nonnull Group group, @Nonnull String agentString) {
        checkNotNull(group, "group is null");
        checkNotNull(agentString, "agentString is null");

        int longestMatch = -1;
        for (String agentPattern : group.getUserAgents()) {
            int length = getAgentMatchLength(agentPattern, agentString);
            if (longestMatch < length) {
                longestMatch = length;
            }
        }
        return longestMatch;
    }


    public int getAgentMatchLength(@Nonnull String agentPattern, @Nonnull String agentString) {
        checkNotNull(agentPattern, "agentPattern is null");
        checkNotNull(agentString, "agentString is null");

        if (agentPattern.isEmpty() || agentPattern.equals("*")) {
            return 0;
        } else if (agentString.toLowerCase().contains(agentPattern.toLowerCase())) {
            return agentPattern.length();
        } else {
            return -1;
        }
    }
}
