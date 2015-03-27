package com.brandwatch.robots;

/*
 * #%L
 * Robots (core)
 * %%
 * Copyright (C) 2015 Brandwatch
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
