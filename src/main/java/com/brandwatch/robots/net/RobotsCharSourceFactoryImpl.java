package com.brandwatch.robots.net;


import com.google.common.io.CharSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.net.URI;

public class RobotsCharSourceFactoryImpl implements RobotsCharSourceFactory {

    private final Logger log = LoggerFactory.getLogger(RobotsCharSourceFactoryImpl.class);

    @Nonnull
    @Override
    public CharSource createFor(@Nonnull URI resourceUri) {
        log.debug("Resolving robot.txt URL for resource: {}", resourceUri);
        try {
            final URI robotsUri = new RobotsURIBuilder()
                    .fromUri(resourceUri)
                    .build();
            log.debug("Resolved robot URI for resource {} to: {}", resourceUri, robotsUri);
            return new RobotsCharSource(robotsUri);
        } catch (RuntimeException ex) {
            log.error("Failed to resolve robots.txt URL for resource {}, due to exception: {}", resourceUri, ex);
            throw ex;
        }
    }

}
