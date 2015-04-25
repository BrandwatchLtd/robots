package com.brandwatch.robots.net;

import com.google.common.collect.ImmutableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Sets.union;

public class FollowRedirectsFilter implements ClientResponseFilter {

    private static final String VISITED_URIS_KEY = "com.brandwatch.robots.net.FollowRedirectFilter.visited";

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Nonnegative
    private final int maxRedirectHops;

    public FollowRedirectsFilter(@Nonnegative int maxRedirectHops) {
        checkArgument(maxRedirectHops >= 0, "maxRedirectHops is negative");
        this.maxRedirectHops = maxRedirectHops;
    }

    @Override
    public void filter(@Nonnull ClientRequestContext requestContext, @Nonnull ClientResponseContext responseContext) throws IOException {
        if (isValidRedirect(requestContext, responseContext)) {
            final URI location = getAbsoluteRedirectLocation(requestContext, responseContext);
            if (isRedirectWithinLimits(requestContext, location)) {
                followRedirect(requestContext, responseContext, location);
            }
        }
    }

    private boolean isValidRedirect(ClientRequestContext requestContext, ClientResponseContext responseContext) {

        if (responseContext.getStatusInfo().getFamily() != Response.Status.Family.REDIRECTION) {
            log.debug("No redirect to follow:", requestContext.getUri());
            return false;
        }

        if (responseContext.getLocation() == null) {
            log.warn("Missing redirect location in response for: {}", requestContext.getUri());
            return false;
        }

        return true;
    }

    private boolean isRedirectWithinLimits(ClientRequestContext requestContext, URI location) {

        final Set<URI> visited = getVisitedWithCurrentUri(requestContext);

        if (visited.contains(location)) {
            log.warn("Detected redirect cycle: {}", visited);
            return false;
        }

        if (visited.size() > maxRedirectHops) {
            log.warn("Reached max hops following redirects: {}", visited);
            return false;
        }

        return true;
    }

    private void followRedirect(ClientRequestContext requestContext, ClientResponseContext responseContext, URI location) throws IOException {
        log.debug("Following redirect: {} => {}", requestContext.getUri(), responseContext.getLocation());

        populateResponseContext(
                responseContext,
                doRedirect(requestContext, location)
        );
    }

    private URI getAbsoluteRedirectLocation(ClientRequestContext requestContext, ClientResponseContext responseContext) {
        return requestContext.getUri().resolve(responseContext.getLocation());
    }

    private Response doRedirect(ClientRequestContext requestContext, URI redirectLocation) {
        return requestContext.getClient()
                .target(redirectLocation)
                .request()
                .accept(getAcceptedMediaTypes(requestContext))
                .property(VISITED_URIS_KEY, getVisitedWithCurrentUri(requestContext))
                .build(requestContext.getMethod())
                .invoke();
    }

    private Set<URI> getVisitedWithCurrentUri(ClientRequestContext requestContext) {
        return union(getVisited(requestContext), ImmutableSet.of(requestContext.getUri()));
    }

    private Set<URI> getVisited(ClientRequestContext requestContext) {
        Object value = requestContext.getProperty(VISITED_URIS_KEY);
        if (value == null) {
            return ImmutableSet.of(requestContext.getUri());
        }
        return (Set<URI>) value;
    }

    private MediaType[] getAcceptedMediaTypes(ClientRequestContext requestContext) {
        final List<MediaType> mediaTypeList = requestContext.getAcceptableMediaTypes();
        return mediaTypeList.toArray(new MediaType[mediaTypeList.size()]);
    }

    private void populateResponseContext(ClientResponseContext responseContext, Response response) {
        responseContext.setEntityStream((InputStream) response.getEntity());
        responseContext.setStatusInfo(response.getStatusInfo());
        responseContext.setStatus(response.getStatus());
    }
}