package com.brandwatch.robots;

import com.brandwatch.robots.parser.RobotsTxtParser;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import org.junit.Before;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.io.Reader;

import static com.google.common.io.Resources.getResource;

public abstract class AbstractDataTest {

    private final String resourceName;
    protected RobotsTxtParser robotsTxtParser;

    protected AbstractDataTest(String resourceName) {
        this.resourceName = resourceName;
    }

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Iterable<Object[]> data() {
        return ImmutableList.<Object[]>builder()
                .add(new Object[]{"http_boards.dailymail.co.uk_robots.txt"})
                .add(new Object[]{"https_www.drupal.org_robots.txt"})
                .add(new Object[]{"http_www.brandwatch.com_robots.txt"})
                .add(new Object[]{"http_www.dailymail.co.uk_robots.txt"})
                .add(new Object[]{"http_www.google.com_robots.txt"})
                .add(new Object[]{"https_twitter.com_robots.txt"})
                .add(new Object[]{"http_facebook.com_robots.txt"})
                .add(new Object[]{"obama_http_www.whitehouse.gov_robots.txt"})
                .add(new Object[]{"bush_http_www.whitehouse.gov_robots.txt"})
                .add(new Object[]{"http_www.last.fm_robots.txt"})
                .add(new Object[]{"http_www.reddit.com_robots.txt"})
                .build();
    }

    public static Reader resourceReader(String name) throws IOException {
        return Resources.asCharSource(
                getResource(AbstractDataTest.class, name), Charsets.UTF_8
        ).openBufferedStream();
    }

    @Before
    public final void setupAbstractTest() throws IOException {
        robotsTxtParser = new RobotsTxtParser(resourceReader(resourceName));
    }

}