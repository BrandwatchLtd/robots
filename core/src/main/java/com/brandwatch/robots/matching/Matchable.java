package com.brandwatch.robots.matching;

public interface Matchable<T> {

    Matcher<T> getMatcher();

}
