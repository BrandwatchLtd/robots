package com.brandwatch.robots;

import com.brandwatch.robots.domain.Robots;
import com.brandwatch.robots.parser.ParseException;
import com.brandwatch.robots.parser.RobotsParser;
import com.google.common.io.Closer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;

import static com.google.common.base.Preconditions.checkNotNull;

final class RobotsLoaderDefaultImpl implements RobotsLoader {

    private static final Logger log = LoggerFactory.getLogger(RobotsLoaderDefaultImpl.class);

    private final RobotsConfig config;

    public RobotsLoaderDefaultImpl(@Nonnull RobotsConfig config) {
        this.config = checkNotNull(config, "config is null");
    }

    @Nonnull
    @Override
    public Robots load(@Nonnull URI robotsResource) throws IOException {
        checkNotNull(robotsResource, "robotsResource");

        final RobotsBuildingParseHandler handler = config.getRobotsBuildingHandler();
        final Closer closer = Closer.create();

        try {
            try {
                log.debug("Downloading and parsing robot.txt: {}", robotsResource);

                final Reader reader = closer.register(
                        config.getUtilities()
                                .createCharSourceFor(robotsResource, config.getMaxFileSizeBytes())
                                .openBufferedStream());

                final RobotsParser parser = new RobotsParser(reader);
                parser.parse(handler);

            } catch (ParseException e) {
                log.warn("Failed to parse robots.txt: ", e);
                throw closer.rethrow(e);
            } catch (Throwable t) {
                log.warn("Failed to download robots.txt: ", t);
                throw closer.rethrow(t);
            } finally {
                closer.close();
            }
        } catch (IOException ex) {
            log.warn("Failed to download robots.txt: ", ex);
            throw ex;
        }

        return handler.get();
    }

}
