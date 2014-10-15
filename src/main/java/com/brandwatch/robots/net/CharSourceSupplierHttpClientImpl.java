package com.brandwatch.robots.net;

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
import java.io.*;
import java.net.URI;
import java.util.List;

import static com.google.common.base.Optional.*;
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
                return handleResponse(getResponseFollowingRedirects(resource));
            }
        };
    }

    @Nonnull
    private Response getResponseFollowingRedirects(@Nonnull final URI resource) {
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
    private Response getResponse(@Nonnull final URI resource) {
        checkNotNull(resource, "resource");
        return client.target(resource)
                .request()
                .accept(MediaType.TEXT_PLAIN_TYPE.withCharset(config.getDefaultCharset().displayName()))
                .header(HttpHeaders.USER_AGENT, config.getUserAgent())
                .buildGet()
                .invoke();
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
                    return fullDisallow("Response status {0}", info);
            }
        }

        switch (info.getFamily()) {
            case SUCCESSFUL:
                return conditionalAllow(response);
            case INFORMATIONAL:
            case REDIRECTION:
            case CLIENT_ERROR:
            case OTHER:
                return fullAllow("Response status {0}", info);
            case SERVER_ERROR:
                return fullDisallow("Response status {0}", info);
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

}
