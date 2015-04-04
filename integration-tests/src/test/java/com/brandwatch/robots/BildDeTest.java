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
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BildDeTest {

    @Spy
    private RobotsUtilities utilities;
    @Mock
    private CharSourceSupplier charSourceSupplier;

    private RobotsService service;

    private String agent = "magpie-crawler/1.1 (U; Linux x86_64; en-GB; +http://www.brandwatch.net)";

    @Before
    public void setup() {
        RobotsConfig config = new RobotsConfig();
        RobotsFactory factory = spy(new RobotsFactory(config));
        when(factory.getUtilities()).thenReturn(utilities);
        when(factory.createCharSourceSupplier()).thenReturn(charSourceSupplier);

        final CharSource fakeSource = asCharSource(
                getResource(getClass(), "http_www.bild.de_robots_20141017.txt"),
                Charsets.UTF_8);
        when(charSourceSupplier.get(any(URI.class))).thenReturn(fakeSource);

        service = factory.createService();
    }

    @Test
    public void givenRssFeedPath_isAllowed_thenReturnsTrue() throws Exception {
        boolean allowed = service.isAllowed(agent, URI.create("http://www.bild.de/rssfeeds/"));
        assertThat(allowed, is(true));
    }

    @Test
    public void givenRootPath_isAllowed_thenReturnsFalse() throws Exception {
        boolean allowed = service.isAllowed(agent, URI.create("http://www.bild.de/"));
        assertThat(allowed, is(false));
    }

}
