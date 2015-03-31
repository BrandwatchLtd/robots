package com.brandwatch.robots.domain;

public class SiteMapDirectiveTest extends AbstractDomainObjectTest<SiteMapDirective> {

    private static final String VALID_SITEMAP = "http://example.com/sitemap.xml";

    @Override
    protected SiteMapDirective newValidInstance() {
        return new SiteMapDirective(VALID_SITEMAP);
    }
}
