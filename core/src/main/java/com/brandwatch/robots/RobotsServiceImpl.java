package com.brandwatch.robots;

import com.brandwatch.robots.domain.Group;
import com.brandwatch.robots.domain.PathDirective;
import com.brandwatch.robots.domain.Robots;
import com.brandwatch.robots.matching.MatcherUtils;
import com.google.common.base.Optional;
import com.google.common.util.concurrent.AbstractIdleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.net.URI;

import static com.google.common.base.Preconditions.checkNotNull;

class RobotsServiceImpl extends AbstractIdleService implements RobotsService {

    private static final Logger log = LoggerFactory.getLogger(RobotsServiceImpl.class);

    private final RobotsLoader loader;
    private final RobotsUtilities utilities;
    private final MatcherUtils matcherUtils;

    public RobotsServiceImpl(@Nonnull RobotsLoader loader, @Nonnull RobotsUtilities utilities, MatcherUtils matcherUtils) {
        this.matcherUtils = matcherUtils;
        this.loader = checkNotNull(loader, "loader");
        this.utilities = checkNotNull(utilities, "utilities");
    }

    @Override
    protected void startUp() throws Exception {
        log.info("Starting up");
    }

    @Override
    protected void shutDown() throws Exception {
        log.info("Shutting down");
    }

    @Override
    public boolean isAllowed(@Nonnull String crawlerAgentString, @Nonnull URI resourceUri) {
        checkNotNull(crawlerAgentString, "crawlerAgentString is null");
        checkNotNull(resourceUri, "resourceUri is null");

        log.debug("evaluating: {}", resourceUri);

        log.debug("Resolving robots.txt URL for resource: {}", resourceUri);
        final URI robotsUri = utilities.getRobotsURIForResource(resourceUri);
        log.debug("Resolved robot URI for resource {} to: {}", resourceUri, robotsUri);

        final Robots robots;
        try {
            robots = loader.load(robotsUri);
        } catch (Exception e) {
            log.debug("robots.txt download failure; allowing: {}", resourceUri);
            return true;
        }

        if (robots.getGroups().isEmpty()) {
            log.debug("robots.txt contains no agent groups; allowing: {}", resourceUri);
            return true;
        }

        final Optional<Group> group = matcherUtils.getMostSpecificMatchingGroup(robots.getGroups(), crawlerAgentString);

        if (!group.isPresent()) {
            log.debug("No matching groups; allowing: {}", resourceUri);
            return true;
        }

        final Optional<PathDirective> bestMatch = matcherUtils.getMostSpecificMatch(
                group.get().getDirectives(PathDirective.class),
                resourceUri.getPath());

        if (!bestMatch.isPresent()) {
            log.debug("No matching path directive; allowing: {}", resourceUri);
            return true;
        } else {
            final PathDirective directive = bestMatch.get();
            log.debug("Path directive {} matches; {}: {}", directive.getValue(),
                    directive.isAllowed() ? "allowing" : "disallowing", resourceUri);
            return directive.isAllowed();
        }
    }


}
