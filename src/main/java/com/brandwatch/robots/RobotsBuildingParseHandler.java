package com.brandwatch.robots;

import com.brandwatch.robots.domain.*;
import com.brandwatch.robots.parser.RobotsParseHandler;
import com.brandwatch.robots.util.ExpressionCompiler;
import com.google.common.base.Optional;
import com.google.common.base.Supplier;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

class RobotsBuildingParseHandler implements RobotsParseHandler, Supplier<Robots> {

    private final ExpressionCompiler pathExpressionCompiler;
    private final ExpressionCompiler agentExpressionCompiler;

    @Nonnull
    private final Robots.Builder robots = new Robots.Builder();
    @Nonnull
    private Optional<Group.Builder> group = Optional.absent();

    RobotsBuildingParseHandler(@Nonnull ExpressionCompiler pathExpressionCompiler,
                               @Nonnull ExpressionCompiler agentExpressionCompiler) {
        this.pathExpressionCompiler = checkNotNull(pathExpressionCompiler, "pathExpressionCompiler");
        this.agentExpressionCompiler = checkNotNull(agentExpressionCompiler, "agentExpressionCompiler");
    }

    @Override
    public void startEntry() {
        group = Optional.of(new Group.Builder());
    }

    @Override
    public void userAgent(@Nonnull String agentExpression) {
        group.get().withDirective(new AgentDirective(agentExpression,
                agentExpressionCompiler.compile(agentExpression)));
    }

    @Override
    public void allow(@Nonnull String pattern) {
        addNewPathDirective(PathDirective.Field.allow, pattern);
    }

    @Override
    public void disallow(@Nonnull String pattern) {
        addNewPathDirective(PathDirective.Field.disallow, pattern);
    }

    private void addNewPathDirective(@Nonnull PathDirective.Field field,
                                     @Nonnull final String pathExpression) {
        group.get().withDirective(new PathDirective(field, pathExpression,
                pathExpressionCompiler.compile(pathExpression)));
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
