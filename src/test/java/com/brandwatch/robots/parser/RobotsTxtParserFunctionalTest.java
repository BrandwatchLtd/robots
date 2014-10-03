package com.brandwatch.robots.parser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.Reader;

import static com.brandwatch.robots.AbstractDataTest.resourceReader;
import static org.mockito.Mockito.inOrder;

@RunWith(MockitoJUnitRunner.class)
public class RobotsTxtParserFunctionalTest {

    @Mock
    private RobotsTxtHandler handler;

    @Test
    public void givenDailyMailBoards_whenParse_thenHandlerInteractionsAreExpected() throws IOException, ParseException {
        Reader reader = resourceReader("http_boards.dailymail.co.uk_robots.txt");
        RobotsTxtParser robotsTxtParser = new RobotsTxtParser(reader);
        robotsTxtParser.parse(handler);
        InOrder o = inOrder(handler);
        o.verify(handler).startEntry();
        o.verify(handler).userAgent("*");
        o.verify(handler).disallow("*.js");
        o.verify(handler).disallow("/search.php*");
        o.verify(handler).disallow("/includes/");
        o.verify(handler).disallow("/install/");
        o.verify(handler).disallow("/customavatars/");
        o.verify(handler).endEntry();
        o.verifyNoMoreInteractions();
    }


    @Test
    public void givenDailyMail_whenParse_thenHandlerInteractionsAreExpected() throws IOException, ParseException {
        Reader reader = resourceReader("http_www.dailymail.co.uk_robots.txt");
        RobotsTxtParser robotsTxtParser = new RobotsTxtParser(reader);
        robotsTxtParser.parse(handler);

        InOrder o = inOrder(handler);

        //# Robots.txt for http://www.dailymail.co.uk/ updated 01/10/14
        //# TS-5682
        //#
        //# All Robots
        o.verify(handler).startEntry();
        o.verify(handler).userAgent("*");
        //#
        //# Begin Standard Rules");
        o.verify(handler).disallow("*readcommentshtml*");
        o.verify(handler).disallow("/home/christmas/*");
        o.verify(handler).disallow("/home/scotland/*");
        o.verify(handler).disallow("*jsessionid*");
        o.verify(handler).disallow("/*competitionId=*");
        o.verify(handler).disallow("/*createThread.html");
        o.verify(handler).disallow("/*debateSearchResults.html");
        o.verify(handler).disallow("/*debateTagSearch.html");
        o.verify(handler).disallow("/*debateUserSearch.html");
        o.verify(handler).disallow("/*login?redirectPath=");
        o.verify(handler).disallow("/*logout?redirectPath=");
        o.verify(handler).disallow("/*myStories.html$");
        o.verify(handler).disallow("/*nextThread.html");
        o.verify(handler).disallow("/*previousThread.html");
        o.verify(handler).disallow("/*questionId*");
        o.verify(handler).disallow("/*selectedImage*");
        o.verify(handler).disallow("/*textbased/channel");
        o.verify(handler).disallow("/*threadIndex=*");
        o.verify(handler).disallow("/*topGallery*");
        o.verify(handler).disallow("/?article_id*");
        o.verify(handler).disallow("/?in_article_id*");
        o.verify(handler).disallow("/ce/item.cms*");
        o.verify(handler).disallow("/dailymail/");
        o.verify(handler).disallow("/debate/polls/poll.html$");
        o.verify(handler).disallow("/destinations/");
        o.verify(handler).disallow("/guide/*");
        o.verify(handler).disallow("/home/sitemaparchive/static$");
        o.verify(handler).disallow("/link/index.html*");
        o.verify(handler).disallow("/mailshopQA/*");
        o.verify(handler).disallow("/SITE=DM/");
        o.verify(handler).disallow("/sport/columnist-1000543/hatchet-man.rss");
        o.verify(handler).disallow("/sport/columnist-1031773/andy-roddick.rss");
        o.verify(handler).disallow("/sport/columnist-1046072/glenn-hoddle.rss");
        o.verify(handler).disallow("/sport/columnist-1060209/jake-humphrey.rss");
        o.verify(handler).disallow("/sport/columnist-353/sir-bobby-robson.rss");
        o.verify(handler).disallow("/sport/columnist-354/ian-ridley--mail-sunday-sports-reporter.rss");
        o.verify(handler).disallow("/sport/columnist-1043711/mark-jeffreys.rss");
        o.verify(handler).disallow("/sport/columnist-423/dan-king.rss");
        o.verify(handler).disallow("/sport/columnist-352/monty-panesar.rss");
        o.verify(handler).disallow("/sudoku*");
        o.verify(handler).disallow("/travel/enjoyengland/index.rss");
        o.verify(handler).disallow("/travel/visitcornwall/index.html");
        o.verify(handler).disallow("/tvshowbiz/tvlistings/*");
        o.verify(handler).disallow("/weather/*");
        o.verify(handler).disallow("/ukplus/*");
        o.verify(handler).disallow("*/chat/*");
        o.verify(handler).disallow("/app/");
        o.verify(handler).disallow("/home/prmts/*");
        o.verify(handler).disallow("/home/sweepstakes/*");
        o.verify(handler).disallow("/beta/*");
        o.verify(handler).disallow("/home/canneslions/index.html");
        o.verify(handler).disallow("/cms/sites/all/modules/ckeditor_link/proxy.php*");
        o.verify(handler).disallow("/%20user/view.php*");
        o.verify(handler).endEntry();

        //#
        //# Disallow Money for Google News
        o.verify(handler).startEntry();
        o.verify(handler).userAgent("Googlebot-News");
        o.verify(handler).disallow("/money/*");
        o.verify(handler).disallow("/tmoney/*");
        o.verify(handler).endEntry();
        //#
        //# Allow Adsense
        o.verify(handler).startEntry();
        o.verify(handler).userAgent("Mediapartners-Google");
        o.verify(handler).disallow("");
        o.verify(handler).endEntry();
        //#
        //# Disallow Specific Robots
        o.verify(handler).startEntry();
        o.verify(handler).userAgent("MnoGoSearch/*");
        o.verify(handler).disallow("/");
        o.verify(handler).endEntry();
        //#
        o.verify(handler).startEntry();
        o.verify(handler).userAgent("omgilibot/0.3");
        o.verify(handler).disallow("/");
        o.verify(handler).endEntry();
        //#
        o.verify(handler).startEntry();
        o.verify(handler).userAgent("WebVac");
        o.verify(handler).disallow("/");
        o.verify(handler).endEntry();
        //#
        o.verify(handler).startEntry();
        o.verify(handler).userAgent("WebZip");
        o.verify(handler).disallow("/");
        o.verify(handler).endEntry();
        //#
        o.verify(handler).startEntry();
        o.verify(handler).userAgent("psbot");
        o.verify(handler).disallow("/");
        o.verify(handler).endEntry();
        //#
        o.verify(handler).startEntry();
        o.verify(handler).userAgent("daumoa");
        o.verify(handler).disallow("/");
        o.verify(handler).endEntry();
        //#
        o.verify(handler).startEntry();
        o.verify(handler).userAgent("AhrefsBot");
        o.verify(handler).disallow("/");
        o.verify(handler).endEntry();
        //#
        o.verify(handler).startEntry();
        o.verify(handler).userAgent("CrystalSemanticsBot");
        o.verify(handler).disallow("/");
        o.verify(handler).endEntry();
        //#
        o.verify(handler).startEntry();
        o.verify(handler).userAgent("AhrefsBot");
        o.verify(handler).disallow("/");
        o.verify(handler).endEntry();
        //#
        o.verify(handler).startEntry();
        o.verify(handler).userAgent("ia_archiver");
        o.verify(handler).disallow("/*/article-*");
        //#
        //# Sitemap Files");
        o.verify(handler).siteMap("http://www.dailymail.co.uk/newssitemap.xml");
        o.verify(handler).siteMap("http://www.dailymail.co.uk/sitemap-articles-year~2014.xml");
        o.verify(handler).siteMap("http://www.dailymail.co.uk/sitemap-articles-year~2013.xml");
        o.verify(handler).siteMap("http://www.dailymail.co.uk/sitemap-articles-year~2012.xml");
        o.verify(handler).siteMap("http://www.dailymail.co.uk/sitemap-articles-year~2011.xml");
        o.verify(handler).siteMap("http://www.dailymail.co.uk/sitemap-articles-year~2010.xml");
        o.verify(handler).siteMap("http://www.dailymail.co.uk/sitemap-articles-year~2009.xml");
        o.verify(handler).siteMap("http://www.dailymail.co.uk/sitemap-articles-year~2008.xml");
        o.verify(handler).siteMap("http://www.dailymail.co.uk/sitemap-articles-year~2007.xml");
        o.verify(handler).siteMap("http://www.dailymail.co.uk/sitemap-articles-year~2006.xml");
        o.verify(handler).siteMap("http://www.dailymail.co.uk/sitemap-articles-year~2005.xml");
        o.verify(handler).siteMap("http://www.dailymail.co.uk/sitemap-articles-year~2004.xml");
        o.verify(handler).siteMap("http://www.dailymail.co.uk/sitemap-articles-year~2003.xml");
        o.verify(handler).siteMap("http://www.dailymail.co.uk/sitemap-articles-year~2002.xml");
        o.verify(handler).siteMap("http://www.dailymail.co.uk/sitemap-articles-year~2001.xml");
        o.verify(handler).siteMap("http://www.dailymail.co.uk/sitemap-articles-year~2000.xml");
        o.verify(handler).siteMap("http://www.dailymail.co.uk/sitemap-articles-year~1999.xml");
        o.verify(handler).siteMap("http://www.dailymail.co.uk/sitemap-articles-year~1998.xml");
        o.verify(handler).siteMap("http://www.dailymail.co.uk/sitemap-articles-year~1997.xml");
        o.verify(handler).siteMap("http://www.dailymail.co.uk/sitemap-articles-year~1996.xml");
        o.verify(handler).siteMap("http://www.dailymail.co.uk/sitemap-articles-year~1994.xml");
        o.verify(handler).siteMap("http://www.dailymail.co.uk/sitemap-articles-year~1993.xml");
        o.verify(handler).siteMap("http://www.dailymail.co.uk/videositemap.xml");
        o.verify(handler).siteMap("http://www.dailymail.co.uk/sitemap-channels.xml");
        o.verify(handler).siteMap("http://www.dailymail.co.uk/sitemap-columnist.xml");
        o.verify(handler).siteMap("http://www.dailymail.co.uk/sitemap-topics.xml");

        o.verify(handler).endEntry();

        o.verifyNoMoreInteractions();
    }


    @Test
    public void giveWwwGoogleCom_whenParse_thenHandlerInteractionsAreExpected() throws IOException, ParseException {
        Reader reader = resourceReader("http_www.google.com_robots.txt");
        RobotsTxtParser robotsTxtParser = new RobotsTxtParser(reader);
        robotsTxtParser.parse(handler);
        InOrder o = inOrder(handler);
        o.verify(handler).startEntry();
        o.verify(handler).userAgent("*");
        o.verify(handler).disallow("/search");
        o.verify(handler).disallow("/sdch");
        o.verify(handler).disallow("/groups");
        o.verify(handler).disallow("/images");
        o.verify(handler).disallow("/catalogs");
        o.verify(handler).allow("/catalogs/about");
        o.verify(handler).allow("/catalogs/p?");
        o.verify(handler).disallow("/catalogues");
        o.verify(handler).allow("/newsalerts");
        o.verify(handler).disallow("/news");
        o.verify(handler).allow("/news/directory");
        o.verify(handler).disallow("/nwshp");
        o.verify(handler).disallow("/setnewsprefs?");
        o.verify(handler).disallow("/index.html?");
        o.verify(handler).disallow("/?");
        o.verify(handler).allow("/?hl=");
        o.verify(handler).disallow("/?hl=*&");
        o.verify(handler).disallow("/addurl/image?");
        o.verify(handler).allow("/mail/help/");
        o.verify(handler).disallow("/mail/");
        o.verify(handler).disallow("/pagead/");
        o.verify(handler).disallow("/relpage/");
        o.verify(handler).disallow("/relcontent");
        o.verify(handler).disallow("/imgres");
        o.verify(handler).disallow("/imglanding");
        o.verify(handler).disallow("/sbd");
        o.verify(handler).disallow("/keyword/");
        o.verify(handler).disallow("/u/");
        o.verify(handler).disallow("/univ/");
        o.verify(handler).disallow("/cobrand");
        o.verify(handler).disallow("/custom");
        o.verify(handler).disallow("/advanced_group_search");
        o.verify(handler).disallow("/googlesite");
        o.verify(handler).disallow("/preferences");
        o.verify(handler).disallow("/setprefs");
        o.verify(handler).disallow("/swr");
        o.verify(handler).disallow("/url");
        o.verify(handler).disallow("/default");
        o.verify(handler).disallow("/m?");
        o.verify(handler).disallow("/m/");
        o.verify(handler).allow("/m/finance");
        o.verify(handler).disallow("/wml?");
        o.verify(handler).disallow("/wml/?");
        o.verify(handler).disallow("/wml/search?");
        o.verify(handler).disallow("/xhtml?");
        o.verify(handler).disallow("/xhtml/?");
        o.verify(handler).disallow("/xhtml/search?");
        o.verify(handler).disallow("/xml?");
        o.verify(handler).disallow("/imode?");
        o.verify(handler).disallow("/imode/?");
        o.verify(handler).disallow("/imode/search?");
        o.verify(handler).disallow("/jsky?");
        o.verify(handler).disallow("/jsky/?");
        o.verify(handler).disallow("/jsky/search?");
        o.verify(handler).disallow("/pda?");
        o.verify(handler).disallow("/pda/?");
        o.verify(handler).disallow("/pda/search?");
        o.verify(handler).disallow("/sprint_xhtml");
        o.verify(handler).disallow("/sprint_wml");
        o.verify(handler).disallow("/pqa");
        o.verify(handler).disallow("/palm");
        o.verify(handler).disallow("/gwt/");
        o.verify(handler).disallow("/purchases");
        o.verify(handler).disallow("/hws");
        o.verify(handler).disallow("/bsd?");
        o.verify(handler).disallow("/linux?");
        o.verify(handler).disallow("/mac?");
        o.verify(handler).disallow("/microsoft?");
        o.verify(handler).disallow("/unclesam?");
        o.verify(handler).disallow("/answers/search?q=");
        o.verify(handler).disallow("/local?");
        o.verify(handler).disallow("/local_url");
        o.verify(handler).disallow("/shihui?");
        o.verify(handler).disallow("/shihui/");
        o.verify(handler).disallow("/froogle?");
        o.verify(handler).disallow("/products?");
        o.verify(handler).disallow("/products/");
        o.verify(handler).disallow("/froogle_");
        o.verify(handler).disallow("/product_");
        o.verify(handler).disallow("/products_");
        o.verify(handler).disallow("/products;");
        o.verify(handler).disallow("/print");
        o.verify(handler).disallow("/books/");
        o.verify(handler).disallow("/bkshp?*q=*");
        o.verify(handler).disallow("/books?*q=*");
        o.verify(handler).disallow("/books?*output=*");
        o.verify(handler).disallow("/books?*pg=*");
        o.verify(handler).disallow("/books?*jtp=*");
        o.verify(handler).disallow("/books?*jscmd=*");
        o.verify(handler).disallow("/books?*buy=*");
        o.verify(handler).disallow("/books?*zoom=*");
        o.verify(handler).allow("/books?*q=related:*");
        o.verify(handler).allow("/books?*q=editions:*");
        o.verify(handler).allow("/books?*q=subject:*");
        o.verify(handler).allow("/books/about");
        o.verify(handler).allow("/booksrightsholders");
        o.verify(handler).allow("/books?*zoom=1*");
        o.verify(handler).allow("/books?*zoom=5*");
        o.verify(handler).disallow("/ebooks/");
        o.verify(handler).disallow("/ebooks?*q=*");
        o.verify(handler).disallow("/ebooks?*output=*");
        o.verify(handler).disallow("/ebooks?*pg=*");
        o.verify(handler).disallow("/ebooks?*jscmd=*");
        o.verify(handler).disallow("/ebooks?*buy=*");
        o.verify(handler).disallow("/ebooks?*zoom=*");
        o.verify(handler).allow("/ebooks?*q=related:*");
        o.verify(handler).allow("/ebooks?*q=editions:*");
        o.verify(handler).allow("/ebooks?*q=subject:*");
        o.verify(handler).allow("/ebooks?*zoom=1*");
        o.verify(handler).allow("/ebooks?*zoom=5*");
        o.verify(handler).disallow("/patents?");
        o.verify(handler).disallow("/patents/download/");
        o.verify(handler).disallow("/patents/pdf/");
        o.verify(handler).disallow("/patents/related/");
        o.verify(handler).disallow("/scholar");
        o.verify(handler).disallow("/citations?");
        o.verify(handler).allow("/citations?user=");
        o.verify(handler).disallow("/citations?*cstart=");
        o.verify(handler).allow("/citations?view_op=new_profile");
        o.verify(handler).allow("/citations?view_op=top_venues");
        o.verify(handler).disallow("/complete");
        o.verify(handler).disallow("/s?");
        o.verify(handler).disallow("/sponsoredlinks");
        o.verify(handler).disallow("/videosearch?");
        o.verify(handler).disallow("/videopreview?");
        o.verify(handler).disallow("/videoprograminfo?");
        o.verify(handler).allow("/maps/api/js?");
        o.verify(handler).allow("/maps/d/");
        o.verify(handler).disallow("/maps?");
        o.verify(handler).disallow("/mapstt?");
        o.verify(handler).disallow("/mapslt?");
        o.verify(handler).disallow("/maps/stk/");
        o.verify(handler).disallow("/maps/br?");
        o.verify(handler).disallow("/mapabcpoi?");
        o.verify(handler).disallow("/maphp?");
        o.verify(handler).disallow("/mapprint?");
        o.verify(handler).disallow("/maps/api/js/");
        o.verify(handler).disallow("/maps/api/staticmap?");
        o.verify(handler).disallow("/mld?");
        o.verify(handler).disallow("/staticmap?");
        o.verify(handler).disallow("/places/");
        o.verify(handler).allow("/places/$");
        o.verify(handler).disallow("/maps/preview");
        o.verify(handler).disallow("/maps/place");
        o.verify(handler).disallow("/help/maps/streetview/partners/welcome/");
        o.verify(handler).disallow("/help/maps/indoormaps/partners/");
        o.verify(handler).disallow("/lochp?");
        o.verify(handler).disallow("/center");
        o.verify(handler).disallow("/ie?");
        o.verify(handler).disallow("/sms/demo?");
        o.verify(handler).disallow("/katrina?");
        o.verify(handler).disallow("/blogsearch?");
        o.verify(handler).disallow("/blogsearch/");
        o.verify(handler).disallow("/blogsearch_feeds");
        o.verify(handler).disallow("/advanced_blog_search");
        o.verify(handler).disallow("/uds/");
        o.verify(handler).disallow("/chart?");
        o.verify(handler).disallow("/transit?");
        o.verify(handler).disallow("/mbd?");
        o.verify(handler).disallow("/extern_js/");
        o.verify(handler).disallow("/xjs/");
        o.verify(handler).disallow("/calendar/feeds/");
        o.verify(handler).disallow("/calendar/ical/");
        o.verify(handler).disallow("/cl2/feeds/");
        o.verify(handler).disallow("/cl2/ical/");
        o.verify(handler).disallow("/coop/directory");
        o.verify(handler).disallow("/coop/manage");
        o.verify(handler).disallow("/trends?");
        o.verify(handler).disallow("/trends/music?");
        o.verify(handler).disallow("/trends/hottrends?");
        o.verify(handler).disallow("/trends/viz?");
        o.verify(handler).disallow("/trends/embed.js?");
        o.verify(handler).disallow("/trends/fetchComponent?");
        o.verify(handler).disallow("/notebook/search?");
        o.verify(handler).disallow("/musica");
        o.verify(handler).disallow("/musicad");
        o.verify(handler).disallow("/musicas");
        o.verify(handler).disallow("/musicl");
        o.verify(handler).disallow("/musics");
        o.verify(handler).disallow("/musicsearch");
        o.verify(handler).disallow("/musicsp");
        o.verify(handler).disallow("/musiclp");
        o.verify(handler).disallow("/browsersync");
        o.verify(handler).disallow("/call");
        o.verify(handler).disallow("/archivesearch?");
        o.verify(handler).disallow("/archivesearch/url");
        o.verify(handler).disallow("/archivesearch/advanced_search");
        o.verify(handler).disallow("/base/reportbadoffer");
        o.verify(handler).disallow("/urchin_test/");
        o.verify(handler).disallow("/movies?");
        o.verify(handler).disallow("/codesearch?");
        o.verify(handler).disallow("/codesearch/feeds/search?");
        o.verify(handler).disallow("/wapsearch?");
        o.verify(handler).disallow("/safebrowsing");
        o.verify(handler).allow("/safebrowsing/diagnostic");
        o.verify(handler).allow("/safebrowsing/report_badware/");
        o.verify(handler).allow("/safebrowsing/report_error/");
        o.verify(handler).allow("/safebrowsing/report_phish/");
        o.verify(handler).disallow("/reviews/search?");
        o.verify(handler).disallow("/orkut/albums");
        o.verify(handler).allow("/jsapi");
        o.verify(handler).disallow("/views?");
        o.verify(handler).disallow("/c/");
        o.verify(handler).disallow("/cbk");
        o.verify(handler).allow("/cbk?output=tile&cb_client=maps_sv");
        o.verify(handler).disallow("/recharge/dashboard/car");
        o.verify(handler).disallow("/recharge/dashboard/static/");
        o.verify(handler).disallow("/translate_a/");
        o.verify(handler).disallow("/translate_c");
        o.verify(handler).disallow("/translate_f");
        o.verify(handler).disallow("/translate_static/");
        o.verify(handler).disallow("/translate_suggestion");
        o.verify(handler).disallow("/profiles/me");
        o.verify(handler).allow("/profiles");
        o.verify(handler).disallow("/s2/profiles/me");
        o.verify(handler).allow("/s2/profiles");
        o.verify(handler).allow("/s2/oz");
        o.verify(handler).allow("/s2/photos");
        o.verify(handler).allow("/s2/search/social");
        o.verify(handler).allow("/s2/static");
        o.verify(handler).disallow("/s2");
        o.verify(handler).disallow("/transconsole/portal/");
        o.verify(handler).disallow("/gcc/");
        o.verify(handler).disallow("/aclk");
        o.verify(handler).disallow("/cse?");
        o.verify(handler).disallow("/cse/home");
        o.verify(handler).disallow("/cse/panel");
        o.verify(handler).disallow("/cse/manage");
        o.verify(handler).disallow("/tbproxy/");
        o.verify(handler).disallow("/imesync/");
        o.verify(handler).disallow("/shenghuo/search?");
        o.verify(handler).disallow("/support/forum/search?");
        o.verify(handler).disallow("/reviews/polls/");
        o.verify(handler).disallow("/hosted/images/");
        o.verify(handler).disallow("/ppob/?");
        o.verify(handler).disallow("/ppob?");
        o.verify(handler).disallow("/adwordsresellers");
        o.verify(handler).disallow("/accounts/ClientLogin");
        o.verify(handler).disallow("/accounts/ClientAuth");
        o.verify(handler).disallow("/accounts/o8");
        o.verify(handler).allow("/accounts/o8/id");
        o.verify(handler).disallow("/topicsearch?q=");
        o.verify(handler).disallow("/xfx7/");
        o.verify(handler).disallow("/squared/api");
        o.verify(handler).disallow("/squared/search");
        o.verify(handler).disallow("/squared/table");
        o.verify(handler).disallow("/toolkit/");
        o.verify(handler).allow("/toolkit/*.html");
        o.verify(handler).disallow("/globalmarketfinder/");
        o.verify(handler).allow("/globalmarketfinder/*.html");
        o.verify(handler).disallow("/qnasearch?");
        o.verify(handler).disallow("/app/updates");
        o.verify(handler).disallow("/sidewiki/entry/");
        o.verify(handler).disallow("/quality_form?");
        o.verify(handler).disallow("/labs/popgadget/search");
        o.verify(handler).disallow("/buzz/post");
        o.verify(handler).disallow("/compressiontest/");
        o.verify(handler).disallow("/analytics/reporting/");
        o.verify(handler).disallow("/analytics/admin/");
        o.verify(handler).disallow("/analytics/web/");
        o.verify(handler).disallow("/analytics/feeds/");
        o.verify(handler).disallow("/analytics/settings/");
        o.verify(handler).allow("/alerts/manage");
        o.verify(handler).allow("/alerts/remove");
        o.verify(handler).disallow("/alerts/");
        o.verify(handler).allow("/alerts/$");
        o.verify(handler).disallow("/ads/search?");
        o.verify(handler).disallow("/ads/plan/action_plan?");
        o.verify(handler).disallow("/ads/plan/api/");
        o.verify(handler).disallow("/phone/compare/?");
        o.verify(handler).disallow("/travel/clk");
        o.verify(handler).disallow("/hotelfinder/rpc");
        o.verify(handler).disallow("/hotels/rpc");
        o.verify(handler).disallow("/flights/rpc");
        o.verify(handler).disallow("/commercesearch/services/");
        o.verify(handler).disallow("/evaluation/");
        o.verify(handler).disallow("/chrome/browser/mobile/tour");
        o.verify(handler).disallow("/compare/*/apply*");
        o.verify(handler).disallow("/forms/perks/");
        o.verify(handler).disallow("/baraza/*/search");
        o.verify(handler).disallow("/baraza/*/report");
        o.verify(handler).disallow("/shopping/suppliers/search");
        o.verify(handler).disallow("/ct/");
        o.verify(handler).disallow("/edu/cs4hs/");
        o.verify(handler).disallow("/trustedstores/s/");
        o.verify(handler).disallow("/trustedstores/tm2");
        o.verify(handler).disallow("/trustedstores/verify");
        o.verify(handler).disallow("/adwords/proposal");
        o.verify(handler).disallow("/shopping/product/");
        o.verify(handler).disallow("/shopping/seller");
        o.verify(handler).disallow("/shopping/reviewer");
        o.verify(handler).disallow("/about/careers/apply/");
        o.verify(handler).disallow("/about/careers/applications/");
        o.verify(handler).disallow("/landing/signout.html");
        o.verify(handler).disallow("/work/*");
        o.verify(handler).allow("/gb/images");
        o.verify(handler).allow("/gb/js");
        o.verify(handler).siteMap("http://www.gstatic.com/culturalinstitute/sitemaps/www_google_com_culturalinstitute/sitemap-index.xml");
        o.verify(handler).siteMap("http://www.google.com/hostednews/sitemap_index.xml");
        o.verify(handler).siteMap("http://www.google.com/maps/views/sitemap.xml");
        o.verify(handler).siteMap("http://www.google.com/sitemaps_webmasters.xml");
        o.verify(handler).siteMap("http://www.google.com/ventures/sitemap_ventures.xml");
        o.verify(handler).siteMap("http://www.gstatic.com/dictionary/static/sitemaps/sitemap_index.xml");
        o.verify(handler).siteMap("http://www.gstatic.com/earth/gallery/sitemaps/sitemap.xml");
        o.verify(handler).siteMap("http://www.gstatic.com/s2/sitemaps/profiles-sitemap.xml");
        o.verify(handler).siteMap("http://www.gstatic.com/trends/websites/sitemaps/sitemapindex.xml");
        o.verify(handler).endEntry();

        o.verifyNoMoreInteractions();
    }

    @Test
    public void givenWwwBrandwatchCom_whenParse_thenHandlerInteractionsAreExpected() throws IOException, ParseException {
        Reader reader = resourceReader("http_www.brandwatch.com_robots.txt");
        RobotsTxtParser robotsTxtParser = new RobotsTxtParser(reader);
        robotsTxtParser.parse(handler);
        InOrder o = inOrder(handler);

        o.verify(handler).startEntry();
        o.verify(handler).userAgent("iisbot/1.0 (+http://www.iis.net/iisbot.html)");
        o.verify(handler).allow("/");
        o.verify(handler).endEntry();

        o.verify(handler).startEntry();
        o.verify(handler).userAgent("*");
        o.verify(handler).disallow("/wp-admin/");
        o.verify(handler).disallow("/wp-includes/");

        o.verify(handler).siteMap("http://www.brandwatch.com/sitemap.xml.gz");
        o.verify(handler).siteMap("http://www.brandwatch.com/de/sitemap.xml.gz");

        o.verify(handler).endEntry();

        o.verifyNoMoreInteractions();
    }


}
