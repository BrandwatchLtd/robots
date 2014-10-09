package com.brandwatch.robots;

import com.brandwatch.robots.domain.AgentDirective;
import com.brandwatch.robots.domain.Group;
import com.brandwatch.robots.net.SizeLimitExceededException;
import com.brandwatch.robots.util.ExpressionCompiler;
import com.brandwatch.robots.util.ExpressionCompilerBuilder;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.io.CharSource;
import com.google.common.io.Resources;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class RobotsUtilitiesTest {

    private static ExpressionCompiler agentExpressionCompiler = new ExpressionCompilerBuilder()
            .withCaseSensitivity(false)
            .build();

    public static class GetAgentMatchLengthTest {

        private RobotsUtilities utilities;


        @Before
        public final void setup() {
            utilities = new RobotsUtilities();
        }

        @Test(expected = NullPointerException.class)
        public void givenPatternIsNull_whenGetAgentMatchLength_thenThrowsNPE() {
            AgentDirective directive = null;
            String agent = "googlebot";
            utilities.getAgentMatchLength(directive, agent);
        }

        @Test(expected = NullPointerException.class)
        public void givenAgentIsNull_whenGetAgentMatchLength_thenThrowsNPE() {
            AgentDirective directive = newAgentDirective("*");
            String agent = null;
            utilities.getAgentMatchLength(directive, agent);
        }

        @Test
        public void givenPatternIsWildcard_whenGetAgentMatchLength_thenReturnsZero() {
            AgentDirective directive = newAgentDirective("*");
            String agent = "googlebot";
            int length = utilities.getAgentMatchLength(directive, agent);
            assertThat(length, equalTo(0));
        }

        @Test
        public void givenPatternMatches_whenGetAgentMatchLength_thenReturnsPatternLength() {
            AgentDirective directive = newAgentDirective("google");
            String agent = "googlebot";
            int length = utilities.getAgentMatchLength(directive, agent);
            assertThat(length, equalTo(directive.getValue().length()));
        }

        @Test
        public void givenPatternMatchesIgnoringCase_whenGetAgentMatchLength_thenReturnsPatternLength() {
            AgentDirective directive = newAgentDirective("gOoGlE");
            String agent = "googlebot";
            int length = utilities.getAgentMatchLength(directive, agent);
            assertThat(length, equalTo(directive.getValue().length()));
        }

        @Test
        public void givenPatternNotMatches_whenGetAgentMatchLength_thenReturnsMinusOne() {
            AgentDirective directive = newAgentDirective("yahoo");
            String agent = "googlebot";
            int length = utilities.getAgentMatchLength(directive, agent);
            assertThat(length, equalTo(-1));
        }

        @Test
        public void givenPatternIsEmpty_whenGetAgentMatchLength_thenReturnsZero() {
            AgentDirective directive = newAgentDirective("");
            String agent = "googlebot";
            int length = utilities.getAgentMatchLength(directive, agent);
            assertThat(length, equalTo(0));
        }

        @Test
        public void givenPatternAndAgentAreEmpty_whenGetAgentMatchLength_thenReturnsZero() {
            AgentDirective directive = newAgentDirective("");
            String agent = "";
            int length = utilities.getAgentMatchLength(directive, agent);
            assertThat(length, equalTo(0));
        }

        @Test
        public void givenWildcardPattern_andAgentIsEmpty_whenGetAgentMatchLength_thenReturnsZero() {
            AgentDirective directive = newAgentDirective("*");
            String agent = "";
            int length = utilities.getAgentMatchLength(directive, agent);
            assertThat(length, equalTo(0));
        }

        private AgentDirective newAgentDirective(String pattern) {
            return new AgentDirective(pattern, agentExpressionCompiler.compile(pattern));
        }
    }

    public static class GetLongestAgentMatchTest {


        private RobotsUtilities utilities;

        @Before
        public final void setup() {
            utilities = new RobotsUtilities();
        }

        @Test(expected = NullPointerException.class)
        public void givenGroupIsNull_whenGetLongestAgentMatch_thenThrowsNPE() {
            Group group = null;
            String agent = "googlebot";
            utilities.getLongestAgentMatchLength(group, agent);
        }

        @Test(expected = NullPointerException.class)
        public void givenAgentIsNull_whenGetLongestAgentMatch_thenThrowsNPE() {
            Group group = new Group.Builder().build();
            String agent = null;
            utilities.getLongestAgentMatchLength(group, agent);
        }

        @Test
        public void givenGroupIsEmpty_whenGetLongestAgentMatch_thenReturnsMinusOne() {
            Group group = new Group.Builder().build();
            String agent = "googlebot";
            int longestMatch = utilities.getLongestAgentMatchLength(group, agent);
            assertThat(longestMatch, equalTo(-1));
        }

        @Test
        public void givenSingltonWildcardGroup_whenGetLongestAgentMatch_thenReturnsZero() {
            Group group = new Group.Builder().withDirective(
                    new AgentDirective("*", agentExpressionCompiler.compile("*"))
            ).build();
            String agent = "googlebot";
            int longestMatch = utilities.getLongestAgentMatchLength(group, agent);
            assertThat(longestMatch, equalTo(0));
        }

    }

    public static class GetBestMatchingGroupTest {

        private RobotsUtilities utilities;

        @Before
        public final void setup() {
            utilities = new RobotsUtilities();
        }

        @Test(expected = NullPointerException.class)
        public void givenGroupsIsNull_whenGetBestMatchingGroup_thenThrowsNPE() {
            List<Group> groups = null;
            String agentString = "googlebot";
            utilities.getBestMatchingGroup(groups, agentString);
        }

        @Test(expected = NullPointerException.class)
        public void givenAgentIsNull_whenGetBestMatchingGroup_thenThrowsNPE() {
            List<Group> groups = Collections.emptyList();
            String agentString = null;
            utilities.getBestMatchingGroup(groups, agentString);
        }

        @Test(expected = IllegalArgumentException.class)
        public void givenGroupsAreEmpty_whenGetBestMatchingGroup_thenThrowsIAE() {
            List<Group> groups = Collections.emptyList();
            String agentString = "googlebot";
            utilities.getBestMatchingGroup(groups, agentString);
        }

    }

    @RunWith(Parameterized.class)
    public static class GetBestMatchingGroupDataTests {

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
        private RobotsUtilities utilities;

        public GetBestMatchingGroupDataTests(String crawlerName, Group expectedGroup) {
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

        @Before
        public final void setup() {
            utilities = new RobotsUtilities();
        }

        @Test
        public void whenGetBestMatchingGroup_thenResultEqualsExpected() {
            Optional<Group> bestMatch = utilities.getBestMatchingGroup(groups, crawlerName);
            assertThat(bestMatch.get(), equalTo(expectedGroup));
        }

        @Test
        public void givenUpperCaseCrawlerName_whenGetBestMatchingGroup_thenResultEqualsExpected() {
            Optional<Group> bestMatch = utilities.getBestMatchingGroup(groups, crawlerName.toUpperCase());
            assertThat(bestMatch.get(), equalTo(expectedGroup));
        }

        @Test
        public void givenLowerCaseCrawlerName_whenGetBestMatchingGroup_thenResultEqualsExpected() {
            Optional<Group> bestMatch = utilities.getBestMatchingGroup(groups, crawlerName.toLowerCase());
            assertThat(bestMatch.get(), equalTo(expectedGroup));
        }

    }


    public static class CreateCharSourceForTests {

        private URI robotsUri;
        private long robotsSize;
        private RobotsUtilities utilities;

        @Before
        public final void setup() throws URISyntaxException, IOException {
            URL resource = Resources.getResource(CreateCharSourceForTests.class, "bush_http_www.whitehouse.gov_robots.txt");
            robotsUri = resource.toURI();
            robotsSize = Resources.asByteSource(resource).size();
            utilities = new RobotsUtilities();
        }

        @Test(expected = SizeLimitExceededException.class)
        public void givenRobotsMuchLargerThanLimit_whenRead_thenThrowsSLEE() throws IOException {
            CharSource source = utilities.createCharSourceFor(robotsUri, robotsSize / 2);
            source.read();
        }

        @Test(expected = SizeLimitExceededException.class)
        public void givenRobotsLargerThanLimit_whenRead_thenThrowsSLEE() throws IOException {
            CharSource source = utilities.createCharSourceFor(robotsUri, robotsSize - 1);
            source.read();
        }

        @Test
        public void givenRobotsEqualToLimit_whenRead_thenNoExceptionThrow() throws IOException {
            CharSource source = utilities.createCharSourceFor(robotsUri, robotsSize);
            source.read();
        }

        @Test
        public void givenRobotsSmallerThanLimit_whenRead_thenNoExceptionThrow() throws IOException {
            CharSource source = utilities.createCharSourceFor(robotsUri, robotsSize + 1);
            source.read();
        }

        @Test
        public void givenRobotsMuchSmallerThanLimit_whenRead_thenNoExceptionThrow() throws IOException {
            CharSource source = utilities.createCharSourceFor(robotsUri, robotsSize * 2);
            source.read();
        }

    }
}
