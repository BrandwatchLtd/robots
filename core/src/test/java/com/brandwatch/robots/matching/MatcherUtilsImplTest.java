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

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class MatcherUtilsImplTest {

    private static ExpressionCompiler agentExpressionCompiler = new ExpressionCompilerBuilder()
            .withCaseSensitivity(false)
            .build();

    protected MatcherUtilsImpl utilities;

    private static Matcher<String> newPatternMatcher(String pattern) {
        return agentExpressionCompiler.compile(pattern);
    }

    @Before
    public final void setup() {
        utilities = new MatcherUtilsImpl();
    }

    @Test(expected = NullPointerException.class)
    public void givenMatcherIsNull_whenGetMatchSpecificity_thenThrowsNPE() {
        Matcher<String> matcher = null;
        String agent = "googlebot";
        utilities.getMatchSpecificity(matcher, agent);
    }

    @Test(expected = NullPointerException.class)
    public void givenAgentIsNull_whenGetMatchSpecificity_thenThrowsNPE() {
        Matcher<String> matcher = newPatternMatcher("*");
        String agent = null;
        utilities.getMatchSpecificity(matcher, agent);
    }

    @Test
    public void givenPatternIsWildcard_whenGetMatchSpecificity_thenReturnsZero() {
        Matcher<String> matcher = newPatternMatcher("*");
        String agent = "googlebot";
        Optional<Double> specificity = utilities.getMatchSpecificity(matcher, agent);
        assertThat(specificity.get(), equalTo(0.0));
    }

    @Test
    public void givenPatternMatches_whenGetMatchSpecificity_thenReturnsMatcherSpecificity() {
        Matcher<String> matcher = newPatternMatcher("google");
        String agent = "googlebot";
        Optional<Double> specificity = utilities.getMatchSpecificity(matcher, agent);
        assertThat(specificity.get(), equalTo(matcher.getSpecificity()));
    }

    @Test
    public void givenPatternMatchesIgnoringCase_whenGetMatchSpecificity_thenReturnsMatcherSpecificity() {
        Matcher<String> matcher = newPatternMatcher("gOoGlE");
        String agent = "googlebot";
        Optional<Double> specificity = utilities.getMatchSpecificity(matcher, agent);
        assertThat(specificity.get(), equalTo(matcher.getSpecificity()));
    }

    @Test
    public void givenPatternNotMatches_whenGetMatchSpecificity_thenReturnsAbsent() {
        Matcher<String> matcher = newPatternMatcher("yahoo");
        String agent = "googlebot";
        Optional<Double> specificity = utilities.getMatchSpecificity(matcher, agent);
        assertThat(specificity, equalTo(Optional.<Double>absent()));
    }

    @Test
    public void givenPatternIsEmpty_whenGetMatchSpecificity_thenReturnsZero() {
        Matcher<String> matcher = newPatternMatcher("");
        String agent = "googlebot";
        Optional<Double> specificity = utilities.getMatchSpecificity(matcher, agent);
        assertThat(specificity.get(), equalTo(0.0));
    }

    @Test
    public void givenPatternAndAgentAreEmpty_whenGetMatchSpecificity_thenReturnsZero() {
        Matcher<String> matcher = newPatternMatcher("");
        String agent = "";
        Optional<Double> specificity = utilities.getMatchSpecificity(matcher, agent);
        assertThat(specificity.get(), equalTo(0.0));
    }

    @Test
    public void givenWildcardPattern_andAgentIsEmpty_whenGetMatchSpecificity_thenReturnsZero() {
        Matcher<String> matcher = newPatternMatcher("*");
        String agent = "";
        Optional<Double> specificity = utilities.getMatchSpecificity(matcher, agent);
        assertThat(specificity.get(), equalTo(0.0));
    }

    @Test(expected = NullPointerException.class)
    public void givenGroupIsNull_whenGetMostSpecificMatchSpecificity_thenThrowsNPE() {
        List<Matchable<String>> group = null;
        String agent = "googlebot";
        utilities.getMostSpecificMatchSpecificity(group, agent);
    }

    @Test(expected = NullPointerException.class)
    public void givenAgentIsNull_whenGetMostSpecificMatchSpecificity_thenThrowsNPE() {
        List<Matchable<String>> group = ImmutableList.of();
        String agent = null;
        utilities.getMostSpecificMatchSpecificity(group, agent);
    }

    @Test
    public void givenGroupIsEmpty_whenGetMostSpecificMatchSpecificity_thenReturnsAbsent() {
        List<Matchable<String>> group = ImmutableList.of();
        String agent = "googlebot";
        Optional<Double> longestMatch = utilities.getMostSpecificMatchSpecificity(group, agent);
        assertThat(longestMatch, equalTo(Optional.<Double>absent()));
    }

    @Test
    public void givenSingltonWildcardGroup_whenGetMostSpecificMatchSpecificity_thenReturnsZero() {
        List<Matchable<String>> group = ImmutableList
                .<Matchable<String>>builder()
                .add(new Matchable<String>() {
                    @Override
                    public Matcher<String> getMatcher() {
                        return newPatternMatcher("*");
                    }
                })
                .build();
        String agent = "googlebot";
        Optional<Double> longestMatch = utilities.getMostSpecificMatchSpecificity(group, agent);
        assertThat(longestMatch.get(), equalTo(0.0));
    }


    @Test(expected = NullPointerException.class)
    public void givenGroupsIsNull_whenGetMostSpecificMatchingGroup_thenThrowsNPE() {
        List<List<Matchable<String>>> groups = null;
        String agentString = "googlebot";
        utilities.getMostSpecificMatchingGroup(groups, agentString);
    }

    @Test(expected = NullPointerException.class)
    public void givenAgentIsNull_whenGetMostSpecificMatchingGroup_thenThrowsNPE() {
        List<List<Matchable<String>>> groups = Collections.emptyList();
        String agentString = null;
        utilities.getMostSpecificMatchingGroup(groups, agentString);
    }

    @Test
    public void givenGroupsAreEmpty_whenGetMostSpecificMatchingGroup_thenReturnsAbsent() {
        List<List<Matchable<String>>> groups = Collections.emptyList();
        String agentString = "googlebot";
        Optional<List<Matchable<String>>> result = utilities.getMostSpecificMatchingGroup(groups, agentString);
        assertThat(result, equalTo(Optional.<List<Matchable<String>>>absent()));
    }

}
