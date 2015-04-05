package com.brandwatch.robots.domain;

/*
 * #%L
 * Robots (core)
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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public abstract class AbstractDomainObjectTest<T> {

    @Test
    public void givenTwoValidInstances_whenEquals_thenReturnsTrue() {
        T firstInstance = newValidInstance();
        T secondInstance = newValidInstance();
        boolean equal = firstInstance.equals(secondInstance);
        assertThat(equal, equalTo(true));
    }

    @Test
    public void givenValidInstance_whenEqualsNull_thenReturnsFalse() {
        T firstInstance = newValidInstance();
        boolean equal = firstInstance.equals(null);
        assertThat(equal, equalTo(false));
    }

    @Test
    public void givenTwoValidInstances_whenHashCode_thenReturnsSameValue() {
        T firstInstance = newValidInstance();
        T secondInstance = newValidInstance();
        assertThat(firstInstance.hashCode(), equalTo(secondInstance.hashCode()));
    }

    @Test
    public void givenValidInstance_whenToString_thenReturnsNonNull() {
        T firstInstance = newValidInstance();
        String result = firstInstance.toString();
        assertThat(result, notNullValue());
    }


    protected abstract T newValidInstance();

}
