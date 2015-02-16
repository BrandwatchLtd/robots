package com.brandwatch.robots;

import com.brandwatch.robots.domain.AgentDirective;
import com.brandwatch.robots.domain.Group;
import com.brandwatch.robots.domain.PathDirective;
import com.brandwatch.robots.domain.Robots;
import com.brandwatch.robots.matching.EverythingMatcher;
import com.brandwatch.robots.matching.ExpressionCompiler;
import com.brandwatch.robots.matching.ExpressionCompilerBuilder;
import com.brandwatch.robots.matching.MatcherUtils;
import com.brandwatch.robots.matching.MatcherUtilsImpl;
import com.brandwatch.robots.net.CharSourceSupplier;
import com.brandwatch.robots.net.CharSourceSupplierHttpClientImpl;
import com.brandwatch.robots.net.LoggingClientFilter;
import com.brandwatch.robots.util.LogLevel;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.net.URI;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkNotNull;

public class RobotsFactory {

    private static final Logger log = LoggerFactory.getLogger(RobotsFactory.class);

    private final RobotsUtilities utilities = new RobotsUtilities();
    private final MatcherUtils matcherUtils = new MatcherUtilsImpl();

    @Nonnull
    private final RobotsConfig config;

    public RobotsFactory(@Nonnull RobotsConfig config) {
        this.config = checkNotNull(config, "config is null");
        log.debug("Initializing factory with config: {}", config);
    }

    @Nonnull
    public Robots createAllowAllRobots() {
        return createConstantRobots(PathDirective.Field.allow);
    }

    @Nonnull
    public Robots createDisallowAllRobots() {
        return createConstantRobots(PathDirective.Field.disallow);
    }

    @Nonnull
    private Robots createConstantRobots(@Nonnull PathDirective.Field permission) {
        checkNotNull(permission, "permission");
        return new Robots.Builder()
                .withGroup(new Group.Builder()
                        .withDirective(new AgentDirective("*", new EverythingMatcher<String>()))
                        .withDirective(new PathDirective(permission, "/*", new EverythingMatcher<String>()))
                        .build())
                .build();
    }

    @Nonnull
    public RobotsUtilities getUtilities() {
        return utilities;
    }

    @Nonnull
    public RobotsLoader createLoader() {
        return new RobotsLoaderCachedImpl(
                new RobotsLoaderImpl(this),
                createCache());
    }

    @Nonnull
    public CharSourceSupplier createCharSourceSupplier() {
        return new CharSourceSupplierHttpClientImpl(config, createClient());
    }

    @Nonnull
    public ExpressionCompiler createPathExpressionCompiler() {
        return new ExpressionCompilerBuilder()
                .withLeftBoundaryMatching(true)
                .withExpressionPreprocessor(new MissingPrefixFixingFunction())
                .build();
    }

    @Nonnull
    public ExpressionCompiler createAgentExpressionCompiler() {
        return new ExpressionCompilerBuilder()
                .withCaseSensitivity(false)
                .build();
    }

    @Nonnull
    public Cache<URI, Robots> createCache() {

        log.debug("Initializing cache (maxSize: {}, expires after: {} hours)",
                config.getCacheMaxSizeRecords(), config.getCacheExpiresHours());

        return CacheBuilder.newBuilder()
                .maximumSize(config.getCacheMaxSizeRecords())
                .expireAfterWrite(config.getCacheExpiresHours(), TimeUnit.HOURS)
                .recordStats()
                .build();
    }

    @Nonnull
    public RobotsService createService() {
        RobotsServiceImpl service = new RobotsServiceImpl(
                createLoader(), getUtilities(), getMatcherUtils());
        return service;
    }

    @Nonnull
    public RobotsBuildingParseHandler createRobotsBuildingHandler() {
        return new RobotsBuildingParseHandler(
                createPathExpressionCompiler(),
                createAgentExpressionCompiler());
    }

    @Nonnull
    public Client createClient() {
        return ClientBuilder
                .newClient(new ClientConfig().register(ClientProperties.READ_TIMEOUT,
                        config.getReadTimeoutMillis()))
                .register(new LoggingClientFilter(this.getClass(), LogLevel.TRACE));
    }

    @Nonnull
    public MatcherUtils getMatcherUtils() {
        return matcherUtils;
    }
}
