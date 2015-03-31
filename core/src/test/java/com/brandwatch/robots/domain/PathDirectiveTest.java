package com.brandwatch.robots.domain;

import com.brandwatch.robots.matching.EverythingMatcher;

public class PathDirectiveTest extends AbstractDomainObjectTest<PathDirective> {

    @Override
    protected PathDirective newValidInstance() {
        return new PathDirective(PathDirective.Field.allow, "*", new EverythingMatcher<String>());
    }
}
