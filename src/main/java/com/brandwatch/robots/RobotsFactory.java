package com.brandwatch.robots;

import com.brandwatch.robots.domain.AgentDirective;
import com.brandwatch.robots.domain.Group;
import com.brandwatch.robots.domain.PathDirective;
import com.brandwatch.robots.domain.Robots;
import com.brandwatch.robots.util.Matchers;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

public class RobotsFactory {

    @Nonnull
    public Robots createAllowAllRobots() {
        return createConstantRobots(PathDirective.Field.allow);
    }

    @Nonnull
    public Robots createDisallowAllRobots() {
        return createConstantRobots(PathDirective.Field.disallow);
    }

    @Nonnull
    private Robots createConstantRobots(@Nonnull PathDirective.Field permission) {
        checkNotNull(permission, "permission");
        return new Robots.Builder()
                .withGroup(new Group.Builder()
                        .withDirective(new AgentDirective("*", Matchers.<String>all()))
                        .withDirective(new PathDirective(permission, "/*", Matchers.<String>all()))
                        .build())
                .build();
    }

    @Nonnull
    private Robots createEmptyRobots() {
        return new Robots.Builder().build();
    }
}
