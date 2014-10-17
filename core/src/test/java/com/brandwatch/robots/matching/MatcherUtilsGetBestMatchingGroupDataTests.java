package com.brandwatch.robots.matching;

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
