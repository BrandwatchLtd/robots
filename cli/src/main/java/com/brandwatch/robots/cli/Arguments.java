package com.brandwatch.robots.cli;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.validators.PositiveInteger;
import com.brandwatch.robots.cli.converters.CharsetConverter;
import com.brandwatch.robots.cli.converters.URIConverter;
import com.brandwatch.robots.cli.validators.AbsoluteURIValidator;
import com.brandwatch.robots.cli.validators.NonEmptyStringValidator;
import com.google.common.base.Charsets;

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
}
