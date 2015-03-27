package com.brandwatch.robots;

/*
 * #%L
 * Robots (core)
 * %%
 * Copyright (C) 2015 Brandwatch
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

import com.brandwatch.robots.domain.Robots;
import com.brandwatch.robots.net.CharSourceSupplier;
import com.google.common.io.CharSource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RobotsLoaderImplTest {

    private static final URI EXAMPLE_URI = URI.create("http://example.com/robots.txt");
    private static final Robots ALLOW_ALL = new Robots.Builder().build();
    private static final Robots DISALLOW_ALL = new Robots.Builder().build();
    @Mock
    private RobotsUtilities utilities;
    @Mock
    private RobotsBuildingParseHandler handler;
    @Mock
    private RobotsConfig config;
    @Mock
    private RobotsFactory factory;
    @Mock
    private CharSourceSupplier charSourceSupplier;

    private RobotsLoaderImpl instance;

    @Before
    public void setup() {
        when(factory.getUtilities()).thenReturn(utilities);
        when(factory.createRobotsBuildingHandler()).thenReturn(handler);

        when(factory.createAllowAllRobots()).thenReturn(ALLOW_ALL);
        when(factory.createDisallowAllRobots()).thenReturn(DISALLOW_ALL);
        when(factory.createCharSourceSupplier()).thenReturn(charSourceSupplier);

        instance = new RobotsLoaderImpl(factory);
    }

    @Test(expected = NullPointerException.class)
    public void givenNullSource_whenLoad_thenThrowsNPE() throws IOException {
        instance.load(null);
    }

    @Test
    public void givenEmptySource_whenLoad_thenHandlerGetInvoked() throws IOException {
        when(charSourceSupplier.get(any(URI.class)))
                .thenReturn(CharSource.empty());
        instance.load(EXAMPLE_URI);
        verify(handler).get();
    }

    @Test(expected = ExpectedError.class)
    public void givenSourceThrowsError_whenLoad_thenThrowsError() {
        when(charSourceSupplier.get(any(URI.class)))
                .thenReturn(new CharSource() {
                    @Nonnull
                    @Override
                    public Reader openStream() {
                        throw new ExpectedError();
                    }
                });
        instance.load(EXAMPLE_URI);
    }

    @Test(expected = ExpectedRuntimeException.class)
    public void givenSourceThrowsRuntimeException_whenLoad_thenThrowsRuntimeException() {
        when(charSourceSupplier.get(any(URI.class)))
                .thenReturn(new CharSource() {
                    @Nonnull
                    @Override
                    public Reader openStream() {
                        throw new ExpectedRuntimeException();
                    }
                });
        instance.load(EXAMPLE_URI);
    }


    @Test
    public void givenSourceThrowsIOException_whenLoad_thenAllowAll() {
        when(charSourceSupplier.get(any(URI.class)))
                .thenReturn(new CharSource() {
                    @Nonnull
                    @Override
                    public Reader openStream() throws IOException {
                        throw new IOException();
                    }
                });
        Robots result = instance.load(EXAMPLE_URI);
        assertThat(result, equalTo(ALLOW_ALL));
    }

}
