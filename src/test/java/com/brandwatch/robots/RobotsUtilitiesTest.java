package com.brandwatch.robots;

import com.brandwatch.robots.domain.Group;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(Enclosed.class)
public class RobotsUtilitiesTest {

    public static class GetAgentMatchLengthTest {

        private RobotsUtilities utilities;

        @Before
        public final void setup() {
            utilities = new RobotsUtilities();
        }

        @Test(expected = NullPointerException.class)
        public void givenPatternIsNull_whenGetAgentMatchLength_thenThrowsNPE() {
            String pattern = null;
            String agent = "googlebot";
            utilities.getAgentMatchLength(pattern, agent);
        }

        @Test(expected = NullPointerException.class)
        public void givenAgentIsNull_whenGetAgentMatchLength_thenThrowsNPE() {
            String pattern = "*";
            String agent = null;
            utilities.getAgentMatchLength(pattern, agent);
        }

        @Test
        public void givenPatternIsWildcard_whenGetAgentMatchLength_thenReturnsZero() {
            String pattern = "*";
            String agent = "googlebot";
            int length = utilities.getAgentMatchLength(pattern, agent);
            assertThat(length, equalTo(0));
        }

        @Test
        public void givenPatternMatches_whenGetAgentMatchLength_thenReturnsPatternLength() {
            String pattern = "google";
            String agent = "googlebot";
            int length = utilities.getAgentMatchLength(pattern, agent);
            assertThat(length, equalTo(pattern.length()));
        }

        @Test
        public void givenPatternMatchesIgnoringCase_whenGetAgentMatchLength_thenReturnsPatternLength() {
            String pattern = "gOoGlE";
            String agent = "googlebot";
            int length = utilities.getAgentMatchLength(pattern, agent);
            assertThat(length, equalTo(pattern.length()));
        }

        @Test
        public void givenPatternNotMatches_whenGetAgentMatchLength_thenReturnsMinusOne() {
            String pattern = "yahoo";
            String agent = "googlebot";
            int length = utilities.getAgentMatchLength(pattern, agent);
            assertThat(length, equalTo(-1));
        }

        @Test
        public void givenPatternIsEmpty_whenGetAgentMatchLength_thenReturnsZero() {
            String pattern = "";
            String agent = "googlebot";
            int length = utilities.getAgentMatchLength(pattern, agent);
            assertThat(length, equalTo(0));
        }

        @Test
        public void givenPatternAndAgentAreEmpty_whenGetAgentMatchLength_thenReturnsZero() {
            String pattern = "";
            String agent = "";
            int length = utilities.getAgentMatchLength(pattern, agent);
            assertThat(length, equalTo(0));
        }

        @Test
        public void givenWildcardPattern_andAgentIsEmpty_whenGetAgentMatchLength_thenReturnsZero() {
            String pattern = "*";
            String agent = "";
            int length = utilities.getAgentMatchLength(pattern, agent);
            assertThat(length, equalTo(0));
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
            Group group = new Group.Builder().withUserAgent("*").build();
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

        private static final Group firstGroup = new Group.Builder().withUserAgent("googlebot-news").build();
        private static final Group secondGroup = new Group.Builder().withUserAgent("*").build();
        private static final Group thirdGroup = new Group.Builder().withUserAgent("googlebot").build();
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


}
