package com.brandwatch.robots;

import com.brandwatch.robots.domain.Group;
import com.brandwatch.robots.domain.PathDirective;
import com.brandwatch.robots.domain.Robots;
import com.brandwatch.robots.matching.MatcherUtils;
import com.google.common.base.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.net.URI;

import static com.google.common.base.Preconditions.checkNotNull;

class RobotsServiceImpl implements RobotsService {

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
    public boolean isAllowed(@Nonnull String crawlerAgentString, @Nonnull URI resourceUri) {
        checkNotNull(crawlerAgentString, "crawlerAgentString is null");
        checkNotNull(resourceUri, "resourceUri is null");

        log.debug("Resolving robots URL for: {}", resourceUri);
        final URI robotsUri = utilities.getRobotsURIForResource(resourceUri);
        log.debug("Resolved robots URI to: {}", robotsUri);

        final Robots robots;
        try {
            robots = loader.load(robotsUri);
        } catch (Exception e) {
            log.debug("Download failure {}", e.getMessage());
            return allow(resourceUri);
        }

        if (robots.getGroups().isEmpty()) {
            log.debug("No agent groups found", resourceUri);
            return allow(resourceUri);
        }

        final Optional<Group> group = matcherUtils.getMostSpecificMatchingGroup(robots.getGroups(), crawlerAgentString);

        if (group.isPresent()) {
            if(log.isDebugEnabled()) {
                log.debug("Matched user-agent group: {}",
                        matcherUtils.getMostSpecificMatch(group.get(), crawlerAgentString).get().getValue());
            }
        } else {
            log.debug("No user-agent group matched");
            return allow(resourceUri);
        }

        final Optional<PathDirective> bestMatch = matcherUtils.getMostSpecificMatch(
                group.get().getDirectives(PathDirective.class),
                resourceUri.getPath());

        if (!bestMatch.isPresent()) {
            log.debug("No matching path directive");
            return allow(resourceUri);
        } else {
            final PathDirective directive = bestMatch.get();
            log.debug("Matched path directive {}:{}",  directive.getField(),  directive.getValue());
            return directive.isAllowed() ? allow(resourceUri) : disallow(resourceUri);
        }
    }

    private boolean allow(URI resourceUri) {
        log.debug("Allowing: {}", resourceUri);
        return true;
    }

    private boolean disallow(URI resourceUri) {
        log.debug("Disallowing: {}", resourceUri);
        return false;
    }

}
