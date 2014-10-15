package com.brandwatch.robots.net;

import com.google.common.io.CharSource;

import javax.annotation.Nonnull;
import java.net.URI;

public interface CharSourceSupplier {

    @Nonnull
    CharSource get(@Nonnull URI resource);
}
