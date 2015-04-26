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

import com.google.common.collect.ImmutableList;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class HttpStatusHandlerTest extends JerseyTest {

    private static final String STATUS_PARAM = "status";

    private final int statusCode;
    private final Action expectedAction;

    public HttpStatusHandlerTest(int statusCode, Action expectedAction) {
        this.statusCode = statusCode;
        this.expectedAction = expectedAction;
    }

    private enum Action {
        TEMPORARY_ALLOW,
        TEMPORARY_DISALLOW,
        CONDITIONAL_ALLOW
    }

    @Path("/")
    public static class TestResource {
        @GET
        public Response test(@QueryParam(STATUS_PARAM) int status) {
            return Response.status(status).build();
        }
    }

    @Override
    protected Application configure() {
        return new ResourceConfig(TestResource.class);
    }

    @Override
    protected void configureClient(ClientConfig config) {
        config.register(new HttpStatusHandler());
    }

    @Test
    public void test() {
        try {
            final Response response = target()
                    .queryParam(STATUS_PARAM, statusCode)
                    .request()
                    .get();
            assertThat(response.getStatus(), equalTo(statusCode));
        } catch (ProcessingException e) {
            final Throwable cause = e.getCause();
            if (cause == null) {
                throw e;
            } else if (cause instanceof TemporaryAllow) {
                assertThat(expectedAction, equalTo(Action.TEMPORARY_ALLOW));
            } else if (cause instanceof TemporaryDisallow) {
                assertThat(expectedAction, equalTo(Action.TEMPORARY_DISALLOW));
            } else {
                throw e;
            }
        }
    }

    @Parameterized.Parameters(name = "{index}: {0} => {1}")
    public static Iterable<Object[]> data() {
        return ImmutableList.of(
                new Object[]{101, Action.TEMPORARY_ALLOW},
                new Object[]{102, Action.TEMPORARY_ALLOW},
                new Object[]{200, Action.CONDITIONAL_ALLOW},
                new Object[]{201, Action.CONDITIONAL_ALLOW},
                new Object[]{202, Action.CONDITIONAL_ALLOW},
                new Object[]{203, Action.CONDITIONAL_ALLOW},
                new Object[]{204, Action.TEMPORARY_ALLOW},
                new Object[]{205, Action.TEMPORARY_ALLOW},
                new Object[]{206, Action.CONDITIONAL_ALLOW},
                new Object[]{207, Action.TEMPORARY_ALLOW},
                new Object[]{208, Action.TEMPORARY_ALLOW},
                new Object[]{226, Action.TEMPORARY_ALLOW},
                new Object[]{300, Action.TEMPORARY_ALLOW},
                new Object[]{301, Action.TEMPORARY_ALLOW},
                new Object[]{302, Action.TEMPORARY_ALLOW},
                new Object[]{303, Action.TEMPORARY_ALLOW},
                new Object[]{304, Action.TEMPORARY_ALLOW},
                new Object[]{305, Action.TEMPORARY_ALLOW},
                new Object[]{306, Action.TEMPORARY_ALLOW},
                new Object[]{307, Action.TEMPORARY_ALLOW},
                new Object[]{308, Action.TEMPORARY_ALLOW},
                new Object[]{400, Action.TEMPORARY_ALLOW},
                new Object[]{401, Action.TEMPORARY_DISALLOW},
                new Object[]{402, Action.TEMPORARY_DISALLOW},
                new Object[]{403, Action.TEMPORARY_DISALLOW},
                new Object[]{404, Action.TEMPORARY_ALLOW},
                new Object[]{405, Action.TEMPORARY_ALLOW},
                new Object[]{406, Action.TEMPORARY_ALLOW},
                new Object[]{407, Action.TEMPORARY_ALLOW},
                new Object[]{408, Action.TEMPORARY_ALLOW},
                new Object[]{409, Action.TEMPORARY_ALLOW},
                new Object[]{410, Action.TEMPORARY_ALLOW},
                new Object[]{411, Action.TEMPORARY_ALLOW},
                new Object[]{412, Action.TEMPORARY_ALLOW},
                new Object[]{413, Action.TEMPORARY_ALLOW},
                new Object[]{414, Action.TEMPORARY_ALLOW},
                new Object[]{415, Action.TEMPORARY_ALLOW},
                new Object[]{416, Action.TEMPORARY_ALLOW},
                new Object[]{417, Action.TEMPORARY_ALLOW},
                new Object[]{418, Action.TEMPORARY_ALLOW},
                new Object[]{419, Action.TEMPORARY_ALLOW},
                new Object[]{420, Action.TEMPORARY_ALLOW},
                new Object[]{421, Action.TEMPORARY_ALLOW},
                new Object[]{422, Action.TEMPORARY_ALLOW},
                new Object[]{423, Action.TEMPORARY_ALLOW},
                new Object[]{424, Action.TEMPORARY_ALLOW},
                new Object[]{426, Action.TEMPORARY_ALLOW},
                new Object[]{428, Action.TEMPORARY_ALLOW},
                new Object[]{429, Action.TEMPORARY_ALLOW},
                new Object[]{431, Action.TEMPORARY_ALLOW},
                new Object[]{440, Action.TEMPORARY_ALLOW},
                new Object[]{444, Action.TEMPORARY_ALLOW},
                new Object[]{449, Action.TEMPORARY_ALLOW},
                new Object[]{450, Action.TEMPORARY_ALLOW},
                new Object[]{451, Action.TEMPORARY_ALLOW},
                new Object[]{494, Action.TEMPORARY_ALLOW},
                new Object[]{495, Action.TEMPORARY_ALLOW},
                new Object[]{496, Action.TEMPORARY_ALLOW},
                new Object[]{497, Action.TEMPORARY_ALLOW},
                new Object[]{498, Action.TEMPORARY_ALLOW},
                new Object[]{499, Action.TEMPORARY_ALLOW},
                new Object[]{500, Action.TEMPORARY_DISALLOW},
                new Object[]{501, Action.TEMPORARY_DISALLOW},
                new Object[]{502, Action.TEMPORARY_DISALLOW},
                new Object[]{503, Action.TEMPORARY_DISALLOW},
                new Object[]{504, Action.TEMPORARY_DISALLOW},
                new Object[]{505, Action.TEMPORARY_DISALLOW},
                new Object[]{506, Action.TEMPORARY_DISALLOW},
                new Object[]{507, Action.TEMPORARY_DISALLOW},
                new Object[]{508, Action.TEMPORARY_DISALLOW},
                new Object[]{509, Action.TEMPORARY_DISALLOW},
                new Object[]{510, Action.TEMPORARY_DISALLOW},
                new Object[]{511, Action.TEMPORARY_DISALLOW},
                new Object[]{598, Action.TEMPORARY_DISALLOW},
                new Object[]{599, Action.TEMPORARY_DISALLOW},
                new Object[]{600, Action.TEMPORARY_ALLOW}
        );
    }

}
