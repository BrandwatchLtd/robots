package com.brandwatch.robots.matching;

/*
 * #%L
 * Robots (core)
 * %%
 * Copyright (C) 2015 Brandwatch
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
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.annotation.Nonnull;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class MatcherUtilsGetBestMatchingGroupDataTests {


    private static ExpressionCompiler agentExpressionCompiler = new ExpressionCompilerBuilder()
            .withCaseSensitivity(false)
            .build();

    protected MatcherUtilsImpl utilities;

    @Before
    public final void setup() {
        utilities = new MatcherUtilsImpl();
    }

    @Nonnull
    private static final Group firstGroup = new Group.Builder()
            .withDirective(new AgentDirective("googlebot-news", agentExpressionCompiler.compile("googlebot-news")))
            .build();
    @Nonnull
    private static final Group secondGroup = new Group.Builder()
            .withDirective(new AgentDirective("*", agentExpressionCompiler.compile("*")))
            .build();
    @Nonnull
    private static final Group thirdGroup = new Group.Builder()
            .withDirective(new AgentDirective("googlebot", agentExpressionCompiler.compile("googlebot")))
            .build();
    private static final List<Group> groups = ImmutableList.<Group>builder()
            .add(firstGroup).add(secondGroup).add(thirdGroup)
            .build();


    private final String crawlerName;
    private final Group expectedGroup;

    public MatcherUtilsGetBestMatchingGroupDataTests(String crawlerName, Group expectedGroup) {
        this.crawlerName = crawlerName;
        this.expectedGroup = expectedGroup;
    }

    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return ImmutableList.<Object[]>builder()
                .add(new Object[]{"Googlebot", thirdGroup})
                .add(new Object[]{"Googlebot-News", firstGroup})
                .add(new Object[]{"Googlebot-web", thirdGroup})
                .add(new Object[]{"Googlebot-Images", thirdGroup})
                .add(new Object[]{"Googlebot-News", firstGroup})
                .add(new Object[]{"Otherbot-News", secondGroup})
                .add(new Object[]{"Otherbot-Web", secondGroup})
                .build();
    }

    @Test
    public void whenGetBestMatchingGroup_thenResultEqualsExpected() {
        Optional<Group> bestMatch = utilities.getMostSpecificMatchingGroup(groups, crawlerName);
        assertThat(bestMatch.get(), equalTo(expectedGroup));
    }

    @Test
    public void givenUpperCaseCrawlerName_whenGetBestMatchingGroup_thenResultEqualsExpected() {
        Optional<Group> bestMatch = utilities.getMostSpecificMatchingGroup(groups, crawlerName.toUpperCase());
        assertThat(bestMatch.get(), equalTo(expectedGroup));
    }

    @Test
    public void givenLowerCaseCrawlerName_whenGetBestMatchingGroup_thenResultEqualsExpected() {
        Optional<Group> bestMatch = utilities.getMostSpecificMatchingGroup(groups, crawlerName.toLowerCase());
        assertThat(bestMatch.get(), equalTo(expectedGroup));
    }

}
