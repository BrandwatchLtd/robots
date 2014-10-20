package com.brandwatch.robots.net;

import com.google.common.io.CharSource;

import javax.annotation.Nonnull;
import java.io.Closeable;
import java.io.IOException;
import java.net.URI;

public interface CharSourceSupplier extends Closeable {

    @Nonnull
    CharSource get(@Nonnull URI resource);

    @Override
    void close() throws IOException;

}
