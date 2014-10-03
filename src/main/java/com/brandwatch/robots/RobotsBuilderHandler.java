package com.brandwatch.robots;

import com.brandwatch.robots.domain.*;
import com.brandwatch.robots.parser.RobotsTxtParserHandler;
import com.google.common.base.Optional;
import com.google.common.base.Supplier;

import javax.annotation.Nonnull;

class RobotsBuilderHandler implements RobotsTxtParserHandler, Supplier<Robots> {

    @Nonnull
    private final Robots.Builder robots = new Robots.Builder();
    @Nonnull
    private Optional<Group.Builder> group = Optional.absent();

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
        group.get().withDirective(new PathDirective(PathDirective.Field.allow, pattern));
    }

    @Override
    public void disallow(@Nonnull String pattern) {
        group.get().withDirective(new PathDirective(PathDirective.Field.disallow, pattern));
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
