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


    @Nonnull
    public String getResourceLocalComponents(@Nonnull URI resourceUri) {
        checkNotNull(resourceUri, "resourceUri");

        StringBuilder builder = new StringBuilder();
        builder.append(resourceUri.getPath());
        if (resourceUri.getQuery() != null) {
            builder.append('?').append(resourceUri.getQuery());
        }
        if (resourceUri.getFragment() != null) {
            builder.append('#').append(resourceUri.getFragment());
        }
        return builder.toString();
    }
}
