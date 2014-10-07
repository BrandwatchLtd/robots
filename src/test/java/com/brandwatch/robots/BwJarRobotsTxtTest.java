package com.brandwatch.robots;

import com.brandwatch.robots.net.RobotsCharSourceFactory;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Set;

import static com.google.common.io.Resources.asCharSource;
import static com.google.common.io.Resources.getResource;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BwJarRobotsTxtTest {

    private RobotsCharSourceFactory sourceFactory;
    private RobotExclusionServiceImpl service;
    private String agent = "magpie-crawler";

    @Before
    public void setup() {

        RobotsUtilities utilities = new RobotsUtilities();

        // mock sourceFactory so we don't cause network IO
        sourceFactory = mock(RobotsCharSourceFactory.class);

        RobotsDownloader downloader = new RobotsDownloaderImpl(utilities);
        service = new RobotExclusionServiceImpl(sourceFactory, downloader, utilities);


        service.startAsync();
        service.awaitRunning();
    }

    @Test
    public void testRobots() throws Exception {
        // http://www.robotstxt.org/norobots-rfc.txt

        parse("bw.jar_http_www.robotstxt.org_norobots-rfc.txt");

        assertFalse(isAllowed("http://www.fict.org/org/plans.html"));
        assertFalse(isAllowed("http://www.fict.org/org/about.html"));
        assertFalse(isAllowed("http://www.fict.org/index.html"));
    }

    @Test
    public void testManutd() throws Exception {
        // http://www.manutd.com/robots.txt
        parse("bw.jar_http_www.manutd.com_robots.txt");
        assertFalse(isAllowed("http://www.manutd.com/Live-Webchat/Endava-Test.aspx"));
        assertFalse(isAllowed("http://www.manutd.com/VideoMessages/AON.aspx"));
        assertFalse(isAllowed("http://www.manutd.com/Supporters-Club-Back-End/Login.aspx"));
        assertTrue(isAllowed("http://www.manutd.com/en/News-And-Features.aspx"));
        assertTrue(isAllowed("http://www.manutd.com/en/News-And-Features/Football-News/2012/Sep/rio-ferdinand-excited-by-youngsters-playing-in-capital-one-cup.aspx"));
    }

    @Test
    public void testMultiAgents1() throws Exception {
        parse("bw.jar_multiagents1.txt");
        assertFalse(isAllowed("http://www.example.com/"));
        assertFalse(isAllowed("http://www.example.com/index.html"));
    }

    @Test
    public void testMultiAgents2() throws Exception {
         parse("bw.jar_multiagents2.txt");
        assertFalse(isAllowed("http://www.example.com/"));
        assertFalse(isAllowed("http://www.example.com/index.html"));
    }

    @Test
    public void testMultiAgents3() throws Exception {
        parse("bw.jar_multiagents3.txt");
        assertFalse(isAllowed("http://www.example.com/"));
        assertFalse(isAllowed("http://www.example.com/index.html"));
    }

    @Test
    public void testMultiRecords() throws Exception {
        parse("bw.jar_multirecords.txt");
        assertFalse(isAllowed("http://www.example.com/magpie-crawler.html"));
        assertTrue(isAllowed("http://www.example.com/unhipbot.html"));
    }

    private void parse(String resource) throws IOException {
        when(sourceFactory.createFor(any(URI.class))).thenReturn(
                asCharSource(getResource(BwJarRobotsTxtTest.class,
                        resource), Charsets.UTF_8));
    }

    private boolean isAllowed(String url) throws MalformedURLException {
        return service.isAllowed(agent, URI.create(url));
    }
}
