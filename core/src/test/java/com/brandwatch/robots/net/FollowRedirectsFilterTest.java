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

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class FollowRedirectsFilterTest extends JerseyTest {

    private static final String RESOURCE_PAYLOAD = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";

    @Path("test")
    public static class TestResource {

        @GET
        @Path("resource")
        public String resource() {
            return RESOURCE_PAYLOAD;
        }

        @GET
        @Path("firstRedirect")
        public Response firstRedirect() {
            return Response.seeOther(UriBuilder.fromPath("/test/resource").build()).build();
        }

        @GET
        @Path("secondRedirect")
        public Response secondRedirect() {
            return Response.seeOther(UriBuilder.fromPath("/test/firstRedirect").build()).build();
        }

        @GET
        @Path("thirdRedirect")
        public Response thirdRedirect() {
            return Response.seeOther(UriBuilder.fromPath("/test/secondRedirect").build()).build();
        }

        @GET
        @Path("brokenRedirect")
        public Response brokenRedirect() {
            return Response.seeOther(UriBuilder.fromPath("/test/resource").build())
                    .location(null)
                    .build();
        }

        @GET
        @Path("loop1")
        public Response loop1() {
            return Response.seeOther(UriBuilder.fromPath("/test/loop2").build()).build();
        }

        @GET
        @Path("loop2")
        public Response loop2() {
            return Response.seeOther(UriBuilder.fromPath("/test/loop1").build()).build();
        }

    }

    @Override
    protected Application configure() {
        return new ResourceConfig(TestResource.class);
    }

    @Override
    protected void configureClient(ClientConfig config) {
        config.register(new FollowRedirectsFilter(2));
        config.property(ClientProperties.FOLLOW_REDIRECTS, false);
    }

    @Test
    public void givenMaxRedirectsIsOne_whenRequestResource_thenReturnsStatusOK() {
        final Response response = target("test/resource").request().get();
        assertThat(response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
    }

    @Test
    public void givenMaxRedirectsIsOne_whenRequestResource_thenReturnsExpectedPayload() {
        final String payload = target("test/resource").request().get(String.class);
        assertThat(payload, equalTo(RESOURCE_PAYLOAD));
    }

    @Test
    public void givenMaxRedirectsIsOne_whenRequestRedirect_thenReturnsStatusOK() {
        final Response response = target("test/firstRedirect").request().get();
        assertThat(response.getStatus(), equalTo(Response.Status.OK.getStatusCode()));
    }

    @Test
    public void givenMaxRedirectsIsOne_whenRequestRedirect_thenReturnsExpectedPayload() {
        final String payload = target("test/firstRedirect").request().get(String.class);
        assertThat(payload, equalTo(RESOURCE_PAYLOAD));
    }

    @Test
    public void givenRedirectsExceedMaxHops_whenRequest_thenReturnsRedirection() {
        final Response response = target("test/thirdRedirect").request().get();
        assertThat(response.getStatusInfo().getFamily(), equalTo(Response.Status.Family.REDIRECTION));
    }

    @Test
    public void givenBrokenRedirect_whenRequest_thenReturnsRedirection() {
        final Response response = target("test/brokenRedirect").request().get();
        assertThat(response.getStatusInfo().getFamily(), equalTo(Response.Status.Family.REDIRECTION));
    }

    @Test
    public void givenRedirectLoop_whenRequest_thenReturnsRedirection() {
        final Response response = target("test/loop1").request().get();
        assertThat(response.getStatusInfo().getFamily(), equalTo(Response.Status.Family.REDIRECTION));
    }

}
