package com.brandwatch.robots.cli;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URI;
import java.util.List;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommandIntegrationTest {

    @Spy
    private Arguments arguments;

    @InjectMocks
    private Command command;

    @Test
    public void givenDefaultArguments_whenGetResults_thenReturnsResultList() {
        List<Result> result = command.getResults();
        assertThat(result, empty());
    }

    @Test
    public void givensSingleResource_whenGetResults_thenReturnsSingleResult() {
        when(arguments.getResources()).thenReturn(ImmutableList.of(URI.create("http://example.com")));
        List<Result> result = command.getResults();
        assertThat(result, hasSize(1));
    }

    @Test
    public void givensTwoResources_whenGetResults_thenReturnsTwoResults() {
        when(arguments.getResources()).thenReturn(ImmutableList.of(
                URI.create("http://example.com"),
                URI.create("https://example.com")));
        List<Result> result = command.getResults();
        assertThat(result, hasSize(2));
    }
}
