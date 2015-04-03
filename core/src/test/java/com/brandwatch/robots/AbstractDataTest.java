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

import com.brandwatch.robots.parser.RobotsParser;
import com.brandwatch.robots.parser.RobotsParserImpl;
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
                .build();
    }

    public static Reader resourceReader(@Nonnull String name) throws IOException {
        return Resources.asCharSource(
                getResource(AbstractDataTest.class, name), Charsets.UTF_8
        ).openBufferedStream();
    }

    @Before
    public final void setupAbstractTest() throws IOException {
        robotsTxtParser = new RobotsParserImpl(resourceReader(resourceName));
    }

}