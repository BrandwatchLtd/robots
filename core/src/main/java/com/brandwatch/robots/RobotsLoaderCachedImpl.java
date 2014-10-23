package com.brandwatch.robots;

import com.brandwatch.robots.domain.Robots;
import com.google.common.cache.Cache;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.Callable;

import static com.google.common.base.Preconditions.checkNotNull;

final class RobotsLoaderCachedImpl implements RobotsLoader {

    @Nonnull
    private final RobotsLoader delegate;
    @Nonnull
    private final Cache<URI, Robots> cache;

    public RobotsLoaderCachedImpl(
            @Nonnull final RobotsLoader delegate,
            @Nonnull final Cache<URI, Robots> cache) {
        this.delegate = checkNotNull(delegate, "delegate");
        this.cache = checkNotNull(cache, "cache");
    }

    @Nonnull
    @Override
    public Robots load(@Nonnull final URI robotsResource) throws Exception {
        checkNotNull(robotsResource, "robotsResource");
        return cache.get(robotsResource, new Callable<Robots>() {
            @Override
            public Robots call() throws Exception {
                return delegate.load(robotsResource);
            }
        });
    }

    @Override
    public void close() throws IOException {
        delegate.close();
    }
}
