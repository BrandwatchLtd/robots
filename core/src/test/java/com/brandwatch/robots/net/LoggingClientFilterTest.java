package com.brandwatch.robots.net;

import com.brandwatch.robots.util.LogLevel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientResponseContext;
import java.io.IOException;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LoggingClientFilterTest {

    @Mock
    private Logger logger;
    @Mock
    private LogLevel level;
    @InjectMocks
    private LoggingClientFilter loggingClientFilter;
    @Mock
    private ClientRequestContext clientRequestContext;
    @Mock
    private ClientResponseContext clientResponseContext;

    @Test
    public void givenRequestContext_whenFilter_thenNoExceptionThrown() throws IOException {
        loggingClientFilter.filter(clientRequestContext);
    }

    @Test
    public void givenRequestAndResponseContext_whenFilter_thenNoExceptionThrown() throws IOException {
        loggingClientFilter.filter(clientRequestContext, clientResponseContext);
    }
}
