package com.brandwatch.robots.net;

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

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

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
    public void setupClient() throws IOException {
        when(client.target(any(URI.class))
                        .request()
                        .accept(Matchers.<MediaType>anyVararg())
                        .header(anyString(), anyObject())
                        .buildGet()
                        .invoke()
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
