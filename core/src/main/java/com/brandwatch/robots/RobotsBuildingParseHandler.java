package com.brandwatch.robots;

/*
 * #%L
 * Robots (core)
 * %%
 * Copyright (C) 2014 - 2015 Brandwatch
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

import com.brandwatch.robots.domain.AgentDirective;
import com.brandwatch.robots.domain.Group;
import com.brandwatch.robots.domain.OtherDirective;
import com.brandwatch.robots.domain.PathDirective;
import com.brandwatch.robots.domain.Robots;
import com.brandwatch.robots.domain.SiteMapDirective;
import com.brandwatch.robots.matching.ExpressionCompiler;
import com.brandwatch.robots.parser.RobotsParseHandler;
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
        if (!pathExpression.trim().isEmpty()) {
            group.get().withDirective(new PathDirective(field, pathExpression,
                    pathExpressionCompiler.compile(pathExpression)));
        }
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
