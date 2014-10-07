package com.brandwatch.robots;

import com.brandwatch.robots.domain.Group;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.net.URI;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
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


    public static class IsPathPatternMatchedTest {

        private RobotsUtilities utilities;

        @Before
        public final void setup() {
            utilities = new RobotsUtilities();
        }

        @Test
        public void givenMatchingArgs_whenIsPathPatternMatched_thenReturnsTrue() {
            String pathPattern = "/";
            String path = "/index.html";
            boolean result = utilities.isPathExpressionMatched(pathPattern, path);
            assertThat(result, is(true));
        }

        @Test
        public void givenNonMatchingArgs_whenIsPathPatternMatched_thenReturnsFalse() {
            String pathPattern = "/stuff";
            String path = "/index.html";
            boolean result = utilities.isPathExpressionMatched(pathPattern, path);
            assertThat(result, is(false));
        }

        @Test
        public void givenMatchingWildcardPattern_whenIsPathPatternMatched_thenReturnsTrue() {
            String pathPattern = "/*";
            String path = "/index.html";
            boolean result = utilities.isPathExpressionMatched(pathPattern, path);
            assertThat(result, is(true));
        }

        @Test(expected = NullPointerException.class)
        public void givenNullPattern_whenIsPathPatternMatched_thenThrowsNPE() {
            String pathPattern = null;
            String path = "/index.html";
            utilities.isPathExpressionMatched(pathPattern, path);
        }

        @Test(expected = NullPointerException.class)
        public void givenNullPath_whenIsPathPatternMatched_thenThrowsNPE() {
            String pathPattern = "/";
            String path = null;
            utilities.isPathExpressionMatched(pathPattern, path);
        }


        // missing prefix slash

    }


    @RunWith(Parameterized.class)
    public static class IsPathPatternMatchedTestDataTests {

        private final String pathPattern;
        private final URI uri;
        private final boolean expectedResult;

        private RobotsUtilities utilities;

        public IsPathPatternMatchedTestDataTests(String pathPattern, String path, boolean expectedResult) {
            this.pathPattern = pathPattern;
            this.uri = URI.create("http://www.example.com" + (path.startsWith("/") ? "" : "/") + path);
            this.expectedResult = expectedResult;
        }

        @Parameterized.Parameters(name = "{index}: {0} {1} {2}")
        public static Iterable<Object[]> data() {
            return ImmutableList.<Object[]>builder()
                    .add(new Object[]{"/*", "/path/to/index.html", true})
                    .add(new Object[]{"/*", "/index.html", true})
                    .add(new Object[]{"/*", "/?query", true})
                    .add(new Object[]{"/*", "/#fragment", true})
                    .add(new Object[]{"/*", "/", true})
                    .add(new Object[]{"/*", "", true})
                    .add(new Object[]{"*", "/path/to/index.html", true})
                    .add(new Object[]{"*", "/index.html", true})
                    .add(new Object[]{"*", "/?query", true})
                    .add(new Object[]{"*", "/#fragment", true})
                    .add(new Object[]{"*", "/", true})
                    .add(new Object[]{"*", "", true})
                    .add(new Object[]{"/index.html", "/index.html", true})
                    .add(new Object[]{"index.html", "/index.html", true})
                    .add(new Object[]{"index.html", "/path/to/index.html", false})
                    .add(new Object[]{"/index.html", "/index.html?query=foo", true})
                    .add(new Object[]{"/index.html", "/index.html#fragment", true})
                    .add(new Object[]{"/index.html", "/index.html.php", true})
                    .add(new Object[]{"/index.html$", "/index.html.php", false})
                    .add(new Object[]{"/fish", "/fish", true})
                    .add(new Object[]{"/fish", "/fish.html", true})
                    .add(new Object[]{"/fish", "/fish.html", true})
                    .add(new Object[]{"/fish", "/fish/salmon.html", true})
                    .add(new Object[]{"/fish", "/fishheads", true})
                    .add(new Object[]{"/fish", "/fishheads/yummy.html", true})
                    .add(new Object[]{"/fish", "/fish.php?id=anything", true})
                    .add(new Object[]{"/fish", "/Fish.asp", false})
                    .add(new Object[]{"/fish", "/catfish", false})
                    .add(new Object[]{"/fish", "/?id=fish", false})
                    .add(new Object[]{"/fish*", "/fish", true})
                    .add(new Object[]{"/fish*", "/fish.html", true})
                    .add(new Object[]{"/fish*", "/fish.html", true})
                    .add(new Object[]{"/fish*", "/fish/salmon.html", true})
                    .add(new Object[]{"/fish*", "/fishheads", true})
                    .add(new Object[]{"/fish*", "/fishheads/yummy.html", true})
                    .add(new Object[]{"/fish*", "/fish.php?id=anything", true})
                    .add(new Object[]{"/fish*", "/Fish.asp", false})
                    .add(new Object[]{"/fish*", "/catfish", false})
                    .add(new Object[]{"/fish*", "/?id=fish", false})
                    .add(new Object[]{"/fish/", "/fish/?id=anything", true})
                    .add(new Object[]{"/fish/", "/fish/", true})
                    .add(new Object[]{"/fish/", "/fish/salmon.htm", true})
                    .add(new Object[]{"/fish/", "/fish", false})
                    .add(new Object[]{"/fish/", "/fish.html", false})
                    .add(new Object[]{"/fish/", "/Fish/Salmon.asp", false})
                    .add(new Object[]{"fish/", "/fish/?id=anything", true})
                    .add(new Object[]{"fish/", "/fish/", true})
                    .add(new Object[]{"fish/", "/fish/salmon.htm", true})
                    .add(new Object[]{"fish/", "/fish", false})
                    .add(new Object[]{"fish/", "/fish.html", false})
                    .add(new Object[]{"fish/", "/Fish/Salmon.asp", false})
                    .add(new Object[]{"/*.php", "/filename.php", true})
                    .add(new Object[]{"/*.php", "/folder/filename.php", true})
                    .add(new Object[]{"/*.php", "/folder/filename.php?parameters", true})
                    .add(new Object[]{"/*.php", "/folder/any.php.file.html", true})
                    .add(new Object[]{"/*.php", "/filename.php/", true})
                    .add(new Object[]{"/*.php", "/", false})
                    .add(new Object[]{"/*.php", "/windows.PHP", false})
                    .add(new Object[]{"/fish*.php", "/fish.php", true})
                    .add(new Object[]{"/fish*.php", "/fishheads/catfish.php?parameters", true})
                    .add(new Object[]{"/fish*.php", "/Fish.PHP", false})
                    .add(new Object[]{"/*/docs/*.html", "/a/docs/1.html", true})
                    .add(new Object[]{"/*/docs/*.html", "/a/docs/2.html", true})
                    .add(new Object[]{"/*/docs/*.html", "/b/docs/1.html", true})
                    .add(new Object[]{"/*/docs/*.html", "/b/docs/2.html", true})
                    .add(new Object[]{"/*/docs/*.html", "/b/docs/2.html.stuff", true})
                    .add(new Object[]{"/*/docs/*.html", "/a/b/c/docs/1.html", true})
                    .add(new Object[]{"/*/docs/*.html", "/docs/1.html", false})
                    .add(new Object[]{"/*/docs/*.html", "/a/docs/1.php", false})
                    .add(new Object[]{"/*/docs/*.html", "//docs/1.html", true})
                    .add(new Object[]{"*/index.html$", "/index.html", true})
                    .add(new Object[]{"/index.htm$", "/index.htm", true})
                    .add(new Object[]{"/index.htm$", "/index.html", false})
                    .add(new Object[]{"/p", "/page", true})
                    .add(new Object[]{"/", "/page", true})
                    .add(new Object[]{"/folder/", "/folder/page", true})
                    .add(new Object[]{"/folder", "/folder/page", true})
                    .add(new Object[]{"/page", "/page.htm", true})
                    .add(new Object[]{"/*.htm", "/page.htm", true})
                    .add(new Object[]{"/$", "/", true})
                    .add(new Object[]{"/", "/", true})
                    .add(new Object[]{"/$", "/page.htm", false})
                    .add(new Object[]{"/", "/page.htm", true})
                    .add(new Object[]{"/************************************", "/index.html", true})
                    .add(new Object[]{"/*$", "/index.html", true})
                    .build();
        }

        @Before
        public final void setup() {
            utilities = new RobotsUtilities();
        }

        @Test
        public void whenIsPathPatternMatched_thenResultEqualsExpected() {
            boolean result = utilities.isPathExpressionMatched(pathPattern, uri);
            assertThat(result, equalTo(expectedResult));
        }

    }


}
