package com.brandwatch.robots.cli;

/*
 * #%L
 * Robots (integration tests)
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
        main.configure(array("--help"));
        main.run();
        assertThat(out.toString(), containsString("Usage: "));
    }

    @Test
    public void givenHelpRequested_whenRun_thenErrIsEmpty() {
        main.configure(array("--help"));
        main.run();
        assertThat(err.toString(), isEmptyString());
    }

    @Test
    public void givenBrandwatchDisallowed_whenRun_thenResourceIsDisallowed() {
        main.configure(array("http://www.brandwatch.com/wp-admin/"));
        main.run();
        assertThat(out.toString(), containsString("http://www.brandwatch.com/wp-admin/: disallowed"));
    }

    @Test
    public void givenBrandwatchAllowed_whenRun_thenResourceIsAllowed() {
        main.configure(array("http://www.brandwatch.com/the-team/"));
        main.run();
        assertThat(out.toString(), containsString("http://www.brandwatch.com/the-team/: allowed"));
    }

    public static <T> T[] array(T... args) {
        return args;
    }

}
