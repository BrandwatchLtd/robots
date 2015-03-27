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
import com.google.common.cache.Cache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.net.URI;
import java.util.concurrent.Callable;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RobotsLoaderCachedImplTest {

    private static final URI EXAMPLE_URI = URI.create("http://example.com/robots.txt");

    @Mock
    private Cache<URI, Robots> cache;

    @Mock
    private RobotsLoader delegate;

    @InjectMocks
    private RobotsLoaderCachedImpl loader;

    @Test(expected = NullPointerException.class)
    public void givenNullUri_whenLoad_thenThrowsNPE() throws Exception {
        loader.load(null);
    }

    @Test
    public void givenValidUri_whenLoad_resultEqualsExpected() throws Exception {
        Robots expected = new Robots.Builder().build();
        when(cache.get(eq(EXAMPLE_URI), any(Callable.class))).thenReturn(expected);
        Robots result = loader.load(EXAMPLE_URI);
        assertThat(result, equalTo(expected));
    }

    @Test
    public void givenCacheMiss_whenLoad_resultInvokesDelegateOnce() throws Exception {
        when(cache.get(eq(EXAMPLE_URI), any(Callable.class))).thenAnswer(new Answer<Robots>() {
            @Override
            public Robots answer(InvocationOnMock invocation) throws Throwable {
                return ((Callable<Robots>) invocation.getArguments()[1]).call();
            }
        });
        loader.load(EXAMPLE_URI);
        verify(delegate).load(EXAMPLE_URI);
        verifyNoMoreInteractions(delegate);
    }

    @Test
    public void givenCacheHit_whenLoad_resultNotInvokesDelegate() throws Exception {
        when(cache.get(eq(EXAMPLE_URI), any(Callable.class))).thenAnswer(new Answer<Robots>() {
            @Override
            public Robots answer(InvocationOnMock invocation) throws Throwable {
                return new Robots.Builder().build();
            }
        });
        loader.load(EXAMPLE_URI);
        verifyZeroInteractions(delegate);
    }

}
