package com.brandwatch.robots;

/*
 * #%L
 * Robots (core)
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

import com.brandwatch.robots.domain.AgentDirective;
import com.brandwatch.robots.domain.Group;
import com.brandwatch.robots.domain.PathDirective;
import com.brandwatch.robots.domain.Robots;
import com.brandwatch.robots.matching.EverythingMatcher;
import com.brandwatch.robots.matching.MatcherUtils;
import com.brandwatch.robots.net.exception.ExcludedDomainException;

import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.UncheckedExecutionException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URI;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyCollection;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.ws.rs.ProcessingException;

@RunWith(MockitoJUnitRunner.class)
public class RobotsServiceImplTest {

    private static final String CRAWLER_AGENT = "magpie";
    private static final URI RESOURCE_URI = URI.create("http://example.org/absolute/URI/with/absolute/path/to/resource.txt");
    private static final URI DOMAIN_EXCLUDED_RESOURCE_URI = URI.create("http://test.anotherexample.org/absolute/URI/with/absolute/path/to/resource.txt");
    private static final URI ROBOTS_URI = URI.create("http://example.org/robots.txt");
    private static final URI EXCLUDED_ROBOTS_URI = URI.create("http://anotherexample.org/robots.txt");
    private static final URI INCLUDED_ROBOTS_URI = URI.create("http://another.example.org/robots.txt");
    private static final URI SUBDOMAIN_EXCLUDED_ROBOTS_URI = URI.create("http://example.anotherexample.org/robots.txt");
    private static final URI PREFIX_INCLUDED_ROBOTS_URI = URI.create("http://example-anotherexample.org/robots.txt");
    private static final PathDirective ALLOW_PATH_DIRECTIVE = new PathDirective(PathDirective.Field.allow, "", new EverythingMatcher<String>());
    private static final PathDirective DISALLOW_PATH_DIRECTIVE = new PathDirective(PathDirective.Field.disallow, "", new EverythingMatcher<String>());
    private static final Robots EMPTY_ROBOTS = new Robots.Builder().build();
    private static final Group GROUP = new Group.Builder()
            .withDirective(new AgentDirective("xxx", new EverythingMatcher<String>()))
            .build();
    private static final Robots SINGLE_GROUP_ROBOTS = new Robots.Builder().withGroup(GROUP).build();
    private static final Set<String> EXCLUDED_DOMAINS = Sets.newHashSet("anotherexample.org");

    @Mock
    private RobotsLoader loader;

    @Mock
    private RobotsUtilities utilities;

    @Mock
    private MatcherUtils matcherUtils;

    private RobotsServiceImpl instance;

    @Before
    public final void startUp() throws Exception {
        instance = new RobotsServiceImpl(loader, utilities, matcherUtils, EXCLUDED_DOMAINS);
        when(utilities.getRobotsURIForResource(eq(RESOURCE_URI))).thenReturn(ROBOTS_URI);
        when(utilities.getRobotsURIForResource(eq(DOMAIN_EXCLUDED_RESOURCE_URI))).thenReturn(EXCLUDED_ROBOTS_URI);
        when(loader.load(any(URI.class))).thenReturn(EMPTY_ROBOTS);
    }

    @Test(expected = NullPointerException.class)
    public void givenNullUri_whenIsAllowed_thenThrowsNPE() {
        instance.isAllowed(CRAWLER_AGENT, null);
    }

    @Test(expected = NullPointerException.class)
    public void givenNullAgent_whenIsAllowed_thenThrowsNPE() {
        instance.isAllowed(null, RESOURCE_URI);
    }

    @Test
    public void givenExampleUri_whenIsAllowed_thenReturnsTrue() {
        boolean result = instance.isAllowed(CRAWLER_AGENT, RESOURCE_URI);
        assertThat(result, is(true));
    }

    @Test
    public void givenExampleUriWithExcludedDomain_whenIsAllowed_thenReturnsFalse() {
        boolean result = instance.isAllowed(CRAWLER_AGENT, DOMAIN_EXCLUDED_RESOURCE_URI);
        assertThat(result, is(false));
    }

    @Test
    public void givenExampleUriWithIncludedDomain_whenIsAllowed_thenReturnsTrue() {
        when(utilities.getRobotsURIForResource(eq(DOMAIN_EXCLUDED_RESOURCE_URI))).thenReturn(INCLUDED_ROBOTS_URI);
        boolean result = instance.isAllowed(CRAWLER_AGENT, DOMAIN_EXCLUDED_RESOURCE_URI);
        assertThat(result, is(true));
    }

    @Test
    public void givenExampleUriWithExcludedSubDomain_whenIsAllowed_thenReturnsFalse() {
        when(utilities.getRobotsURIForResource(eq(DOMAIN_EXCLUDED_RESOURCE_URI))).thenReturn(SUBDOMAIN_EXCLUDED_ROBOTS_URI);
        boolean result = instance.isAllowed(CRAWLER_AGENT, DOMAIN_EXCLUDED_RESOURCE_URI);
        assertThat(result, is(false));
    }

    @Test
    public void givenExampleUriWithIncludeDomainByPrefix_whenIsAllowed_thenReturnsTrue() {
        when(utilities.getRobotsURIForResource(eq(DOMAIN_EXCLUDED_RESOURCE_URI))).thenReturn(PREFIX_INCLUDED_ROBOTS_URI);
        boolean result = instance.isAllowed(CRAWLER_AGENT, DOMAIN_EXCLUDED_RESOURCE_URI);
        assertThat(result, is(true));
    }

    @Test
    public void givenExampleUriWithExcludedDomainException_whenIsAllowed_thenReturnsFalse() throws Exception {
        when(loader.load(eq(EXCLUDED_ROBOTS_URI))).thenThrow(new UncheckedExecutionException(new ProcessingException(new ExcludedDomainException())));
        boolean result = instance.isAllowed(CRAWLER_AGENT, DOMAIN_EXCLUDED_RESOURCE_URI);
        assertThat(result, is(false));
    }

    @Test
    public void givenExampleUri_whenIsAllowed_thenSourceFactoryIsInvoked() {
        instance.isAllowed(CRAWLER_AGENT, RESOURCE_URI);
        verify(utilities).getRobotsURIForResource(RESOURCE_URI);
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenMalformedResourceURI_whenIsAllowed_thenThrowsIAE() {
        when(utilities.getRobotsURIForResource(RESOURCE_URI)).thenThrow(new IllegalArgumentException());
        instance.isAllowed(CRAWLER_AGENT, RESOURCE_URI);
    }

    @Test
    public void whenClose_thenCloseCalledOnLoader() throws IOException {
        instance.close();
        verify(loader).close();
    }

    @Test
    public void givenLoaderThrowsException_whenIsAllowed_thenReturnsTrue() throws Exception {
        when(loader.load(any(URI.class))).thenThrow(Exception.class);
        boolean allowed = instance.isAllowed(CRAWLER_AGENT, RESOURCE_URI);
        assertThat(allowed, equalTo(true));
    }

    @Test
    public void givenLoaderProducesSingleNonMatchingGroup_whenIsAllowed_thenReturnsTrue() throws Exception {
        when(loader.load(any(URI.class))).thenReturn(SINGLE_GROUP_ROBOTS);
        when(matcherUtils.getMostSpecificMatchingGroup(anyCollection(), anyString())).thenReturn(Optional.absent());

        boolean allowed = instance.isAllowed(CRAWLER_AGENT, RESOURCE_URI);
        assertThat(allowed, equalTo(true));
    }
    @Test
    public void givenLoaderProducesMatchingGroup_butNotMatchingPath_whenIsAllowed_thenReturnsTrue() throws Exception {
        when(loader.load(any(URI.class))).thenReturn(SINGLE_GROUP_ROBOTS);
        when(matcherUtils.getMostSpecificMatchingGroup(anyCollection(), anyString())).thenReturn(Optional.of(GROUP));
        when(matcherUtils.getMostSpecificMatch(anyCollection(), anyString())).thenReturn(Optional.absent());

        boolean allowed = instance.isAllowed(CRAWLER_AGENT, RESOURCE_URI);
        assertThat(allowed, equalTo(true));
    }


    @Test
    public void givenLoaderProducesMatchingGroup_andSingleMatchingPath_andFieldIsAllow_whenIsAllowed_thenReturnsTrue() throws Exception {
        when(loader.load(any(URI.class))).thenReturn(SINGLE_GROUP_ROBOTS);
        when(matcherUtils.getMostSpecificMatchingGroup(anyCollection(), anyString())).thenReturn(Optional.of(GROUP));
        when(matcherUtils.getMostSpecificMatch(anyCollection(), anyString())).thenReturn(Optional.of(ALLOW_PATH_DIRECTIVE));

        boolean allowed = instance.isAllowed(CRAWLER_AGENT, RESOURCE_URI);
        assertThat(allowed, equalTo(true));
    }

    @Test
    public void givenLoaderProducesMatchingGroup_andSingleMatchingPath_andFieldIsDisallow_whenIsAllowed_thenReturnsTrue() throws Exception {
        when(loader.load(any(URI.class))).thenReturn(SINGLE_GROUP_ROBOTS);
        when(matcherUtils.getMostSpecificMatchingGroup(anyCollection(), anyString())).thenReturn(Optional.of(GROUP));
        when(matcherUtils.getMostSpecificMatch(anyCollection(), anyString())).thenReturn(Optional.of(DISALLOW_PATH_DIRECTIVE));

        boolean allowed = instance.isAllowed(CRAWLER_AGENT, RESOURCE_URI);
        assertThat(allowed, equalTo(false));
    }

}
