package com.brandwatch.robots;

import com.brandwatch.robots.domain.Robots;
import com.brandwatch.robots.parser.ParseException;
import com.brandwatch.robots.parser.RobotsTxtParser;
import com.google.common.io.CharSource;
import com.google.common.io.Closer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.Reader;

import static com.google.common.base.Preconditions.checkNotNull;

public class RobotsDownloaderImpl implements RobotsDownloader {

    private static final Logger log = LoggerFactory.getLogger(RobotsDownloaderImpl.class);

    @Nonnull
    @Override
    public Robots load(@Nonnull CharSource robotsSource) {
        checkNotNull(robotsSource, "robotsSource");

        final RobotsBuildingHandler handler = new RobotsBuildingHandler();
        final Closer closer = Closer.create();

        try {
            try {
                log.debug("Downloading and parsing robot.txt: {}", robotsSource);

                final Reader reader = closer.register(robotsSource.openBufferedStream());
                final RobotsTxtParser parser = new RobotsTxtParser(reader);
                parser.parse(handler);

            } catch (ParseException e) {
                log.warn("Failed to parse robots.txt: ", e);
                closer.rethrow(e);
            } catch (Throwable t) {
                log.warn("Failed to download robots.txt: ", t);
                throw closer.rethrow(t);
            } finally {
                closer.close();
            }
        } catch (IOException ex) {
            log.warn("Failed to download robots.txt: ", ex);
            throw new RuntimeException(ex);
        }

        return handler.get();
    }
}
