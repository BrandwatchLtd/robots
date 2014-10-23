package com.brandwatch.robots.cli;

import javax.annotation.Nonnull;
import java.net.URI;

import static com.google.common.base.Preconditions.checkNotNull;

public class Result {

    @Nonnull
    private final URI resource;

    private final boolean allowed;

    public Result(@Nonnull URI resource, boolean allowed) {
        this.resource = checkNotNull(resource, "resource");
        this.allowed = checkNotNull(allowed, "allowed");
    }

    public URI getResource() {
        return resource;
    }

    public boolean isAllowed() {
        return allowed;
    }
}
