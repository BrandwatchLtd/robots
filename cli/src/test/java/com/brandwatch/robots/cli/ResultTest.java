package com.brandwatch.robots.cli;

import org.junit.Test;

import java.net.URI;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ResultTest {

    private static final URI VALID_RESOURCE = URI.create("http://example.com");

    @Test(expected = NullPointerException.class)
    public void givenResourceIsNull_whenInstantiate_thenThrowsNPE() {
        new Result(null, true);
    }

    @Test
    public void givenValidURI_whenGetResource_thenResultsEqualsExpected() {
        Result result = new Result(VALID_RESOURCE, true);
        assertThat(result.getResource(), equalTo(VALID_RESOURCE));
    }

    @Test
    public void givenResourceAllowed_whenIsAllowed_thenReturnsTrue() {
        Result result = new Result(VALID_RESOURCE, true);
        assertThat(result.isAllowed(), equalTo(true));
    }

    @Test
    public void givenResourceDisallowed_whenIsAllowed_thenReturnsFalse() {
        Result result = new Result(VALID_RESOURCE, false);
        assertThat(result.isAllowed(), equalTo(false));
    }
}
