package com.brandwatch.robots;

import com.brandwatch.robots.domain.Group;
import com.brandwatch.robots.domain.PathDirective;
import com.brandwatch.robots.domain.Robots;
import com.brandwatch.robots.domain.SiteMapDirective;
import com.brandwatch.robots.parser.ParseException;
import com.brandwatch.robots.parser.RobotsTxtParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.Reader;
import java.util.regex.Pattern;

import static com.brandwatch.robots.AbstractDataTest.resourceReader;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class RobotsBuildingHandlerFunctionalTest {

    @Test
    public void givenDailyMailBoards_whenParse_thenRobotsObjectEqualsExpected() throws IOException, ParseException {
        Reader reader = resourceReader("http_boards.dailymail.co.uk_robots.txt");
        RobotsTxtParser robotsTxtParser = new RobotsTxtParser(reader);
        RobotsUtilities utilities = new RobotsUtilities();
        RobotsBuildingHandler handler = new RobotsBuildingHandler(utilities);
        robotsTxtParser.parse(handler);

        Robots expected = new Robots.Builder()
                .withGroup(new Group.Builder()
                        .withUserAgent("*")
                        .withDirective(new PathDirective(PathDirective.Field.disallow, "*.js", Pattern.compile("")))
                        .withDirective(new PathDirective(PathDirective.Field.disallow, "/search.php*", Pattern.compile("")))
                        .withDirective(new PathDirective(PathDirective.Field.disallow, "/includes/", Pattern.compile("")))
                        .withDirective(new PathDirective(PathDirective.Field.disallow, "/install/", Pattern.compile("")))
                        .withDirective(new PathDirective(PathDirective.Field.disallow, "/customavatars/", Pattern.compile("")))
                        .build())
                .build();

        Robots actual = handler.get();

        assertThat(actual, equalTo(expected));
    }

    @Test
    public void givenWwwBrandwatchCom_whenParse_thenRobotsObjectEqualsExpected() throws IOException, ParseException {
        Reader reader = resourceReader("http_www.brandwatch.com_robots.txt");
        RobotsTxtParser robotsTxtParser = new RobotsTxtParser(reader);
        RobotsUtilities utilities = new RobotsUtilities();
        RobotsBuildingHandler handler = new RobotsBuildingHandler(utilities);
        robotsTxtParser.parse(handler);

        Robots expected = new Robots.Builder()
                .withGroup(new Group.Builder()
                        .withUserAgent("iisbot/1.0 (+http://www.iis.net/iisbot.html)")
                        .withDirective(new PathDirective(PathDirective.Field.allow, "/", Pattern.compile("")))
                        .build())
                .withGroup(new Group.Builder()
                        .withUserAgent("*")
                        .withDirective(new PathDirective(PathDirective.Field.disallow, "/wp-admin/", Pattern.compile("")))
                        .withDirective(new PathDirective(PathDirective.Field.disallow, "/wp-includes/", Pattern.compile("")))
                        .build())
                .withNonGroupDirective(new SiteMapDirective("http://www.brandwatch.com/sitemap.xml.gz"))
                .withNonGroupDirective(new SiteMapDirective("http://www.brandwatch.com/de/sitemap.xml.gz"))
                .build();

        Robots actual = handler.get();

        assertThat(actual, equalTo(expected));
    }


}
