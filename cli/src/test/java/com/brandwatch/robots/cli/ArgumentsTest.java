package com.brandwatch.robots.cli;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.google.common.base.Charsets;
import org.junit.Before;
import org.junit.Test;

import java.net.IDN;
import java.net.URI;

import static com.brandwatch.robots.cli.TestUtils.$;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ArgumentsTest {

    private static final String FIRST_RESOURCE = "http://www.example.com/index.html";
    private static final String SECOND_RESOURCE = "http://xn--80akhbyknj4f.com/index.html";
    private static final String THIRD_RESOURCE = "http://名がドメイン.com/index.html";

    private static final URI FIRST_RESOURCE_URI = URI.create(FIRST_RESOURCE);
    private static final URI SECOND_RESOURCE_URI = URI.create(SECOND_RESOURCE);
    private static final URI THIRD_RESOURCE_URI = URI.create(IDN.toASCII("http://xn--v8jxj3d1dzdz08w.com/index.html"));

    private Arguments arguments;
    private JCommander jCommander;

    @Before
    public void setup() {
        arguments = new Arguments();
        jCommander = new JCommander(arguments);
    }

    @Test(expected = ParameterException.class)
    public void givenNoArguments_whenInit_throwsParameterException() {
        jCommander.parse();
    }

    @Test
    public void givenHelpArgument_whenInit_thenHelpIsTrue() {
        jCommander.parse($("--help"));
        assertThat(arguments.isHelpRequested(), is(true));
    }

    @Test(expected = ParameterException.class)
    public void givenHelpArgumentGivenTwice_whenInit_thenThrowsParameterException() {
        jCommander.parse($("--help", "--help"));
    }

    @Test(expected = ParameterException.class)
    public void givenBothHelpArgumentNames_whenInit_thenThrowsParameterException() {
        jCommander.parse($("--help", "-h"));
    }

    @Test
    public void givenHArgument_whenInit_thenHelpIsTrue() {
        jCommander.parse($("-h"));
        assertThat(arguments.isHelpRequested(), is(true));
    }

    @Test
    public void givenMaxRedirectHopsArgument_whenInit_thenMaxRedirectHopsIsExpected() {
        jCommander.parse($("--maxRedirectHops", "123", FIRST_RESOURCE));
        assertThat(arguments.getMaxRedirectHops(), equalTo(123));
    }

    @Test
    public void givenRArgument_whenInit_thenMaxRedirectHopsIsExpected() {
        jCommander.parse($("-r", "456", FIRST_RESOURCE));
        assertThat(arguments.getMaxRedirectHops(), equalTo(456));
    }

    @Test
    public void givenZeroRedirectHopsArgument_whenInit_thenMaxRedirectHopsIsExpected() {
        jCommander.parse($("--maxRedirectHops", "0", FIRST_RESOURCE));
        assertThat(arguments.getMaxRedirectHops(), equalTo(0));
    }

    @Test(expected = ParameterException.class)
    public void givenEmptyMaxRedirectHops_whenInit_thenThrowsParameterException() {
        jCommander.parse($("--maxRedirectHops", "", FIRST_RESOURCE));
    }

    @Test(expected = ParameterException.class)
    public void givenNegativeMaxRedirectHops_whenInit_thenThrowsParameterException() {
        jCommander.parse($("--maxRedirectHops", "-1", FIRST_RESOURCE));
    }

    @Test(expected = ParameterException.class)
    public void givenFloatingPointMaxRedirectHops_whenInit_thenThrowsParameterException() {
        jCommander.parse($("--maxRedirectHops", "1.5", FIRST_RESOURCE));
    }

    @Test(expected = ParameterException.class)
    public void givenNonNumericMaxRedirectHops_whenInit_thenThrowsParameterException() {
        jCommander.parse($("--maxRedirectHops", "abc", FIRST_RESOURCE));
    }

    @Test(expected = ParameterException.class)
    public void givenMaxRedirectHopsGivenTwice_whenInit_thenThrowsParameterException() {
        jCommander.parse($("--maxRedirectHops", "1", "--maxRedirectHops", "2", FIRST_RESOURCE));
    }

    @Test(expected = ParameterException.class)
    public void givenBothMaxRedirectHopsNames_whenInit_thenThrowsParameterException() {
        jCommander.parse($("-r", "1", "--maxRedirectHops", "2", FIRST_RESOURCE));
    }

    @Test
    public void givenMaxFileSizeBytesArgument_whenInit_thenMaxFileSizeBytesIsExpected() {
        jCommander.parse($("--maxFileSizeBytes", "123", FIRST_RESOURCE));
        assertThat(arguments.getMaxFileSizeBytes(), equalTo(123));
    }

    @Test
    public void givenSArgument_whenInit_thenMaxFileSizeBytesIsExpected() {
        jCommander.parse($("-s", "456", FIRST_RESOURCE));
        assertThat(arguments.getMaxFileSizeBytes(), equalTo(456));
    }

    @Test
    public void givenZeroMaxFileSizeBytes_whenInit_thenMaxFileSizeBytesIsExpected() {
        jCommander.parse($("--maxFileSizeBytes", "0", FIRST_RESOURCE));
        assertThat(arguments.getMaxFileSizeBytes(), equalTo(0));
    }

    @Test(expected = ParameterException.class)
    public void givenEmptyMaxFileSizeBytes_whenInit_thenThrowsParameterException() {
        jCommander.parse($("--maxFileSizeBytes", "", FIRST_RESOURCE));
    }

    @Test(expected = ParameterException.class)
    public void givenNegativeMaxFileSizeBytes_whenInit_thenThrowsParameterException() {
        jCommander.parse($("--maxFileSizeBytes", "-1", FIRST_RESOURCE));
    }

    @Test(expected = ParameterException.class)
    public void givenNonNumericMaxFileSizeBytes_whenInit_thenThrowsParameterException() {
        jCommander.parse($("--maxFileSizeBytes", "abc", FIRST_RESOURCE));
    }

    @Test(expected = ParameterException.class)
    public void givenNonIntegerMaxFileSizeBytes_whenInit_thenThrowsParameterException() {
        jCommander.parse($("--maxFileSizeBytes", "1.5", FIRST_RESOURCE));
    }

    @Test(expected = ParameterException.class)
    public void givenMaxFileSizeBytesGivenTwice_whenInit_thenThrowsParameterException() {
        jCommander.parse($("--maxFileSizeBytes", "1", "--maxFileSizeBytes", "2", FIRST_RESOURCE));
    }

    @Test(expected = ParameterException.class)
    public void givenBothMaxFileSizeBytesNames_whenInit_thenThrowsParameterException() {
        jCommander.parse($("-s", "1", "--maxFileSizeBytes", "2", FIRST_RESOURCE));
    }

    @Test
    public void givenAArgument_whenInit_thenAgentIsExpected() {
        jCommander.parse($("-a", "funky-bot-1", FIRST_RESOURCE));
        assertThat(arguments.getAgent(), equalTo("funky-bot-1"));
    }

    @Test
    public void givenAgentArgument_whenInit_thenAgentIsExpected() {
        jCommander.parse($("--agent", "funky-bot-2", FIRST_RESOURCE));
        assertThat(arguments.getAgent(), equalTo("funky-bot-2"));
    }

    @Test(expected = ParameterException.class)
    public void givenEmptyAgent_whenInit_thenThrowsParameterException() {
        jCommander.parse($("--agent", "", FIRST_RESOURCE));
    }

    @Test(expected = ParameterException.class)
    public void givenAgentGivenTwice_whenInit_thenThrowsParameterException() {
        jCommander.parse($("--agent", "x", "--agent", "y", FIRST_RESOURCE));
    }

    @Test(expected = ParameterException.class)
    public void givenBothAgentNames_whenInit_thenThrowsParameterException() {
        jCommander.parse($("-a", "x", "--agent", "y", FIRST_RESOURCE));
    }

    @Test
    public void givenCharsetUnset_whenInit_thenCharsetIsUtf8() {
        jCommander.parse($(FIRST_RESOURCE));
        assertThat(arguments.getDefaultCharset(), equalTo(Charsets.UTF_8));
    }

    @Test
    public void givenCArgument_whenInit_thenDefaultCharsetIsExpected() {
        jCommander.parse($("-c", "UTF-8", FIRST_RESOURCE));
        assertThat(arguments.getDefaultCharset(), equalTo(Charsets.UTF_8));
    }

    @Test
    public void givenCharsetArgument_whenInit_thenDefaultCharsetIsExpected() {
        jCommander.parse($("--charset", "UTF-8", FIRST_RESOURCE));
        assertThat(arguments.getDefaultCharset(), equalTo(Charsets.UTF_8));
    }

    @Test
    public void givenDefaultCharsetArgument_whenInit_thenDefaultCharsetIsExpected() {
        jCommander.parse($("--defaultCharset", "UTF-16", FIRST_RESOURCE));
        assertThat(arguments.getDefaultCharset(), equalTo(Charsets.UTF_16));
    }

    @Test(expected = ParameterException.class)
    public void givenInvalidCharsetArgument_whenInit_thenThrowsParameterException() {
        jCommander.parse($("-c", "NOT_A_VALID_CHARSET", FIRST_RESOURCE));
        assertThat(arguments.getDefaultCharset(), equalTo(Charsets.US_ASCII));
    }

    @Test(expected = ParameterException.class)
    public void givenCharsetGivenTwice_whenInit_thenThrowsParameterException() {
        jCommander.parse($("--charset", "UTF-8", "--charset", "UTF-16", FIRST_RESOURCE));
    }

    @Test(expected = ParameterException.class)
    public void givenBothCharsetNames_whenInit_thenThrowsParameterException() {
        jCommander.parse($("--charset", "UTF-8", "-c", "UTF-16", FIRST_RESOURCE));
    }

    @Test(expected = ParameterException.class)
    public void givenEmptyCharset_whenInit_thenThrowsParameterException() {
        jCommander.parse($("--charset", "", FIRST_RESOURCE));
    }

    @Test
    public void givenSingleResource_whenInit_thenResourcesSizeOne() {
        jCommander.parse($(FIRST_RESOURCE));
        assertThat(arguments.getResources(), hasSize(1));
    }

    @Test
    public void givenTwoResources_whenInit_thenResourcesSizeTwo() {
        jCommander.parse($(FIRST_RESOURCE, SECOND_RESOURCE));
        assertThat(arguments.getResources(), hasSize(2));
    }

    @Test
    public void givenThreeResources_whenInit_thenResourcesSizeThree() {
        jCommander.parse($(FIRST_RESOURCE, SECOND_RESOURCE, THIRD_RESOURCE));
        assertThat(arguments.getResources(), hasSize(3));
    }

    @Test
    public void givenSingleResource_whenInit_thenResourcesContainExpected() {
        jCommander.parse($(FIRST_RESOURCE));
        assertThat(arguments.getResources(), contains(FIRST_RESOURCE_URI));
    }

    @Test
    public void givenIDNResource1_whenInit_thenResourcesContainExpected() {
        jCommander.parse($(SECOND_RESOURCE));
        assertThat(arguments.getResources(), contains(SECOND_RESOURCE_URI));
    }

    @Test
    public void givenIDNResource2_whenInit_thenResourcesContainExpected() {
        jCommander.parse($(THIRD_RESOURCE));
        assertThat(arguments.getResources(), contains(THIRD_RESOURCE_URI));
    }

    @Test
    public void givenTwoResources_whenInit_thenResourcesContainExpected() {
        jCommander.parse($(FIRST_RESOURCE, SECOND_RESOURCE));
        assertThat(arguments.getResources(), contains(FIRST_RESOURCE_URI, SECOND_RESOURCE_URI));
    }

    @Test
    public void givenThreeResources_whenInit_thenResourcesContainExpected() {
        jCommander.parse($(FIRST_RESOURCE, SECOND_RESOURCE, THIRD_RESOURCE));
        assertThat(arguments.getResources(), contains(FIRST_RESOURCE_URI, SECOND_RESOURCE_URI, THIRD_RESOURCE_URI));
    }

    @Test(expected = ParameterException.class)
    public void givenEmptyResource_whenInit_thenThrowsParameterException() {
        jCommander.parse($(""));
    }

    @Test(expected = ParameterException.class)
    public void givenResourceMissingScheme_whenInit_thenThrowsParameterException() {
        jCommander.parse($("example.com"));
    }

    @Test(expected = ParameterException.class)
    public void givenResourceMissingScheme2_whenInit_thenThrowsParameterException() {
        jCommander.parse($(":example.com"));
    }

    @Test(expected = ParameterException.class)
    public void givenResourceMissingScheme3_whenInit_thenThrowsParameterException() {
        jCommander.parse($("//:example.com"));
    }

    @Test(expected = ParameterException.class)
    public void givenResourceMissingHostAndScheme_whenInit_thenThrowsParameterException() {
        jCommander.parse($("index.html"));
    }

    @Test(expected = ParameterException.class)
    public void givenResourceMissingHostAndScheme2_whenInit_thenThrowsParameterException() {
        jCommander.parse($(":"));
    }

    @Test(expected = ParameterException.class)
    public void givenResourceMissingHost_whenInit_thenThrowsParameterException() {
        jCommander.parse($("http:"));
    }

    @Test(expected = ParameterException.class)
    public void givenResourceMissingHost2_whenInit_thenThrowsParameterException() {
        jCommander.parse($("http://"));
    }

    @Test(expected = ParameterException.class)
    public void givenResourceMissingHost3_whenInit_thenThrowsParameterException() {
        jCommander.parse($("http://#fragment"));
    }

    @Test(expected = ParameterException.class)
    public void givenResourceMissingHost4_whenInit_thenThrowsParameterException() {
        jCommander.parse($("http://?query"));
    }


}
