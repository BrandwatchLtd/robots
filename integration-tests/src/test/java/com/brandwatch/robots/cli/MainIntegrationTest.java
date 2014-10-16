package com.brandwatch.robots.cli;

import com.google.common.base.Charsets;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class MainIntegrationTest {

    private static final String ENCODING = Charsets.UTF_8.name();

    private ByteArrayOutputStream out;
    private ByteArrayOutputStream err;
    private Main main;

    @Before
    public void setup() throws UnsupportedEncodingException {
        out = new ByteArrayOutputStream();
        err = new ByteArrayOutputStream();
        main = new Main(
                new PrintStream(out, true, ENCODING),
                new PrintStream(err, true, ENCODING));
    }

    @After
    public void debugPrintOutput() {
        System.out.println(out.toString());
        System.err.println(err.toString());
    }

    @Test
    public void givenNoParameters_whenRun() {
        main.configure();
        main.run();
        assertThat(err.toString(), not(isEmptyString()));
    }

    @Test
    public void givenHelpRequested_whenRun_thenOutContainsUsage() {
        main.configure($("--help"));
        main.run();
        assertThat(out.toString(), containsString("Usage: "));
    }

    @Test
    public void givenHelpRequested_whenRun_thenErrIsEmpty() {
        main.configure($("--help"));
        main.run();
        assertThat(err.toString(), isEmptyString());
    }

    @Test
    public void givenBrandwatchDisallowed_whenRun_thenResourceIsDisallowed() {
        main.configure($("http://www.brandwatch.com/wp-admin/"));
        main.run();
        assertThat(out.toString(), containsString("http://www.brandwatch.com/wp-admin/: disallowed"));
    }

    @Test
    public void givenBrandwatchAllowed_whenRun_thenResourceIsAllowed() {
        main.configure($("http://www.brandwatch.com/the-team/"));
        main.run();
        assertThat(out.toString(), containsString("http://www.brandwatch.com/the-team/: allowed"));
    }

    public static <T> T[] $(T... args) {
        return args;
    }

}
