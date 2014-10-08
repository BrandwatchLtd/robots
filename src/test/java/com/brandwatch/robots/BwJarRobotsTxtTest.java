package com.brandwatch.robots;

import com.google.common.base.Charsets;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;

import static com.google.common.io.Resources.asCharSource;
import static com.google.common.io.Resources.getResource;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class BwJarRobotsTxtTest {

    private RobotsUtilities utilities;
    private RobotsService service;
    private String agent = "magpie-crawler";

    @Before
    public void setup() {

        // mock sourceFactory so we don't cause network IO
        utilities = spy(new RobotsUtilities());

        RobotsConfig config = spy(new RobotsConfig());
        when(config.getUtilities()).thenReturn(utilities);

        service = config.getService();
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

    private void parse(String robotsResourceName) throws IOException {
        doReturn(
                asCharSource(getResource(BwJarRobotsTxtTest.class,
                        robotsResourceName), Charsets.UTF_8))
                .when(utilities).createCharSourceFor(any(URI.class));
    }

    private boolean isAllowed(String url) throws MalformedURLException {
        return service.isAllowed(agent, URI.create(url));
    }
}
