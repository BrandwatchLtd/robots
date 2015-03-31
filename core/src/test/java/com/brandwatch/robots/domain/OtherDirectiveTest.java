package com.brandwatch.robots.domain;

public class OtherDirectiveTest extends AbstractDomainObjectTest<OtherDirective> {

    @Override
    protected OtherDirective newValidInstance() {
        return new OtherDirective("things", "stuff");
    }
}
