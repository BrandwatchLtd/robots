package com.brandwatch.robots.net;

import com.brandwatch.robots.RobotsConfig;
import com.google.common.base.Charsets;
import com.google.common.io.CharSource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URI;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CharSourceSupplierBasicImplTest {

    @Mock
    private RobotsConfig config;

    @InjectMocks
    private CharSourceSupplierBasicImpl instance;

    @Before
    public void setup() {
        when(config.getMaxFileSizeBytes()).thenReturn(1000000);
        when(config.getDefaultCharset()).thenReturn(Charsets.UTF_8);
    }

    @Test(expected = NullPointerException.class)
    public void givenNullUri_whenGet_thenThrowsNPE() throws IOException {
        instance.get(null);
    }

    @Test
    public void givenExampleUri_whenGet_thenReturnsCharSource() throws IOException {
        CharSource result = instance.get(URI.create("http://example.com/robots.txt"));
        assertThat(result, notNullValue());
    }

}
