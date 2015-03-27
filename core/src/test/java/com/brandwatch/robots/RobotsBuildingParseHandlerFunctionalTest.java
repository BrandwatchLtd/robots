package com.brandwatch.robots;

/*
 * #%L
 * Robots (core)
 * %%
 * Copyright (C) 2015 Brandwatch
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

import com.brandwatch.robots.domain.*;
import com.brandwatch.robots.matching.ExpressionCompiler;
import com.brandwatch.robots.matching.Matcher;
import com.brandwatch.robots.parser.ParseException;
import com.brandwatch.robots.parser.RobotsParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.Reader;

import static com.brandwatch.robots.AbstractDataTest.resourceReader;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class RobotsBuildingParseHandlerFunctionalTest {

    RobotsConfig config = new RobotsConfig();
    RobotsFactory factory = new RobotsFactory(config);
    ExpressionCompiler agentExpressionCompiler = factory.createAgentExpressionCompiler();
    ExpressionCompiler pathExpressionCompiler = factory.createPathExpressionCompiler();
    Matcher<String> ALL = pathExpressionCompiler.compile("*");

    @Test
    public void givenDailyMailBoards_whenParse_thenRobotsObjectEqualsExpected() throws IOException, ParseException {
        Reader reader = resourceReader("http_boards.dailymail.co.uk_robots.txt");
        RobotsParser robotsTxtParser = new RobotsParser(reader);
        RobotsBuildingParseHandler handler = new RobotsBuildingParseHandler(
                pathExpressionCompiler,
                agentExpressionCompiler);
        robotsTxtParser.parse(handler);

        Robots expected = new Robots.Builder()
                .withGroup(new Group.Builder()
                        .withDirective(new AgentDirective("*", agentExpressionCompiler.compile("*")))
                        .withDirective(new PathDirective(PathDirective.Field.disallow, "*.js", ALL))
                        .withDirective(new PathDirective(PathDirective.Field.disallow, "/search.php*", ALL))
                        .withDirective(new PathDirective(PathDirective.Field.disallow, "/includes/", ALL))
                        .withDirective(new PathDirective(PathDirective.Field.disallow, "/install/", ALL))
                        .withDirective(new PathDirective(PathDirective.Field.disallow, "/customavatars/", ALL))
                        .build())
                .build();

        Robots actual = handler.get();

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void givenWwwBrandwatchCom_whenParse_thenRobotsObjectEqualsExpected() throws IOException, ParseException {
        Reader reader = resourceReader("http_www.brandwatch.com_robots.txt");
        RobotsParser robotsTxtParser = new RobotsParser(reader);
        RobotsConfig config = new RobotsConfig();
        RobotsBuildingParseHandler handler = new RobotsBuildingParseHandler(pathExpressionCompiler, agentExpressionCompiler);
        robotsTxtParser.parse(handler);

        Robots expected = new Robots.Builder()
                .withGroup(new Group.Builder()
                        .withDirective(new AgentDirective("iisbot/1.0 (+http://www.iis.net/iisbot.html)", agentExpressionCompiler.compile("iisbot/1.0 (+http://www.iis.net/iisbot.html)")))
                        .withDirective(new PathDirective(PathDirective.Field.allow, "/", ALL))
                        .build())
                .withGroup(new Group.Builder()
                        .withDirective(new AgentDirective("*", factory.createAgentExpressionCompiler().compile("*")))
                        .withDirective(new PathDirective(PathDirective.Field.disallow, "/wp-admin/", ALL))
                        .withDirective(new PathDirective(PathDirective.Field.disallow, "/wp-includes/", ALL))
                        .build())
                .withNonGroupDirective(new SiteMapDirective("http://www.brandwatch.com/sitemap.xml.gz"))
                .withNonGroupDirective(new SiteMapDirective("http://www.brandwatch.com/de/sitemap.xml.gz"))
                .build();

        Robots actual = handler.get();

        assertThat(actual, equalTo(expected));
    }


}
