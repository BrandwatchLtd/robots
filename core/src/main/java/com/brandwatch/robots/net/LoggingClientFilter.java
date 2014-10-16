package com.brandwatch.robots.net;

import com.brandwatch.robots.util.LogLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

public class LoggingClientFilter implements ClientRequestFilter, ClientResponseFilter {

    @Nonnull
    private final Logger logger;
    @Nonnull
    private final LogLevel level;

    public LoggingClientFilter(@Nonnull Class<?> context, @Nonnull LogLevel level) {
        logger = LoggerFactory.getLogger(checkNotNull(context, "context"));
        this.level = checkNotNull(level, "level is null");
    }

    public LoggingClientFilter(@Nonnull Class<?> context) {
        this(context, LogLevel.DEBUG);
    }

    public LoggingClientFilter(@Nonnull LogLevel level) {
        this(LoggingClientFilter.class, level);
    }

    public LoggingClientFilter() {
        this(LoggingClientFilter.class, LogLevel.DEBUG);
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        level.log(logger, "Request: {}", requestContext);
    }

    @Override
    public void filter(ClientRequestContext requestContext, ClientResponseContext responseContext) throws IOException {
        level.log(logger, "Response: {}", responseContext);
    }
}
