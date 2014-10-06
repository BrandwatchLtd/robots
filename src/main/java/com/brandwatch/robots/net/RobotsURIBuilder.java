package com.brandwatch.robots.net;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

final class RobotsURIBuilder {

    @Nonnull
    private static final String PATH = "/robots.txt";

    @Nonnull
    private Optional<String> scheme = Optional.absent();

    @Nonnull
    private Optional<String> userInfo = Optional.absent();

    @Nonnull
    private Optional<String> host = Optional.absent();

    @Nonnull
    private Optional<Integer> port = Optional.absent();

    @Nonnull
    public RobotsURIBuilder withScheme(@Nonnull String scheme) {
        checkNotNull(scheme, "scheme is null");
        this.scheme = Optional.of(scheme);
        return this;
    }

    @Nonnull
    public RobotsURIBuilder withUserInfo(@Nonnull String userInfo) {
        checkNotNull(userInfo, "userInfo is null");
        this.userInfo = Optional.of(userInfo);
        return this;
    }

    @Nonnull
    public RobotsURIBuilder withHost(@Nonnull String host) {
        checkNotNull(host, "host is null");
        this.host = Optional.of(host);
        return this;
    }

    @Nonnull
    public RobotsURIBuilder withPort(int port) {
        checkArgument(port > 0, "port is negative");
        this.port = Optional.of(port);
        return this;
    }

    @Nonnull
    public RobotsURIBuilder copyFrom(@Nonnull URI uri) {
        checkNotNull(uri, "uri is null");
        return from(Optional.fromNullable(uri.getScheme()),
                Optional.fromNullable(uri.getUserInfo()),
                uri.getHost(),
                Optional.fromNullable(uri.getPort()));
    }

    @Nonnull
    public RobotsURIBuilder copyFrom(@Nonnull URL uri) {
        checkNotNull(uri, "uri is null");
        return from(Optional.fromNullable(uri.getProtocol()),
                Optional.fromNullable(uri.getUserInfo()),
                uri.getHost(),
                Optional.fromNullable(uri.getPort()));
    }

    @Nonnull
    private RobotsURIBuilder from(@Nonnull Optional<String> scheme,
                                  @Nonnull Optional<String> userInfo,
                                  @Nonnull String host,
                                  @Nonnull Optional<Integer> port) {
        withHost(host);
        if (port.isPresent()) {
            withPort(port.get());
        }
        if (scheme.isPresent()) {
            withScheme(scheme.get());
        }
        if (userInfo.isPresent()) {
            withUserInfo(userInfo.get());
        }
        return this;
    }

    @Nonnull
    public URI build() {
        if (!host.isPresent()) {
            throw new IllegalStateException("Required field 'host' is not set.");
        }
        if (!scheme.isPresent()) {
            setDefaultSchema();
        }
        if (!port.isPresent()) {
            setDefaultPort();
        }
        try {
            return new URI(scheme.orNull(), userInfo.orNull(), host.orNull(), port.orNull(), PATH, null, null);
        } catch (URISyntaxException e) {
            throw new AssertionError(e);
        }
    }

    private void setDefaultSchema() {
        if (port.isPresent() && Defaults.SCHEMA_FOR_PORT.containsKey(port.get())) {
            scheme = Optional.of(Defaults.SCHEMA_FOR_PORT.get(port.get()));
        } else {
            scheme = Optional.of(Defaults.SCHEMA);
        }
    }

    private void setDefaultPort() {
        if (scheme.isPresent() && Defaults.PORT_FOR_SCHEMA.containsKey(scheme.get().toLowerCase())) {
            port = Optional.of(Defaults.PORT_FOR_SCHEMA.get(scheme.get().toLowerCase()));
        }
        port = Optional.of(Defaults.PORT);
    }

    @Immutable
    private static final class Defaults {

        @Nonnull
        private static final int PORT = 80;

        @Nonnull
        private static final String SCHEMA = "http";

        @Nonnull
        private static final Map<String, Integer> PORT_FOR_SCHEMA;

        @Nonnull
        private static final Map<Integer, String> SCHEMA_FOR_PORT;

        static {
            SCHEMA_FOR_PORT = ImmutableMap.<Integer, String>builder()
                    .put(80, "http")
                    .put(443, "https")
                    .put(20, "ftp")
                    .put(21, "ftp")
                    .put(989, "ftps")
                    .put(990, "ftps")
                    .build();
            PORT_FOR_SCHEMA = ImmutableMap.<String, Integer>builder()
                    .put("http", 80)
                    .put("https", 443)
                    .put("ftp", 20)
                    .put("ftps", 989)
                    .build();
        }

    }
}
