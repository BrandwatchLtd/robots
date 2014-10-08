package com.brandwatch.robots;

import com.brandwatch.robots.parser.ParseException;
import com.google.common.base.Function;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.annotation.Nullable;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class RobotsBuildingHandlerDataTest extends AbstractDataTest {

    private RobotsBuildingHandler handler;

    public RobotsBuildingHandlerDataTest(String resourceName) {
        super(resourceName);
    }

    @Before
    public void setup() throws IOException {
        RobotExclusionConfig config = mock(RobotExclusionConfig.class);
        when(config.getExpressionCompiler()).thenReturn(new ExpressionCompiler());

        Function<String, Matcher<String>> expressionCompiler = new Function<String, Matcher<String>>() {

            @Nullable
            @Override
            public Matcher<String> apply(String input) {
                return ExpressionCompiler.ALL;
            }
        };

        handler = new RobotsBuildingHandler(expressionCompiler);
    }

    @Test
    public void whenParser_thenHandlerReturnsNonNull() throws IOException, ParseException {
        robotsTxtParser.parse(handler);
        assertThat(handler.get(), notNullValue());
    }

}