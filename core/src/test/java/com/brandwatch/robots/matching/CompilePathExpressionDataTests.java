package com.brandwatch.robots.matching;

import com.brandwatch.robots.RobotsConfig;
import com.brandwatch.robots.RobotsFactory;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.annotation.Nonnull;
import java.net.URI;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class CompilePathExpressionDataTests {

    private final String pathPattern;
    @Nonnull
    private final URI uri;
    private final boolean expectedResult;

    private final ExpressionCompiler pathExpressionCompiler
            = new RobotsFactory(new RobotsConfig()).createPathExpressionCompiler();

    public CompilePathExpressionDataTests(String pathPattern, @Nonnull String path, boolean expectedResult) {
        this.pathPattern = pathPattern;
        this.uri = URI.create("http://www.example.com" + (path.startsWith("/") ? "" : "/") + path);
        this.expectedResult = expectedResult;
    }

    @Parameterized.Parameters(name = "{index}: {0} {1} {2}")
    public static Iterable<Object[]> data() {
        return ImmutableList.<Object[]>builder()
                .add(new Object[]{"/*", "/path/to/index.html", true})
                .add(new Object[]{"/*", "/index.html", true})
                .add(new Object[]{"/*", "/?query", true})
                .add(new Object[]{"/*", "/#fragment", true})
                .add(new Object[]{"/*", "/", true})
                .add(new Object[]{"/*", "", true})
                .add(new Object[]{"*", "/path/to/index.html", true})
                .add(new Object[]{"*", "/index.html", true})
                .add(new Object[]{"*", "/?query", true})
                .add(new Object[]{"*", "/#fragment", true})
                .add(new Object[]{"*", "/", true})
                .add(new Object[]{"*", "", true})
                .add(new Object[]{"/index.html", "/index.html", true})
                .add(new Object[]{"index.html", "/index.html", true})
                .add(new Object[]{"index.html", "/path/to/index.html", false})
                .add(new Object[]{"/index.html", "/index.html?query=foo", true})
                .add(new Object[]{"/index.html", "/index.html#fragment", true})
                .add(new Object[]{"/index.html", "/index.html.php", true})
                .add(new Object[]{"/index.html$", "/index.html.php", false})
                .add(new Object[]{"/fish", "/fish", true})
                .add(new Object[]{"/fish", "/fish.html", true})
                .add(new Object[]{"/fish", "/fish.html", true})
                .add(new Object[]{"/fish", "/fish/salmon.html", true})
                .add(new Object[]{"/fish", "/fishheads", true})
                .add(new Object[]{"/fish", "/fishheads/yummy.html", true})
                .add(new Object[]{"/fish", "/fish.php?id=anything", true})
                .add(new Object[]{"/fish", "/Fish.asp", false})
                .add(new Object[]{"/fish", "/catfish", false})
                .add(new Object[]{"/fish", "/?id=fish", false})
                .add(new Object[]{"/fish*", "/fish", true})
                .add(new Object[]{"/fish*", "/fish.html", true})
                .add(new Object[]{"/fish*", "/fish.html", true})
                .add(new Object[]{"/fish*", "/fish/salmon.html", true})
                .add(new Object[]{"/fish*", "/fishheads", true})
                .add(new Object[]{"/fish*", "/fishheads/yummy.html", true})
                .add(new Object[]{"/fish*", "/fish.php?id=anything", true})
                .add(new Object[]{"/fish*", "/Fish.asp", false})
                .add(new Object[]{"/fish*", "/catfish", false})
                .add(new Object[]{"/fish*", "/?id=fish", false})
                .add(new Object[]{"/fish/", "/fish/?id=anything", true})
                .add(new Object[]{"/fish/", "/fish/", true})
                .add(new Object[]{"/fish/", "/fish/salmon.htm", true})
                .add(new Object[]{"/fish/", "/fish", false})
                .add(new Object[]{"/fish/", "/fish.html", false})
                .add(new Object[]{"/fish/", "/Fish/Salmon.asp", false})
                .add(new Object[]{"fish/", "/fish/?id=anything", true})
                .add(new Object[]{"fish/", "/fish/", true})
                .add(new Object[]{"fish/", "/fish/salmon.htm", true})
                .add(new Object[]{"fish/", "/fish", false})
                .add(new Object[]{"fish/", "/fish.html", false})
                .add(new Object[]{"fish/", "/Fish/Salmon.asp", false})
                .add(new Object[]{"/*.php", "/filename.php", true})
                .add(new Object[]{"/*.php", "/folder/filename.php", true})
                .add(new Object[]{"/*.php", "/folder/filename.php?parameters", true})
                .add(new Object[]{"/*.php", "/folder/any.php.file.html", true})
                .add(new Object[]{"/*.php", "/filename.php/", true})
                .add(new Object[]{"/*.php", "/", false})
                .add(new Object[]{"/*.php", "/windows.PHP", false})
                .add(new Object[]{"/fish*.php", "/fish.php", true})
                .add(new Object[]{"/fish*.php", "/fishheads/catfish.php?parameters", true})
                .add(new Object[]{"/fish*.php", "/Fish.PHP", false})
                .add(new Object[]{"/*/docs/*.html", "/a/docs/1.html", true})
                .add(new Object[]{"/*/docs/*.html", "/a/docs/2.html", true})
                .add(new Object[]{"/*/docs/*.html", "/b/docs/1.html", true})
                .add(new Object[]{"/*/docs/*.html", "/b/docs/2.html", true})
                .add(new Object[]{"/*/docs/*.html", "/b/docs/2.html.stuff", true})
                .add(new Object[]{"/*/docs/*.html", "/a/b/c/docs/1.html", true})
                .add(new Object[]{"/*/docs/*.html", "/docs/1.html", false})
                .add(new Object[]{"/*/docs/*.html", "/a/docs/1.php", false})
                .add(new Object[]{"/*/docs/*.html", "//docs/1.html", true})
                .add(new Object[]{"*/index.html$", "/index.html", true})
                .add(new Object[]{"/index.htm$", "/index.htm", true})
                .add(new Object[]{"/index.htm$", "/index.html", false})
                .add(new Object[]{"/p", "/page", true})
                .add(new Object[]{"/", "/page", true})
                .add(new Object[]{"/folder/", "/folder/page", true})
                .add(new Object[]{"/folder", "/folder/page", true})
                .add(new Object[]{"/page", "/page.htm", true})
                .add(new Object[]{"/*.htm", "/page.htm", true})
                .add(new Object[]{"/$", "/", true})
                .add(new Object[]{"/", "/", true})
                .add(new Object[]{"/$", "/page.htm", false})
                .add(new Object[]{"/", "/page.htm", true})
                .add(new Object[]{"/************************************", "/index.html", true})
                .add(new Object[]{"/*$", "/index.html", true})
                .build();
    }

    @Test
    public void whenIsPathPatternMatched_thenResultEqualsExpected() {
        Matcher<String> pattern = pathExpressionCompiler.compile(pathPattern);
        boolean result = pattern.matches(uri.getPath());
        assertThat(result, equalTo(expectedResult));
    }

}
