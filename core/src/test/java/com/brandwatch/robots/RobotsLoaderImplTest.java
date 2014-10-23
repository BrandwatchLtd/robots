package com.brandwatch.robots;

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
