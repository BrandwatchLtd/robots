package com.brandwatch.robots;

import com.brandwatch.robots.domain.Robots;
import com.google.common.io.CharSource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.Reader;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class RobotsDownloaderImplTest {

    private RobotsDownloaderImpl instance;

    @Before
    public void setup() throws IOException {
        instance = new RobotsDownloaderImpl();
    }

    @Test(expected = NullPointerException.class)
    public void givenNullSource_whenLoad_thenThrowsNPE() {
        instance.load(null);
    }

    @Test
    public void givenEmptySource_whenLoad_thenReturnsNonNull() {
        Robots result = instance.load(CharSource.empty());
        assertThat(result, notNullValue());
    }

    @Test(expected = RuntimeException.class)
    public void givenSourceThrowsIOException_whenLoad_thenThrowsRuntimeException() {
        instance.load(new CharSource() {
            @Override
            public Reader openStream() throws IOException {
                throw new IOException();
            }
        });
    }

}
