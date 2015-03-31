package com.brandwatch.robots.domain;

public class GroupTest extends AbstractDomainObjectTest<Group> {

    @Override
    protected Group newValidInstance() {
        return new Group.Builder().build();
    }

}
