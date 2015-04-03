package com.brandwatch.robots.net;

import com.brandwatch.robots.util.LogLevel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.Reader;

import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LoggingReaderTest {

    @Mock
    private Reader delegateReader;
    @Mock
    private LogLevel level;
    @Mock
    private Logger logger;
    @InjectMocks
    private LoggingReader loggingReader;

    @Test
    public void whenClose_thenCallsDelegate() throws IOException {
        loggingReader.close();
        verify(delegateReader, only()).close();
    }

    @Test
    public void whenMarkSupported_thenCallsDelegate() throws IOException {
        loggingReader.markSupported();
        verify(delegateReader, only()).markSupported();
    }

    @Test
    public void givenValidMark_whenMark_thenCallsDelegate() throws IOException {
        int readAheadLimit = 123;
        loggingReader.mark(readAheadLimit);
        verify(delegateReader, only()).mark(readAheadLimit);
    }

    @Test
    public void whenRead_thenCallsDelegate() throws IOException {
        loggingReader.read();
        verify(delegateReader, only()).read();
    }

    @Test
    public void whenRead3args_thenCallsDelegate() throws IOException {
        char[] chars = new char[0];
        int off = 0;
        int len = 0;
        loggingReader.read(chars, off, len);
        verify(delegateReader, only()).read(chars, off, len);
    }

    @Test
    public void whenReady_thenCallsDelegate() throws IOException {
        loggingReader.ready();
        verify(delegateReader, only()).ready();
    }

    @Test
    public void whenReset_thenCallsDelegate() throws IOException {
        loggingReader.reset();
        verify(delegateReader, only()).reset();
    }

    @Test
    public void whenSkip_thenCallsDelegate() throws IOException {
        int len = 0;
        loggingReader.skip(len);
        verify(delegateReader, only()).skip(len);
    }

}
