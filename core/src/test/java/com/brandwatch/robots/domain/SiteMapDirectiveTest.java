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
import static org.junit.Assert.assertThat;

public class SiteMapDirectiveTest extends AbstractDomainObjectTest<SiteMapDirective> {

    private static final String VALID_SITE_MAP = "http://example.com/sitemap.xml";

    @Override
    protected SiteMapDirective newValidInstance() {
        return new SiteMapDirective(VALID_SITE_MAP);
    }

    @Test
    public void givenValidSiteMapDirective_whenGetValue_thenReturnsExpectedValue() {
        SiteMapDirective siteMapDirective = new SiteMapDirective(VALID_SITE_MAP);
        assertThat(siteMapDirective.getValue(), equalTo(VALID_SITE_MAP));
    }

    @Test
    public void givenValidSiteMapDirective_whenGetField_thenReturnsExpectedValue() {
        SiteMapDirective siteMapDirective = new SiteMapDirective(VALID_SITE_MAP);
        assertThat(siteMapDirective.getField(), equalTo("sitemap"));
    }
}
