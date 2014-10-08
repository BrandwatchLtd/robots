package com.brandwatch.robots;

import com.brandwatch.robots.domain.Robots;
import com.google.common.io.CharSource;

import javax.annotation.Nonnull;
import java.net.URI;

public interface RobotsDownloader {

    @Nonnull
    Robots load(URI robotsResource);

}
