package com.brandwatch.robots;

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
