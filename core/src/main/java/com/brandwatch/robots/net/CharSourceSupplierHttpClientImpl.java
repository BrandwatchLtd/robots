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
import com.google.common.io.ByteStreams;
import com.google.common.io.CharSource;

import javax.annotation.Nonnull;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.google.common.base.Preconditions.checkNotNull;

public class CharSourceSupplierHttpClientImpl implements CharSourceSupplier {

    private final Client client;
    private final RobotsConfig config;

    public CharSourceSupplierHttpClientImpl(@Nonnull RobotsConfig config, Client client) {
        this.config = checkNotNull(config, "config is null");
        this.client = checkNotNull(client, "client is null");
    }

    @Override
    public CharSource get(final URI resource) {
        checkNotNull(resource, "resource is null");
        return new CharSource() {
            @Override
            public Reader openStream() throws IOException {
                final Response response;
                try {
                    response = getResponse(resource);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (TimeoutException e) {
                    throw new IOException(e);
                }
                return conditionalAllow(response);
            }
        };
    }

    @Override
    public void close() throws IOException {
        client.close();
    }

    @Nonnull
    private Response getResponse(@Nonnull final URI resource) throws InterruptedException, TimeoutException {
        checkNotNull(resource, "resource");
        Future<Response> future = client.target(resource)
                .request()
                .accept(MediaType.TEXT_PLAIN_TYPE.withCharset(config.getDefaultCharset().displayName()))
                .header(HttpHeaders.USER_AGENT, config.getUserAgent())
                .buildGet()
                .submit();
        try {
            return future.get(config.getRequestTimeoutMillis(), TimeUnit.MILLISECONDS);
        } catch (ExecutionException e) {
            throw e.getCause() instanceof RuntimeException
                    ? (RuntimeException) e.getCause()
                    : new RuntimeException(e.getCause());
        }
    }

    @Nonnull
    private Reader conditionalAllow(@Nonnull final Response response) {
        return new InputStreamReader(
                ByteStreams.limit(
                        new BufferedInputStream((InputStream) response.getEntity()),
                        config.getMaxFileSizeBytes()),
                config.getDefaultCharset());
    }

}
