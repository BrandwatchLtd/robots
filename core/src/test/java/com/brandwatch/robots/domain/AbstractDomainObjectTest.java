package com.brandwatch.robots.domain;

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
