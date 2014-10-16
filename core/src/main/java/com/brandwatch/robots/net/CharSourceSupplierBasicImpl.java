package com.brandwatch.robots.net;

import com.brandwatch.robots.RobotsConfig;
import com.google.common.io.CharSource;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.io.ByteStreams.limit;
import static com.google.common.io.Resources.asByteSource;

public final class CharSourceSupplierBasicImpl implements CharSourceSupplier {

    @Nonnull
    private RobotsConfig config;

    public CharSourceSupplierBasicImpl(RobotsConfig config) {
        this.config = checkNotNull(config, "config is null");
    }

    @Nonnull
    @Override
    public CharSource get(@Nonnull final URI resource) {
        checkNotNull(resource, "resource is null");
        return new CharSource() {
            @Nonnull
            @Override
            public Reader openStream() throws IOException {
                return new InputStreamReader(
                        limit(
                                asByteSource(resource.toURL()).openBufferedStream(),
                                config.getMaxFileSizeBytes()),
                        config.getDefaultCharset());
            }
        };
    }
}
