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

import javax.annotation.Nonnull;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.Response;

import static java.text.MessageFormat.format;

public class HttpStatusHandler implements ClientResponseFilter {

    private static final int MULTI_STATUS = 207;
    private static final int ALREADY_REPORTED = 208;
    private static final int IM_USED = 226  ;

    @Override
    public void filter(@Nonnull ClientRequestContext requestContext, @Nonnull ClientResponseContext responseContext)
            throws TemporaryAllow, TemporaryDisallow {
        final Response.StatusType statusInfo = responseContext.getStatusInfo();
        handleStatusCode(statusInfo);
        handleStatus(statusInfo);
        handleFamily(statusInfo);
    }

    private void handleStatusCode(@Nonnull Response.StatusType statusInfo) throws TemporaryDisallow {
        switch (statusInfo.getStatusCode()) {
            case MULTI_STATUS:
            case ALREADY_REPORTED:
            case IM_USED:
                throw new TemporaryAllow(createStatusMessage(statusInfo));
        }
    }

    private void handleStatus(@Nonnull Response.StatusType statusInfo) throws TemporaryDisallow {
        final Response.Status status = Response.Status.fromStatusCode(statusInfo.getStatusCode());
        if (status != null) {
            switch (status) {
                case NO_CONTENT:
                case RESET_CONTENT:
                    throw new TemporaryAllow(createStatusMessage(statusInfo));
                case UNAUTHORIZED:
                case FORBIDDEN:
                case PAYMENT_REQUIRED:
                    throw new TemporaryDisallow(createStatusMessage(statusInfo));
            }
        }
    }

    private void handleFamily(@Nonnull Response.StatusType statusInfo) throws TemporaryDisallow, TemporaryAllow {
        switch (statusInfo.getFamily()) {
            case SUCCESSFUL:
                break;
            case INFORMATIONAL:
            case REDIRECTION:
            case CLIENT_ERROR:
            case OTHER:
                throw new TemporaryAllow(createStatusMessage(statusInfo));
            case SERVER_ERROR:
                throw new TemporaryDisallow(createStatusMessage(statusInfo));
            default:
                throw new AssertionError("Unknown status family: " + statusInfo.getFamily());
        }
    }

    private String createStatusMessage(@Nonnull Response.StatusType statusInfo) {
        return (statusInfo.getReasonPhrase() != null)
                ? format("response status: {0} \"{1}\"", statusInfo.getStatusCode(), statusInfo.getReasonPhrase())
                : format("response status: {0}", statusInfo.getStatusCode());
    }

}
