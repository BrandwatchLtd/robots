package com.brandwatch.robots.net;

import com.google.common.io.CharSource;

import javax.annotation.Nonnull;
import java.net.URI;


public interface RobotsCharSourceFactory {

    @Nonnull
    CharSource createFor(URI url);

}
