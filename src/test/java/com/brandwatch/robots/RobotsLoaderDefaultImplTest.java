package com.brandwatch.robots;

import com.brandwatch.robots.domain.Robots;
import com.google.common.io.CharSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.io.Reader;
import java.net.URI;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RobotsLoaderDefaultImplTest {

    @Mock
    private RobotsUtilities utilities;

    @InjectMocks
    private RobotsLoaderDefaultImpl instance;

    @Test(expected = NullPointerException.class)
    public void givenNullSource_whenLoad_thenThrowsNPE() throws IOException {
        instance.load(null);
    }

    @Test
    public void givenEmptySource_whenLoad_thenReturnsNonNull() throws IOException {
        when(utilities.createCharSourceFor(any(URI.class)))
                .thenReturn(CharSource.empty());
        Robots result = instance.load(URI.create("http://example.com/robots.txt"));
        assertThat(result, notNullValue());
    }

    @Test(expected = IOException.class)
    public void givenSourceThrowsIOE_whenLoad_thenThrowsIOE() throws IOException {
        when(utilities.createCharSourceFor(any(URI.class)))
                .thenReturn(new CharSource() {
                    @Nonnull
                    @Override
                    public Reader openStream() throws IOException {
                        throw new IOException();
                    }
                });
        instance.load(URI.create("http://example.com/robots.txt"));
    }

}
