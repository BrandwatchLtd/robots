package com.brandwatch.robots.net;

import com.brandwatch.robots.domain.Group;
import com.brandwatch.robots.domain.PathDirective;
import com.brandwatch.robots.domain.Robots;
import com.brandwatch.robots.domain.SiteMapDirective;
import com.brandwatch.robots.net.RobotsBuildingHandler;
import com.brandwatch.robots.parser.ParseException;
import com.brandwatch.robots.parser.RobotsTxtParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.Reader;

import static com.brandwatch.robots.AbstractDataTest.resourceReader;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class RobotsBuildingHandlerFunctionalTest {

    @Test
    public void givenDailyMailBoards_whenParse_thenRobotsObjectEqualsExpected() throws IOException, ParseException {
        Reader reader = resourceReader("http_boards.dailymail.co.uk_robots.txt");
        RobotsTxtParser robotsTxtParser = new RobotsTxtParser(reader);
        RobotsBuildingHandler handler = new RobotsBuildingHandler();
        robotsTxtParser.parse(handler);

        Robots expected = new Robots.Builder()
                .withGroup(new Group.Builder()
                        .withUserAgent("*")
                        .withDirective(new PathDirective(PathDirective.Field.disallow, "*.js"))
                        .withDirective(new PathDirective(PathDirective.Field.disallow, "/search.php*"))
                        .withDirective(new PathDirective(PathDirective.Field.disallow, "/includes/"))
                        .withDirective(new PathDirective(PathDirective.Field.disallow, "/install/"))
                        .withDirective(new PathDirective(PathDirective.Field.disallow, "/customavatars/"))
                        .build())
                .build();

        Robots actual = handler.get();

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void givenWwwBrandwatchCom_whenParse_thenRobotsObjectEqualsExpected() throws IOException, ParseException {
        Reader reader = resourceReader("http_www.brandwatch.com_robots.txt");
        RobotsTxtParser robotsTxtParser = new RobotsTxtParser(reader);
        RobotsBuildingHandler handler = new RobotsBuildingHandler();
        robotsTxtParser.parse(handler);

        Robots expected = new Robots.Builder()
                .withGroup(new Group.Builder()
                        .withUserAgent("iisbot/1.0 (+http://www.iis.net/iisbot.html)")
                        .withDirective(new PathDirective(PathDirective.Field.allow, "/"))
                        .build())
                .withGroup(new Group.Builder()
                        .withUserAgent("*")
                        .withDirective(new PathDirective(PathDirective.Field.disallow, "/wp-admin/"))
                        .withDirective(new PathDirective(PathDirective.Field.disallow, "/wp-includes/"))
                        .build())
                .withNonGroupDirective(new SiteMapDirective("http://www.brandwatch.com/sitemap.xml.gz"))
                .withNonGroupDirective(new SiteMapDirective("http://www.brandwatch.com/de/sitemap.xml.gz"))
                .build();

        Robots actual = handler.get();

        assertThat(actual, equalTo(expected));
    }


}
