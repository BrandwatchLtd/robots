package com.brandwatch.robots.domain;

import com.brandwatch.robots.matching.EverythingMatcher;
import com.brandwatch.robots.matching.Matcher;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class AgentDirectiveTest extends AbstractDomainObjectTest<AgentDirective> {

    private static final String PATTERN = "*";
    private static final Matcher<String> MATCHER = new EverythingMatcher<String>();

    @Override
    protected AgentDirective newValidInstance() {
        return new AgentDirective(PATTERN, MATCHER);
    }

    @Test(expected = NullPointerException.class)
    public void givenPatternIsNull_whenNewInstance_thenThrowsNPE() {
        new AgentDirective(null, MATCHER);
    }

    @Test(expected = NullPointerException.class)
    public void givenMatcherIsNull_whenNewInstance_thenThrowsNPE() {
        new AgentDirective(PATTERN, null);
    }

    @Test
    public void givenValidAgentDirective_whenGetValue_thenResultEqualsExpected() {
        AgentDirective agentDirective = new AgentDirective(PATTERN, MATCHER);
        String actualPattern = agentDirective.getValue();
        assertThat(actualPattern, equalTo(PATTERN));
    }

    @Test
    public void givenValidAgentDirective_whenGetMatcher_thenResultEqualsExpected() {
        AgentDirective agentDirective = new AgentDirective(PATTERN, MATCHER);
        Matcher<String> actualMatcher = agentDirective.getMatcher();
        assertThat(actualMatcher, equalTo(MATCHER));
    }

    @Test
    public void givenValidAgentDirective_whenField_thenResultEqualsExpected() {
        AgentDirective agentDirective = new AgentDirective(PATTERN, MATCHER);
        String actualField = agentDirective.getField();
        assertThat(actualField, equalTo("user-agent"));
    }

}
