package com.brandwatch.robots;

import com.brandwatch.robots.domain.Robots;
import com.brandwatch.robots.net.CharSourceSupplier;
import com.brandwatch.robots.net.LoggingReader;
import com.brandwatch.robots.parser.ParseException;
import com.brandwatch.robots.parser.RobotsParser;
import com.brandwatch.robots.util.LogLevel;
import com.google.common.io.CharSource;
import com.google.common.io.Closer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeoutException;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.text.MessageFormat.format;

final class RobotsLoaderImpl implements RobotsLoader {

    private static final Logger log = LoggerFactory.getLogger(RobotsLoaderImpl.class);
    @Nonnull
    private final RobotsFactory factory;
    @Nonnull
    private final CharSourceSupplier charSourceSupplier;

    public RobotsLoaderImpl(@Nonnull RobotsFactory factory,
                            @Nonnull CharSourceSupplier charSourceSupplier) {
        this.charSourceSupplier = charSourceSupplier;
        this.factory = checkNotNull(factory, "factory is null");
    }

    @Nonnull
    @Override
    public Robots load(@Nonnull URI robotsResource) {
        checkNotNull(robotsResource, "robotsResource");
        log.debug("Loading: {}", robotsResource);
        return load(robotsResource, charSourceSupplier.get(robotsResource));
    }

    @Nonnull
    private Robots load(@Nonnull URI robotsResource, @Nonnull CharSource robotsData) {
        try {
            final Closer closer = Closer.create();
            try {

                final Reader reader;
                try {
                    reader = closer.register(robotsData.openBufferedStream());
                } catch (TemporaryAllow e) {
                    return fullAllow(robotsResource, e.getMessage());
                } catch (TemporaryDisallow e) {
                    return fullDisallow(robotsResource, e.getMessage());
                }
                return conditionalAllow(robotsResource, reader);
            } catch (Throwable t) {
                throw closer.rethrow(t);
            } finally {
                closer.close();
            }
        } catch (IOException e) {
            if(e.getCause() instanceof TimeoutException) {
                return fullAllow(robotsResource, format("Timeout waiting for response."));
            } else {
                log.debug("Caught IO exception", e);
                return fullAllow(robotsResource, format("IO exception: \"{0}\"", e.getMessage()));
            }
        }
    }

    @Nonnull
    private Robots conditionalAllow(@Nonnull URI robotsResource, @Nonnull Reader robotsData)
            throws IOException {
        log.debug("Conditional allow; parsing contents of {}", robotsResource);

        final Reader reader = new LoggingReader(robotsData, this.getClass(), LogLevel.TRACE);
        final RobotsParser parser = new RobotsParser(reader);
        final RobotsBuildingParseHandler handler = factory.createRobotsBuildingHandler();

        try {
            parser.parse(handler);
            return handler.get();
        } catch (ParseException e) {
            return fullAllow(robotsResource, format("Caught parsing exception: \"{0}\"", e));
        }
    }

    @Nonnull
    private Robots fullAllow(@Nonnull URI robotsResource, @Nonnull String reason, @Nonnull Object... args) {
        log.info("Allowing entire site: {}; {}", site(robotsResource), format(reason, args));
        return factory.createAllowAllRobots();
    }

    @Nonnull
    private Robots fullDisallow(@Nonnull URI robotsResource, @Nonnull String reason, @Nonnull Object... args) {
        log.info("Disallowing entire site: {}; {}", site(robotsResource), format(reason, args));
        return factory.createDisallowAllRobots();
    }

    static URI site(URI resource) {
        try {
            return new URI(resource.getScheme(), resource.getUserInfo(), resource.getHost(),
                    resource.getPort(), null, null, null);
        } catch (URISyntaxException e) {
            throw new AssertionError(e);
        }
    }

}
