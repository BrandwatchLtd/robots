package com.brandwatch.robots.net;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import java.net.IDN;
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
        checkArgument(!scheme.isEmpty(), "empty scheme");
        this.scheme = Optional.of(scheme);
        return this;
    }

    @Nonnull
    public RobotsURIBuilder withUserInfo(@Nonnull String userInfo) {
        checkNotNull(userInfo, "userInfo is null");
        checkArgument(!userInfo.isEmpty(), "empty userInfo");
        this.userInfo = Optional.of(userInfo);
        return this;
    }

    @Nonnull
    public RobotsURIBuilder withHost(@Nonnull String host) {
        checkNotNull(host, "host is null");
        checkArgument(!host.isEmpty(), "host userInfo");
        this.host = Optional.of(IDN.toASCII(host));
        return this;
    }

    @Nonnull
    public RobotsURIBuilder withPort(int port) {
        checkArgument(port >= 0, "port is negative");
        checkArgument(port <= 65535, "port too large");
        this.port = Optional.of(port);
        return this;
    }

    @Nonnull
    public RobotsURIBuilder fromUriString(@Nonnull String uriString) {
        return fromUri(URI.create(IDN.toASCII(uriString)));
    }

    @Nonnull
    public RobotsURIBuilder fromUri(@Nonnull URI uri) {
        checkNotNull(uri, "uri is null");
        return from(uri.getScheme(),
                Optional.fromNullable(uri.getUserInfo()),
                uri.getHost(),
                uri.getPort() >= 0 ? Optional.of(uri.getPort()) : Optional.<Integer>absent());
    }

    @Nonnull
    public RobotsURIBuilder fromURL(@Nonnull URL url) {
        checkNotNull(url, "uri is null");
        return from(url.getProtocol(),
                Optional.fromNullable(url.getUserInfo()),
                url.getHost(),
                url.getPort() >= 0 ? Optional.of(url.getPort()) : Optional.<Integer>absent());
    }

    @Nonnull
    private RobotsURIBuilder from(@Nonnull String scheme,
                                  @Nonnull Optional<String> userInfo,
                                  @Nonnull String host,
                                  @Nonnull Optional<Integer> port) {
        checkNotNull(scheme, "scheme");
        checkNotNull(host, "host");
        withScheme(scheme);
        withHost(host);
        if (port.isPresent()) {
            withPort(port.get());
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
            throw new IllegalStateException("Required field 'scheme' is not set.");
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

    private void setDefaultPort() {
        if (scheme.isPresent() && Defaults.PORT_FOR_SCHEMA.containsKey(scheme.get().toLowerCase())) {
            port = Optional.of(Defaults.PORT_FOR_SCHEMA.get(scheme.get().toLowerCase()));
        } else {
            port = Optional.of(Defaults.PORT);
        }
    }

    @Immutable
    private static final class Defaults {

        @Nonnull
        private static final int PORT = 80;

        @Nonnull
        private static final Map<String, Integer> PORT_FOR_SCHEMA;

        static {
            PORT_FOR_SCHEMA = ImmutableMap.<String, Integer>builder()
                    .put("http", 80)
                    .put("https", 443)
                    .put("ftp", 21)
                    .put("ftps", 989)
                    .build();
        }

    }
}
