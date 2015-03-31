package com.brandwatch.robots.domain;

public class RobotsTest extends AbstractDomainObjectTest<Robots> {

    @Override
    protected Robots newValidInstance() {
        return new Robots.Builder().build();
    }

}
