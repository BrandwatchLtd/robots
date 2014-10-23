package com.brandwatch.robots;

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
import java.net.URI;

import static com.google.common.io.Resources.asCharSource;
import static com.google.common.io.Resources.getResource;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class BlankPathsTest {

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
    public void givenAllowBlankPathDirective_whenIsAllowed_thenReturnsTrue() throws Exception {
        parse("blank_allow_robots.txt");
        boolean allowed = service.isAllowed(agent, URI.create("http://example.com/index.html"));
        assertThat(allowed, is(true));
    }

    @Test
    public void givenDisallowBlankPathDirective_whenIsAllowed_thenReturnsTrue() throws Exception {
        parse("blank_disallow_robots.txt");
        boolean allowed = service.isAllowed(agent, URI.create("http://example.com/index.html"));
        assertThat(allowed, is(true));
    }

    private void parse(String robotsResourceName) throws IOException {
        final CharSource fakeSource = asCharSource(
                getResource(getClass(), robotsResourceName),
                Charsets.UTF_8);
        when(charSourceSupplier.get(any(URI.class))).thenReturn(fakeSource);
    }


}
