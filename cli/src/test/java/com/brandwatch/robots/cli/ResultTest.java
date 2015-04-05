package com.brandwatch.robots.cli;

/*
 * #%L
 * Robots (command-line interface)
 * %%
 * Copyright (C) 2014 - 2015 Brandwatch
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
