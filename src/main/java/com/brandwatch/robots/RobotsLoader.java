package com.brandwatch.robots;

import com.brandwatch.robots.domain.Robots;

import javax.annotation.Nonnull;
import java.net.URI;

public interface RobotsLoader {

    @Nonnull
    Robots load(@Nonnull URI robotsResource) throws Exception;

}
