package com.brandwatch.robots;

import com.brandwatch.robots.domain.Robots;

import javax.annotation.Nonnull;
import java.io.Closeable;
import java.net.URI;

public interface RobotsLoader extends Closeable {

    @Nonnull
    Robots load(@Nonnull URI robotsResource) throws Exception;

}
