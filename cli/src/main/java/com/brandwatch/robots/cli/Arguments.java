package com.brandwatch.robots.cli;

/*
 * #%L
 * Robots (command-line interface)
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

import com.beust.jcommander.Parameter;
import com.beust.jcommander.validators.PositiveInteger;
import com.brandwatch.robots.RobotsConfig;
import com.brandwatch.robots.cli.converters.CharsetConverter;
import com.brandwatch.robots.cli.converters.URIConverter;
import com.brandwatch.robots.cli.validators.AbsoluteURIValidator;
import com.brandwatch.robots.cli.validators.NonEmptyStringValidator;
import com.google.common.base.Charsets;
import com.google.common.collect.Sets;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public class Arguments {

    @Parameter(
            names = {"--help", "-h"},
            description = "Display this helpful message.",
            help = true
    )
    private boolean helpRequested = false;

    @Nonnegative
    @Parameter(
            names = {"--maxFileSizeBytes", "-s"},
            description = "Download size limit. robots.txt retrieval will give up beyond this point.",
            validateWith = PositiveInteger.class
    )
    private int maxFileSizeBytes = 192 * 1024;

    @Nonnegative
    @Parameter(
            names = {"--maxRedirectHops", "-r"},
            description = "Number of HTTP 3XX (redirection) responses to follow before giving up.",
            validateWith = PositiveInteger.class
    )
    private int maxRedirectHops = 5;

    @Nonnull
    @Parameter(
            names = {"--excludedDomains", "-x"},
            description = "Set of domains to always consider denied. Will consider, for each domain supplied, both exact matches of and any subdomain thereof to be excluded. Values are comma separated."
    )
    private List<String> excludedDomains = newArrayList();

    @Nonnull
    @Parameter(
            names = {"--defaultCharset", "--charset", "-c"},
            description = "Preferred character encoding for reading robots.txt. Used when server doesn't specify encoding.",
            converter = CharsetConverter.class
    )
    private Charset defaultCharset = Charsets.UTF_8;

    @Nonnull
    @Parameter(
            names = {"--agent", "-a"},
            description = "User agent identifier. Sent to the host on retrieval of robots.txt, and also used for directive group matching.",
            validateWith = NonEmptyStringValidator.class
    )
    private String agent = "<unnamed-agent>";

    @Nonnull
    @Parameter(
            required = true,
            description = "RESOURCES",
            converter = URIConverter.class,
            validateWith = AbsoluteURIValidator.class
    )
    private List<URI> resources = newArrayList();

    @Nonnegative
    @Parameter(
            names = {"--readTimeout", "-t"},
            description = "Time in millis before the client times out while downloading a response payload.",
            validateWith = PositiveInteger.class
    )
    private int readTimeoutMillis = 30000;

    @Nonnegative
    public int getMaxFileSizeBytes() {
        return maxFileSizeBytes;
    }

    @Nonnegative
    public int getMaxRedirectHops() {
        return maxRedirectHops;
    }

    @Nonnull
    public Charset getDefaultCharset() {
        return defaultCharset;
    }

    @Nonnull
    public List<URI> getResources() {
        return resources;
    }

    @Nonnull
    public String getAgent() {
        return agent;
    }

    public boolean isHelpRequested() {
        return helpRequested;
    }

    @Nonnegative
    public int getReadTimeoutMillis() {
        return readTimeoutMillis;
    }

    @Nonnull
    public RobotsConfig buildRobotsConfig() {
        final RobotsConfig config = new RobotsConfig();
        config.setMaxFileSizeBytes(getMaxFileSizeBytes());
        config.setMaxRedirectHops(getMaxRedirectHops());
        config.setDefaultCharset(getDefaultCharset());
        config.setReadTimeoutMillis(getReadTimeoutMillis());
        config.setExcludedDomains(Sets.newHashSet(excludedDomains));
        config.setUserAgent(agent);
        return config;
    }

}
