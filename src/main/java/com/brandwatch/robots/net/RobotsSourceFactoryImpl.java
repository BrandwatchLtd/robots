package com.brandwatch.robots.net;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

public class RobotsSourceFactoryImpl implements RobotsSourceFactory {

    private final Logger log = LoggerFactory.getLogger(RobotsSourceFactoryImpl.class);

    @Override
    public RobotsSource createFor(URI resourceUri) {
        log.debug("Resolving robot.txt URL for resource: {}", resourceUri);
        try {
            final URI robotsUri = new RobotsURIBuilder()
                    .fromUri(resourceUri)
                    .build();
            log.debug("Resolved robot URI for resource {} to: {}", resourceUri, robotsUri);
            return new RobotsSource(robotsUri);
        } catch (RuntimeException ex) {
            log.error("Failed to resolve robots.txt URL for resource {}, due to exception: {}", resourceUri, ex);
            throw ex;
        }
    }

}
