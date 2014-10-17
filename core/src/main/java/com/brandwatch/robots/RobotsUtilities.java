package com.brandwatch.robots;

import com.brandwatch.robots.net.RobotsURIBuilder;

import javax.annotation.Nonnull;
import java.net.URI;

import static com.google.common.base.Preconditions.checkNotNull;

public class RobotsUtilities {

    @Nonnull
    public URI getRobotsURIForResource(@Nonnull final URI resourceUri) {
        checkNotNull(resourceUri, "resourceUri");
        return new RobotsURIBuilder()
                .fromUri(resourceUri)
                .build();
    }

}
