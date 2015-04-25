package com.brandwatch.robots.net;

/*
 * #%L
 * Robots (core)
 * %%
 * Copyright (C) 2014 - 2015 Brandwatch
 * %%
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. Neither the name of the Brandwatch nor the names of its contributors
 *    may be used to endorse or promote products derived from this software without
 *    specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */

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