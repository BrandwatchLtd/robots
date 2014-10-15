package com.brandwatch.robots;

import com.brandwatch.robots.parser.RobotsParser;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;
import org.junit.Before;
import org.junit.runners.Parameterized;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.Reader;

import static com.google.common.io.Resources.getResource;

public abstract class AbstractDataTest {

    private final String resourceName;
    protected RobotsParser robotsTxtParser;

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
                .add(new Object[]{"bw.jar_http_www.manutd.com_robots.txt"})
                .add(new Object[]{"bw.jar_multiagents1.txt"})
                .add(new Object[]{"bw.jar_multiagents2.txt"})
                .add(new Object[]{"bw.jar_multiagents3.txt"})
                .add(new Object[]{"bw.jar_multirecords.txt"})
                .add(new Object[]{"bw.jar_http_www.robotstxt.org_norobots-rfc.txt"})
                .build();
    }

    public static Reader resourceReader(@Nonnull String name) throws IOException {
        return Resources.asCharSource(
                getResource(AbstractDataTest.class, name), Charsets.UTF_8
        ).openBufferedStream();
    }

    @Before
    public final void setupAbstractTest() throws IOException {
        robotsTxtParser = new RobotsParser(resourceReader(resourceName));
    }

}