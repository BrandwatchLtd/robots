package com.brandwatch.robots;

/*
 * #%L
 * Robots (integration tests)
 * %%
 * Copyright (C) 2014 - 2015 Brandwatch
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

import com.brandwatch.robots.net.CharSourceSupplier;
import com.google.common.base.Charsets;
import com.google.common.io.CharSource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;

import static com.google.common.io.Resources.asCharSource;
import static com.google.common.io.Resources.getResource;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * These tests are copied from the old robots manager. They aren't great,
 * but we include them verbatim (as much as possible) to find regressions.
 */
@RunWith(MockitoJUnitRunner.class)
public class BwJarRobotsTxtTest {

    @Spy
    private RobotsUtilities utilities;
    @Mock
    private CharSourceSupplier charSourceSupplier;

    private RobotsService service;
    private String agent = "magpie-crawler";

    @Before
    public void setup() {

        RobotsConfig config = new RobotsConfig();
        RobotsFactory factory = spy(new RobotsFactory(config));
        when(factory.getUtilities()).thenReturn(utilities);
        when(factory.createCharSourceSupplier()).thenReturn(charSourceSupplier);

        service = factory.createService();
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

    @Test
    public void testMultipleAsterisk() throws Exception {
        parse("multipleAsterisk.txt");
        assertTrue(isAllowed("http://www.example.com/unhipbot.html"));
    }

    private void parse(String robotsResourceName) throws IOException {
        final CharSource fakeSource = asCharSource(
                getResource(getClass(), robotsResourceName),
                Charsets.UTF_8);
        when(charSourceSupplier.get(any(URI.class))).thenReturn(fakeSource);
    }

    private boolean isAllowed(String url) throws MalformedURLException {
        return service.isAllowed(agent, URI.create(url));
    }
}
