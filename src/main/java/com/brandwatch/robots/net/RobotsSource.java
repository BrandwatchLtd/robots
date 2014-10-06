package com.brandwatch.robots.net;

import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.common.io.CharSource;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;

import static com.google.common.base.Preconditions.checkNotNull;

public class RobotsSource extends CharSource {

    @Nonnull
    private final URI uri;

    public RobotsSource(@Nonnull URI uri) {
        this.uri = checkNotNull(uri);
    }

    public URI getUri() {
        return uri;
    }

    @Override
    public Reader openStream() throws IOException {
        return new InputStreamReader(uri.toURL().openStream(), Charsets.UTF_8);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("uri", uri)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RobotsSource that = (RobotsSource) o;
        return uri.equals(that.uri);
    }

    @Override
    public int hashCode() {
        return uri.hashCode();
    }
}
