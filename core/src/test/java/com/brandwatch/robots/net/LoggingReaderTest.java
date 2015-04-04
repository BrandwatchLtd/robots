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
