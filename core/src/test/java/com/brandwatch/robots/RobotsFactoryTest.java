package com.brandwatch.robots;

import com.brandwatch.robots.domain.AgentDirective;
import com.brandwatch.robots.domain.Group;
import com.brandwatch.robots.domain.PathDirective;
import com.brandwatch.robots.domain.Robots;
import com.brandwatch.robots.matching.ExpressionCompiler;
import com.brandwatch.robots.matching.MatcherUtils;
import com.brandwatch.robots.net.CharSourceSupplier;
import com.google.common.cache.Cache;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.client.Client;
import java.io.Reader;
import java.net.URI;

import static com.google.common.collect.Iterables.getOnlyElement;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class RobotsFactoryTest {

    private static final RobotsConfig NULL_CONFIG = null;
    private static final Reader NULL_READER = null;

    @Spy
    private RobotsConfig config = new RobotsConfig();
    @Mock
    private RobotsUtilities robotsUtilities;
    @Mock
    private MatcherUtils matcherUtils;
    @InjectMocks
    private RobotsFactory factory;

    @Test(expected = NullPointerException.class)
    public void givenNullConfig_whenNewInstance_thenThrowsNPE() {
        new RobotsFactory(NULL_CONFIG);
    }

    @Test
    public void givenValidConfig_whenNewInstance_thenNoExceptionThrown() {
        new RobotsFactory(config);
    }

    @Test(expected = NullPointerException.class)
    public void givenNullReader_whenCreateRobotsParser_thenThrowsNPE() {
        factory.createRobotsParser(NULL_READER);
    }

    @Test
    public void givenConfig_whenCreateAllowAllRobots_thenReturnsValidRobots() {
        Robots robots = factory.createAllowAllRobots();
        assertRobotsIsValidSingletonWithGivenFieldType(robots, PathDirective.Field.allow);
    }

    @Test
    public void givenConfig_whenCreateDisallowAllRobots_thenReturnsValidRobots() {
        Robots robots = factory.createDisallowAllRobots();
        assertRobotsIsValidSingletonWithGivenFieldType(robots, PathDirective.Field.disallow);
    }

    private void assertRobotsIsValidSingletonWithGivenFieldType(Robots robots, PathDirective.Field expectedField) {
        assertThat(robots, notNullValue());

        assertThat(robots.getGroups(), hasSize(1));
        Group group = getOnlyElement(robots.getGroups());
        assertThat(group, Matchers.notNullValue());

        assertThat(group.getDirectives(AgentDirective.class), hasSize(1));
        AgentDirective agentDirective = getOnlyElement(group.getDirectives(AgentDirective.class));
        assertThat(agentDirective, Matchers.notNullValue());
        assertTrue(agentDirective.getMatcher().matches("abc123"));

        assertThat(group.getDirectives(PathDirective.class), hasSize(1));
        PathDirective pathDirective = getOnlyElement(group.getDirectives(PathDirective.class));
        assertThat(pathDirective, Matchers.notNullValue());
        assertTrue(pathDirective.getMatcher().matches("abc123"));
        assertThat(pathDirective.getFieldValue(), equalTo(expectedField));
    }

    @Test
    public void whenCreateAgentExpressionCompiler_thenReturnsValidExpressionCompiler() {
        ExpressionCompiler compiler = factory.createAgentExpressionCompiler();
        assertThat(compiler, notNullValue());
    }

    @Test
    public void whenCreatePathExpressionCompiler_thenReturnsValidExpressionCompiler() {
        ExpressionCompiler compiler = factory.createPathExpressionCompiler();
        assertThat(compiler, notNullValue());
    }

    @Test
    public void whenCreateCache_thenReturnsValidCache() {
        Cache<URI, Robots> cache = factory.createCache();
        assertThat(cache, notNullValue());
    }

    @Test
    public void whenCreateRobotsBuildingHandler_thenReturnsValidRobotsBuildingParseHandler() {
        RobotsBuildingParseHandler handler = factory.createRobotsBuildingHandler();
        assertThat(handler, notNullValue());
    }

    @Test
    public void whenGetMatcherUtils_thenReturnsExpectedMatcherUtils() {
        MatcherUtils matcherUtils = factory.getMatcherUtils();
        assertThat(matcherUtils, equalTo(this.matcherUtils));
    }

    @Test
    public void whenGetUtilities_thenReturnsExpectedUtilities() {
        RobotsUtilities robotsUtilities = factory.getUtilities();
        assertThat(robotsUtilities, equalTo(this.robotsUtilities));
    }

    @Test
    public void whenCreateCharSourceSupplier_thenReturnsValidCharSourceSupplier() {
        CharSourceSupplier charSourceSupplierities = factory.createCharSourceSupplier();
        assertThat(charSourceSupplierities, notNullValue());
    }

    @Test
    public void whenCreateClient_thenReturnsValidClient() {
        Client client = factory.createClient();
        assertThat(client, notNullValue());
    }
    @Test
    public void whenCreateService_thenReturnsValidService() {
        RobotsService service = factory.createService();
        assertThat(service, notNullValue());
    }

}
