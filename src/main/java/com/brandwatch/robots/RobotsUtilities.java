package com.brandwatch.robots;

import com.brandwatch.robots.domain.AgentDirective;
import com.brandwatch.robots.domain.Group;
import com.brandwatch.robots.net.RobotsURIBuilder;
import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.io.CharSource;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class RobotsUtilities {

    @Nonnull
    public CharSource createCharSourceFor(final URI robotsResource) {
        checkNotNull(robotsResource, "robotsResource");
        return new CharSource() {
            @Override
            public Reader openStream() throws IOException {
                return new InputStreamReader(
                        robotsResource.toURL().openStream(),
                        Charsets.UTF_8);
            }
        };
    }

    @Nonnull
    public URI getRobotsURIForResource(@Nonnull final URI resourceUri) {
        checkNotNull(resourceUri, "resourceUri");
        return new RobotsURIBuilder()
                .fromUri(resourceUri)
                .build();
    }

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
        for (AgentDirective agentPattern : group.getDirectives(AgentDirective.class)) {
            int length = getAgentMatchLength(agentPattern, agentString);
            if (longestMatch < length) {
                longestMatch = length;
            }
        }
        return longestMatch;
    }


    public int getAgentMatchLength(@Nonnull AgentDirective agentDirective, @Nonnull String agentString) {
        checkNotNull(agentDirective, "agentPattern is null");
        checkNotNull(agentString, "agentString is null");

        if (agentDirective.getValue().isEmpty() || agentDirective.getValue().equals("*")) {
            return 0;
        } else if (agentDirective.matches(agentString.toLowerCase())) {
            return agentDirective.getValue().length();
        } else {
            return -1;
        }
    }

}
