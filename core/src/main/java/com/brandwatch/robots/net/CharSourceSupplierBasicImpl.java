package com.brandwatch.robots.net;

/*
 * #%L
 * Robots (core)
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

    @Override
    public void close() throws IOException {
    }

}
