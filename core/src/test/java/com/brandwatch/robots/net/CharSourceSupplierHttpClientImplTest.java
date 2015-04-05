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

import com.brandwatch.robots.RobotsConfig;
import com.brandwatch.robots.TemporaryAllow;
import com.brandwatch.robots.TemporaryDisallow;
import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;
import com.google.common.io.CharSource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyObject;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CharSourceSupplierHttpClientImplTest {

    private static final URI EXAMPLE_URI = URI.create("http://example.com/robots.txt");

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private Client client;
    @Mock
    private Response response = mock(Response.class);
    @Mock
    private StatusType statusInfo = mock(StatusType.class);
    @Mock
    private RobotsConfig config;

    @InjectMocks
    private CharSourceSupplierHttpClientImpl instance;

    @Before
    public void setup() {
        when(config.getMaxFileSizeBytes()).thenReturn(1000);
        when(config.getDefaultCharset()).thenReturn(Charsets.UTF_8);
    }

    @Before
    public void setupClient() throws IOException, ExecutionException, InterruptedException, TimeoutException {
        when(client.target(any(URI.class))
                        .request()
                        .accept(Matchers.<MediaType>anyVararg())
                        .header(anyString(), anyObject())
                        .buildGet()
                        .submit()
                        .get(anyLong(), any(TimeUnit.class))
        ).thenReturn(response);

        when(response.getStatusInfo()).thenReturn(statusInfo);
        when(response.getEntity()).thenReturn(ByteSource.empty().openStream());

        when(statusInfo.getStatusCode()).thenReturn(200);
        when(statusInfo.getFamily()).thenReturn(Family.SUCCESSFUL);
        when(statusInfo.getReasonPhrase()).thenReturn("Okay");
    }

    @Test(expected = NullPointerException.class)
    public void givenNullUri_whenGet_thenThrowsNPE() throws IOException {
        instance.get(null);
    }

    @Test
    public void givenExampleUri_whenGet_thenReturnsCharSource() throws IOException {
        CharSource result = instance.get(URI.create("http://example.com/robots.txt"));
        assertThat(result, notNullValue());
    }

    @Test(expected = TemporaryDisallow.class)
    public void givenServerError_whenOpenStream_thenDisallowAll() throws IOException {
        when(statusInfo.getFamily()).thenReturn(Family.SERVER_ERROR);
        when(statusInfo.getStatusCode()).thenReturn(500);
        when(statusInfo.getReasonPhrase()).thenReturn("Server Error");
        CharSource source = instance.get(EXAMPLE_URI);
        source.openStream();
    }

    @Test(expected = TemporaryAllow.class)
    public void givenClientError_whenOpenStream_thenAllowAll() throws IOException {
        when(statusInfo.getFamily()).thenReturn(Family.CLIENT_ERROR);
        when(statusInfo.getStatusCode()).thenReturn(400);
        when(statusInfo.getReasonPhrase()).thenReturn("Client Error");
        CharSource source = instance.get(EXAMPLE_URI);
        source.openStream();
    }

    @Test(expected = TemporaryDisallow.class)
    public void givenUnauthorized_whenOpenStream_thenDisallowAll() throws IOException {
        when(statusInfo.getFamily()).thenReturn(Family.CLIENT_ERROR);
        when(statusInfo.getStatusCode()).thenReturn(401);
        when(statusInfo.getReasonPhrase()).thenReturn("Unauthorized");
        CharSource source = instance.get(EXAMPLE_URI);
        source.openStream();
    }

    @Test(expected = TemporaryDisallow.class)
    public void givenPaymentRequired_whenOpenStream_thenDisallowAll() throws IOException {
        when(statusInfo.getFamily()).thenReturn(Family.CLIENT_ERROR);
        when(statusInfo.getStatusCode()).thenReturn(402);
        when(statusInfo.getReasonPhrase()).thenReturn("Payment Required");
        CharSource source = instance.get(EXAMPLE_URI);
        source.openStream();
    }

    @Test(expected = TemporaryDisallow.class)
    public void givenForbidden_whenOpenStream_thenDisallowAll() throws IOException {
        when(statusInfo.getFamily()).thenReturn(Family.CLIENT_ERROR);
        when(statusInfo.getStatusCode()).thenReturn(403);
        when(statusInfo.getReasonPhrase()).thenReturn("Forbidden");
        CharSource source = instance.get(EXAMPLE_URI);
        source.openStream();
    }

    @Test(expected = TemporaryAllow.class)
    public void givenNotFound_whenOpenStream_thenAllowAll() throws IOException {
        when(statusInfo.getFamily()).thenReturn(Family.CLIENT_ERROR);
        when(statusInfo.getStatusCode()).thenReturn(404);
        when(statusInfo.getReasonPhrase()).thenReturn("Not Found");
        CharSource source = instance.get(EXAMPLE_URI);
        source.openStream();
    }

    @Test(expected = TemporaryAllow.class)
    public void givenOtherStatus_whenOpenStream_thenAllowAll() throws IOException {
        when(statusInfo.getFamily()).thenReturn(Family.OTHER);
        when(statusInfo.getStatusCode()).thenReturn(600);
        when(statusInfo.getReasonPhrase()).thenReturn("Not Found");
        CharSource source = instance.get(EXAMPLE_URI);
        source.openStream();
    }

}
