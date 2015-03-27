package com.brandwatch.robots.net;

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

import com.brandwatch.robots.RobotsConfig;
import com.brandwatch.robots.TemporaryAllow;
import com.brandwatch.robots.TemporaryDisallow;
import com.google.common.base.Optional;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.google.common.base.Optional.absent;
import static com.google.common.base.Optional.fromNullable;
import static com.google.common.base.Optional.of;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayListWithCapacity;
import static java.text.MessageFormat.format;

public class CharSourceSupplierHttpClientImpl implements CharSourceSupplier {

    private static final Logger log = LoggerFactory.getLogger(CharSourceSupplierHttpClientImpl.class);

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
                    response = getResponseFollowingRedirects(resource);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (TimeoutException e) {
                    throw new IOException(e);
                }

                return handleResponse(response);
            }
        };
    }

    @Override
    public void close() throws IOException {
        client.close();
    }

    @Nonnull
    private Response getResponseFollowingRedirects(@Nonnull final URI resource) throws InterruptedException, TimeoutException {
        final List<URI> visited = newArrayListWithCapacity(config.getMaxRedirectHops() + 1);
        URI location = resource;

        Optional<Response> response = absent();

        while (!response.isPresent()) {

            response = of(getResponse(location));
            visited.add(resource);

            if (response.get().getStatusInfo().getFamily() == Family.REDIRECTION) {
                final Optional<URI> redirect = of(response.get().getLocation());

                if (!redirect.isPresent()) {
                    log.warn("Missing redirect location in response for: {}", location);
                } else if (visited.contains(redirect.get())) {
                    log.warn("Detected redirect cycle: {}", visited);
                } else if (visited.size() > config.getMaxRedirectHops()) {
                    log.warn("Reached max hops following redirects: {}", visited);
                } else {
                    log.debug("Following redirect: {} => {}", location, redirect);
                    location = redirect.get();
                    response.get().close();
                    response = absent();
                }
            }
        }

        return response.get();
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
    private Reader handleResponse(@Nonnull Response response) throws TemporaryAllow, TemporaryDisallow {
        final StatusType info = response.getStatusInfo();

        final Optional<Status> status = fromNullable(Status.fromStatusCode(info.getStatusCode()));
        if (status.isPresent()) {
            switch (status.get()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case PAYMENT_REQUIRED:
                    return fullDisallow(statusMessage(info));
            }
        }

        switch (info.getFamily()) {
            case SUCCESSFUL:
                return conditionalAllow(response);
            case INFORMATIONAL:
            case REDIRECTION:
            case CLIENT_ERROR:
            case OTHER:
                return fullAllow(statusMessage(info));
            case SERVER_ERROR:
                return fullDisallow(statusMessage(info));
        }

        throw new AssertionError("Unknown status family: " + info.getFamily());
    }

    @Nonnull
    private Reader conditionalAllow(@Nonnull final Response response) {
        return new InputStreamReader(
                ByteStreams.limit(
                        new BufferedInputStream((InputStream) response.getEntity()),
                        config.getMaxFileSizeBytes()),
                config.getDefaultCharset());
    }

    @Nonnull
    private Reader fullAllow(@Nonnull String reason, @Nonnull Object... args) throws TemporaryAllow {
        throw new TemporaryAllow(format(reason, args));
    }

    @Nonnull
    private Reader fullDisallow(@Nonnull String reason, @Nonnull Object... args) throws TemporaryDisallow {
        throw new TemporaryDisallow(format(reason, args));
    }

    private static String statusMessage(StatusType info) {
        return (info.getReasonPhrase() != null)
                ? format("response status: {0} \"{1}\"", info.getStatusCode(), info.getReasonPhrase())
                : format("response status: {0}", info.getStatusCode());
    }

}
