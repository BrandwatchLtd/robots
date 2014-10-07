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

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class RobotsDownloaderImplTest {

    @Mock
    private RobotsUtilities utilities;

    @InjectMocks
    private RobotsDownloaderImpl instance;

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
            @Nonnull
            @Override
            public Reader openStream() throws IOException {
                throw new IOException();
            }
        });
    }

}
