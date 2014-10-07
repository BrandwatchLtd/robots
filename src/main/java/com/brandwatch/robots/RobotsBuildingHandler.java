package com.brandwatch.robots;

import com.brandwatch.robots.domain.*;
import com.brandwatch.robots.parser.RobotsTxtParserHandler;
import com.google.common.base.Optional;
import com.google.common.base.Supplier;

import javax.annotation.Nonnull;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;

class RobotsBuildingHandler implements RobotsTxtParserHandler, Supplier<Robots> {

    private final RobotsUtilities utilities;

    @Nonnull
    private final Robots.Builder robots = new Robots.Builder();
    @Nonnull
    private Optional<Group.Builder> group = Optional.absent();

    RobotsBuildingHandler(RobotsUtilities utilities) {
        this.utilities = checkNotNull(utilities, "utilities");
    }

    @Override
    public void startEntry() {
        group = Optional.of(new Group.Builder());
    }

    @Override
    public void userAgent(@Nonnull String pattern) {
        group.get().withUserAgent(pattern);
    }

    @Override
    public void allow(@Nonnull String pattern) {
        addNewPathDirective(PathDirective.Field.allow, pattern);
    }

    @Override
    public void disallow(@Nonnull String pattern) {
        addNewPathDirective(PathDirective.Field.disallow, pattern);
    }

    private void addNewPathDirective(@Nonnull PathDirective.Field field, @Nonnull String pathExpression) {
        Pattern pattern = utilities.compilePathExpression(pathExpression);
        group.get().withDirective(new PathDirective(field, pathExpression, pattern));
    }

    @Override
    public void endEntry() {
        robots.withGroup(group.get().build());
        group = Optional.absent();
    }

    @Override
    public void siteMap(@Nonnull String url) {
        robots.withNonGroupDirective(new SiteMapDirective(url));
    }

    @Override
    public void otherDirective(@Nonnull String field, @Nonnull String value) {
        robots.withNonGroupDirective(new OtherDirective(field, value));
    }

    @Override
    @Nonnull
    public Robots get() {
        return robots.build();
    }
}
