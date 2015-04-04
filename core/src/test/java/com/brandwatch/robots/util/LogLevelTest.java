package com.brandwatch.robots.util;

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

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.Marker;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LogLevelTest {

    private static final String FORMAT = "Some log format {}";
    private static final String MESSAGE = "Some log message.";
    private static final Object FIRST_ARG = new Object();
    private static final Object SECOND_ARG = new Object();
    private static final Object THIRD_ARG = new Object();

    @Mock
    private Logger mockLogger;
    @Mock
    private Marker mockMarker;
    @Mock
    private Throwable mockThrowable;

    @After
    public void checkWeFoundAllTheInteractions() {
        verifyNoMoreInteractions(mockLogger, mockMarker);
    }

    @Test
    public void givenDisabledLevel_whenLogWithMessage_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.DISABLED;
        level.log(mockLogger, MESSAGE);
        verifyNoMoreInteractions(mockLogger);
    }

    @Test
    public void givenDisabledLevel_whenLogWithFormatAndSingleArg_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.DISABLED;
        level.log(mockLogger, FORMAT, FIRST_ARG);
        verifyNoMoreInteractions(mockLogger);
    }

    @Test
    public void givenDisabledLevel_whenLogWithFormatAndTwoArgs_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.DISABLED;
        level.log(mockLogger, FORMAT, FIRST_ARG, SECOND_ARG);
        verifyNoMoreInteractions(mockLogger);
    }

    @Test
    public void givenDisabledLevel_whenLogWithFormatAndThreeArgs_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.DISABLED;
        level.log(mockLogger, FORMAT, FIRST_ARG, SECOND_ARG, THIRD_ARG);
        verifyNoMoreInteractions(mockLogger);
    }

    @Test
    public void givenDisabledLevel_whenLogWithMessageAndThrowable_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.DISABLED;
        level.log(mockLogger, MESSAGE, mockThrowable);
        verifyNoMoreInteractions(mockLogger);
    }

    @Test
    public void givenDisabledLevel_whenLogWithMarkerAndMessage_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.DISABLED;
        level.log(mockLogger, mockMarker, MESSAGE);
        verifyNoMoreInteractions(mockLogger);
    }


    @Test
    public void givenDisabledLevel_whenLogWithMarkerAndFormatAndSingleArg_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.DISABLED;
        level.log(mockLogger, mockMarker, FORMAT, FIRST_ARG);
        verifyNoMoreInteractions(mockLogger);
    }

    @Test
    public void givenDisabledLevel_whenLogWithMarkerAndFormatAndTwoArgs_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.DISABLED;
        level.log(mockLogger, mockMarker, FORMAT, FIRST_ARG, SECOND_ARG);
        verifyNoMoreInteractions(mockLogger);
    }

    @Test
    public void givenDisabledLevel_whenLogWithMarkerAndFormatAndThreeArgs_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.DISABLED;
        level.log(mockLogger, mockMarker, FORMAT, FIRST_ARG, SECOND_ARG, THIRD_ARG);
        verifyNoMoreInteractions(mockLogger);
    }

    @Test
    public void givenDisabledLevel_whenLogWithMarkerAndMessageAndThrowable_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.DISABLED;
        level.log(mockLogger, mockMarker, MESSAGE, mockThrowable);
        verifyNoMoreInteractions(mockLogger);
    }

    @Test
    public void givenDisabledLevel_whenIsEnabled_thenReturnsFalse() throws Exception {
        LogLevel level = LogLevel.DISABLED;
        boolean result = level.isEnabled(mockLogger);
        assertThat(result, is(false));
    }

    @Test
    public void givenDisabledLevel_whenIsEnabledWithMarker_thenReturnsFalse() throws Exception {
        LogLevel level = LogLevel.DISABLED;
        boolean result = level.isEnabled(mockLogger, mockMarker);
        assertThat(result, is(false));
    }

    @Test
    public void givenTraceLevel_whenLogWithMessage_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.TRACE;
        level.log(mockLogger, MESSAGE);
        verify(mockLogger, only()).trace(MESSAGE);
    }

    @Test
    public void givenTraceLevel_whenLogWithFormatAndSingleArg_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.TRACE;
        level.log(mockLogger, FORMAT, FIRST_ARG);
        verify(mockLogger, only()).trace(FORMAT, FIRST_ARG);
    }

    @Test
    public void givenTraceLevel_whenLogWithFormatAndTwoArgs_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.TRACE;
        level.log(mockLogger, FORMAT, FIRST_ARG, SECOND_ARG);
        verify(mockLogger, only()).trace(FORMAT, FIRST_ARG, SECOND_ARG);

    }

    @Test
    public void givenTraceLevel_whenLogWithFormatAndThreeArgs_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.TRACE;
        level.log(mockLogger, FORMAT, FIRST_ARG, SECOND_ARG, THIRD_ARG);
        verify(mockLogger, only()).trace(FORMAT, FIRST_ARG, SECOND_ARG, THIRD_ARG);

    }

    @Test
    public void givenTraceLevel_whenLogWithMessageAndThrowable_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.TRACE;
        level.log(mockLogger, MESSAGE, mockThrowable);
        verify(mockLogger, only()).trace(MESSAGE, mockThrowable);
    }

    @Test
    public void givenTraceLevel_whenLogWithMarkerAndMessage_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.TRACE;
        level.log(mockLogger, mockMarker, MESSAGE);
        verify(mockLogger, only()).trace(mockMarker, MESSAGE);
    }


    @Test
    public void givenTraceLevel_whenLogWithMarkerAndFormatAndSingleArg_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.TRACE;
        level.log(mockLogger, mockMarker, FORMAT, FIRST_ARG);
        verify(mockLogger, only()).trace(mockMarker, FORMAT, FIRST_ARG);
    }

    @Test
    public void givenTraceLevel_whenLogWithMarkerAndFormatAndTwoArgs_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.TRACE;
        level.log(mockLogger, mockMarker, FORMAT, FIRST_ARG, SECOND_ARG);
        verify(mockLogger, only()).trace(mockMarker, FORMAT, FIRST_ARG, SECOND_ARG);

    }

    @Test
    public void givenTraceLevel_whenLogWithMarkerAndFormatAndThreeArgs_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.TRACE;
        level.log(mockLogger, mockMarker, FORMAT, FIRST_ARG, SECOND_ARG, THIRD_ARG);
        verify(mockLogger, only()).trace(mockMarker, FORMAT, FIRST_ARG, SECOND_ARG, THIRD_ARG);

    }

    @Test
    public void givenTraceLevel_whenLogWithMarkerAndMessageAndThrowable_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.TRACE;
        level.log(mockLogger, mockMarker, MESSAGE, mockThrowable);
        verify(mockLogger, only()).trace(mockMarker, MESSAGE, mockThrowable);
    }

    @Test
    public void givenTraceEnabled_whenIsEnabled_thenReturnsTrue() throws Exception {
        LogLevel level = LogLevel.TRACE;
        when(mockLogger.isTraceEnabled()).thenReturn(true);
        boolean result = level.isEnabled(mockLogger);
        assertThat(result, is(true));
        verify(mockLogger, only()).isTraceEnabled();
    }

    @Test
    public void givenTraceDisabled_whenIsEnabled_thenReturnsFalse() throws Exception {
        LogLevel level = LogLevel.TRACE;
        when(mockLogger.isTraceEnabled()).thenReturn(false);
        boolean result = level.isEnabled(mockLogger);
        assertThat(result, is(false));
        verify(mockLogger, only()).isTraceEnabled();
    }

    @Test
    public void givenTraceEnabled_whenIsEnabledWithMarker_thenReturnsTrue() throws Exception {
        LogLevel level = LogLevel.TRACE;
        when(mockLogger.isTraceEnabled(mockMarker)).thenReturn(true);
        boolean result = level.isEnabled(mockLogger, mockMarker);
        assertThat(result, is(true));
        verify(mockLogger, only()).isTraceEnabled(mockMarker);
    }

    @Test
    public void givenTraceDisabled_whenIsEnabledWithMarker_thenReturnsFalse() throws Exception {
        LogLevel level = LogLevel.TRACE;
        when(mockLogger.isTraceEnabled(mockMarker)).thenReturn(false);
        boolean result = level.isEnabled(mockLogger, mockMarker);
        assertThat(result, is(false));
        verify(mockLogger, only()).isTraceEnabled(mockMarker);
    }

    @Test
    public void givenDebugLevel_whenLogWithMessage_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.DEBUG;
        level.log(mockLogger, MESSAGE);
        verify(mockLogger, only()).debug(MESSAGE);
    }

    @Test
    public void givenDebugLevel_whenLogWithFormatAndSingleArg_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.DEBUG;
        level.log(mockLogger, FORMAT, FIRST_ARG);
        verify(mockLogger, only()).debug(FORMAT, FIRST_ARG);
    }

    @Test
    public void givenDebugLevel_whenLogWithFormatAndTwoArgs_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.DEBUG;
        level.log(mockLogger, FORMAT, FIRST_ARG, SECOND_ARG);
        verify(mockLogger, only()).debug(FORMAT, FIRST_ARG, SECOND_ARG);

    }

    @Test
    public void givenDebugLevel_whenLogWithFormatAndThreeArgs_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.DEBUG;
        level.log(mockLogger, FORMAT, FIRST_ARG, SECOND_ARG, THIRD_ARG);
        verify(mockLogger, only()).debug(FORMAT, FIRST_ARG, SECOND_ARG, THIRD_ARG);

    }

    @Test
    public void givenDebugLevel_whenLogWithMessageAndThrowable_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.DEBUG;
        level.log(mockLogger, MESSAGE, mockThrowable);
        verify(mockLogger, only()).debug(MESSAGE, mockThrowable);
    }

    @Test
    public void givenDebugLevel_whenLogWithMarkerAndMessage_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.DEBUG;
        level.log(mockLogger, mockMarker, MESSAGE);
        verify(mockLogger, only()).debug(mockMarker, MESSAGE);
    }

    @Test
    public void givenDebugLevel_whenLogWithMarkerAndFormatAndSingleArg_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.DEBUG;
        level.log(mockLogger, mockMarker, FORMAT, FIRST_ARG);
        verify(mockLogger, only()).debug(mockMarker, FORMAT, FIRST_ARG);
    }

    @Test
    public void givenDebugLevel_whenLogWithMarkerAndFormatAndTwoArgs_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.DEBUG;
        level.log(mockLogger, mockMarker, FORMAT, FIRST_ARG, SECOND_ARG);
        verify(mockLogger, only()).debug(mockMarker, FORMAT, FIRST_ARG, SECOND_ARG);

    }

    @Test
    public void givenDebugLevel_whenLogWithMarkerAndFormatAndThreeArgs_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.DEBUG;
        level.log(mockLogger, mockMarker, FORMAT, FIRST_ARG, SECOND_ARG, THIRD_ARG);
        verify(mockLogger, only()).debug(mockMarker, FORMAT, FIRST_ARG, SECOND_ARG, THIRD_ARG);

    }

    @Test
    public void givenDebugLevel_whenLogWithMarkerAndMessageAndThrowable_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.DEBUG;
        level.log(mockLogger, mockMarker, MESSAGE, mockThrowable);
        verify(mockLogger, only()).debug(mockMarker, MESSAGE, mockThrowable);
    }

    @Test
    public void givenDebugEnabled_whenIsEnabled_thenReturnsTrue() throws Exception {
        LogLevel level = LogLevel.DEBUG;
        when(mockLogger.isDebugEnabled()).thenReturn(true);
        boolean result = level.isEnabled(mockLogger);
        assertThat(result, is(true));
        verify(mockLogger, only()).isDebugEnabled();
    }

    @Test
    public void givenDebugDisabled_whenIsEnabled_thenReturnsFalse() throws Exception {
        LogLevel level = LogLevel.DEBUG;
        when(mockLogger.isDebugEnabled()).thenReturn(false);
        boolean result = level.isEnabled(mockLogger);
        assertThat(result, is(false));
        verify(mockLogger, only()).isDebugEnabled();
    }

    @Test
    public void givenDebugEnabled_whenIsEnabledWithMarker_thenReturnsTrue() throws Exception {
        LogLevel level = LogLevel.DEBUG;
        when(mockLogger.isDebugEnabled(mockMarker)).thenReturn(true);
        boolean result = level.isEnabled(mockLogger, mockMarker);
        assertThat(result, is(true));
        verify(mockLogger, only()).isDebugEnabled(mockMarker);
    }

    @Test
    public void givenDebugDisabled_whenIsEnabledWithMarker_thenReturnsFalse() throws Exception {
        LogLevel level = LogLevel.DEBUG;
        when(mockLogger.isDebugEnabled(mockMarker)).thenReturn(false);
        boolean result = level.isEnabled(mockLogger, mockMarker);
        assertThat(result, is(false));
        verify(mockLogger, only()).isDebugEnabled(mockMarker);
    }


    @Test
    public void givenInfoLevel_whenLogWithMessage_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.INFO;
        level.log(mockLogger, MESSAGE);
        verify(mockLogger, only()).info(MESSAGE);
    }

    @Test
    public void givenInfoLevel_whenLogWithFormatAndSingleArg_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.INFO;
        level.log(mockLogger, FORMAT, FIRST_ARG);
        verify(mockLogger, only()).info(FORMAT, FIRST_ARG);
    }

    @Test
    public void givenInfoLevel_whenLogWithFormatAndTwoArgs_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.INFO;
        level.log(mockLogger, FORMAT, FIRST_ARG, SECOND_ARG);
        verify(mockLogger, only()).info(FORMAT, FIRST_ARG, SECOND_ARG);

    }

    @Test
    public void givenInfoLevel_whenLogWithFormatAndThreeArgs_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.INFO;
        level.log(mockLogger, FORMAT, FIRST_ARG, SECOND_ARG, THIRD_ARG);
        verify(mockLogger, only()).info(FORMAT, FIRST_ARG, SECOND_ARG, THIRD_ARG);

    }

    @Test
    public void givenInfoLevel_whenLogWithMessageAndThrowable_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.INFO;
        level.log(mockLogger, MESSAGE, mockThrowable);
        verify(mockLogger, only()).info(MESSAGE, mockThrowable);
    }

    @Test
    public void givenInfoLevel_whenLogWithMarkerAndMessage_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.INFO;
        level.log(mockLogger, mockMarker, MESSAGE);
        verify(mockLogger, only()).info(mockMarker, MESSAGE);
    }


    @Test
    public void givenInfoLevel_whenLogWithMarkerAndFormatAndSingleArg_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.INFO;
        level.log(mockLogger, mockMarker, FORMAT, FIRST_ARG);
        verify(mockLogger, only()).info(mockMarker, FORMAT, FIRST_ARG);
    }

    @Test
    public void givenInfoLevel_whenLogWithMarkerAndFormatAndTwoArgs_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.INFO;
        level.log(mockLogger, mockMarker, FORMAT, FIRST_ARG, SECOND_ARG);
        verify(mockLogger, only()).info(mockMarker, FORMAT, FIRST_ARG, SECOND_ARG);

    }

    @Test
    public void givenInfoLevel_whenLogWithMarkerAndFormatAndThreeArgs_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.INFO;
        level.log(mockLogger, mockMarker, FORMAT, FIRST_ARG, SECOND_ARG, THIRD_ARG);
        verify(mockLogger, only()).info(mockMarker, FORMAT, FIRST_ARG, SECOND_ARG, THIRD_ARG);

    }

    @Test
    public void givenInfoLevel_whenLogWithMarkerAndMessageAndThrowable_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.INFO;
        level.log(mockLogger, mockMarker, MESSAGE, mockThrowable);
        verify(mockLogger, only()).info(mockMarker, MESSAGE, mockThrowable);
    }

    @Test
    public void givenInfoEnabled_whenIsEnabled_thenReturnsTrue() throws Exception {
        LogLevel level = LogLevel.INFO;
        when(mockLogger.isInfoEnabled()).thenReturn(true);
        boolean result = level.isEnabled(mockLogger);
        assertThat(result, is(true));
        verify(mockLogger, only()).isInfoEnabled();
    }

    @Test
    public void givenInfoDisabled_whenIsEnabled_thenReturnsFalse() throws Exception {
        LogLevel level = LogLevel.INFO;
        when(mockLogger.isInfoEnabled()).thenReturn(false);
        boolean result = level.isEnabled(mockLogger);
        assertThat(result, is(false));
        verify(mockLogger, only()).isInfoEnabled();
    }

    @Test
    public void givenInfoEnabled_whenIsEnabledWithMarker_thenReturnsTrue() throws Exception {
        LogLevel level = LogLevel.INFO;
        when(mockLogger.isInfoEnabled(mockMarker)).thenReturn(true);
        boolean result = level.isEnabled(mockLogger, mockMarker);
        assertThat(result, is(true));
        verify(mockLogger, only()).isInfoEnabled(mockMarker);
    }

    @Test
    public void givenInfoDisabled_whenIsEnabledWithMarker_thenReturnsFalse() throws Exception {
        LogLevel level = LogLevel.INFO;
        when(mockLogger.isInfoEnabled(mockMarker)).thenReturn(false);
        boolean result = level.isEnabled(mockLogger, mockMarker);
        assertThat(result, is(false));
        verify(mockLogger, only()).isInfoEnabled(mockMarker);
    }


    @Test
    public void givenWarnLevel_whenLogWithMessage_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.WARNING;
        level.log(mockLogger, MESSAGE);
        verify(mockLogger, only()).warn(MESSAGE);
    }

    @Test
    public void givenWarnLevel_whenLogWithFormatAndSingleArg_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.WARNING;
        level.log(mockLogger, FORMAT, FIRST_ARG);
        verify(mockLogger, only()).warn(FORMAT, FIRST_ARG);
    }

    @Test
    public void givenWarnLevel_whenLogWithFormatAndTwoArgs_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.WARNING;
        level.log(mockLogger, FORMAT, FIRST_ARG, SECOND_ARG);
        verify(mockLogger, only()).warn(FORMAT, FIRST_ARG, SECOND_ARG);

    }

    @Test
    public void givenWarnLevel_whenLogWithFormatAndThreeArgs_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.WARNING;
        level.log(mockLogger, FORMAT, FIRST_ARG, SECOND_ARG, THIRD_ARG);
        verify(mockLogger, only()).warn(FORMAT, FIRST_ARG, SECOND_ARG, THIRD_ARG);

    }

    @Test
    public void givenWarnLevel_whenLogWithMessageAndThrowable_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.WARNING;
        level.log(mockLogger, MESSAGE, mockThrowable);
        verify(mockLogger, only()).warn(MESSAGE, mockThrowable);
    }

    @Test
    public void givenWarnLevel_whenLogWithMarkerAndMessage_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.WARNING;
        level.log(mockLogger, mockMarker, MESSAGE);
        verify(mockLogger, only()).warn(mockMarker, MESSAGE);
    }


    @Test
    public void givenWarnLevel_whenLogWithMarkerAndFormatAndSingleArg_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.WARNING;
        level.log(mockLogger, mockMarker, FORMAT, FIRST_ARG);
        verify(mockLogger, only()).warn(mockMarker, FORMAT, FIRST_ARG);
    }

    @Test
    public void givenWarnLevel_whenLogWithMarkerAndFormatAndTwoArgs_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.WARNING;
        level.log(mockLogger, mockMarker, FORMAT, FIRST_ARG, SECOND_ARG);
        verify(mockLogger, only()).warn(mockMarker, FORMAT, FIRST_ARG, SECOND_ARG);

    }

    @Test
    public void givenWarnLevel_whenLogWithMarkerAndFormatAndThreeArgs_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.WARNING;
        level.log(mockLogger, mockMarker, FORMAT, FIRST_ARG, SECOND_ARG, THIRD_ARG);
        verify(mockLogger, only()).warn(mockMarker, FORMAT, FIRST_ARG, SECOND_ARG, THIRD_ARG);

    }

    @Test
    public void givenWarnLevel_whenLogWithMarkerAndMessageAndThrowable_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.WARNING;
        level.log(mockLogger, mockMarker, MESSAGE, mockThrowable);
        verify(mockLogger, only()).warn(mockMarker, MESSAGE, mockThrowable);
    }

    @Test
    public void givenWarnEnabled_whenIsEnabled_thenReturnsTrue() throws Exception {
        LogLevel level = LogLevel.WARNING;
        when(mockLogger.isWarnEnabled()).thenReturn(true);
        boolean result = level.isEnabled(mockLogger);
        assertThat(result, is(true));
        verify(mockLogger, only()).isWarnEnabled();
    }

    @Test
    public void givenWarnDisabled_whenIsEnabled_thenReturnsFalse() throws Exception {
        LogLevel level = LogLevel.WARNING;
        when(mockLogger.isWarnEnabled()).thenReturn(false);
        boolean result = level.isEnabled(mockLogger);
        assertThat(result, is(false));
        verify(mockLogger, only()).isWarnEnabled();
    }

    @Test
    public void givenWarnEnabled_whenIsEnabledWithMarker_thenReturnsTrue() throws Exception {
        LogLevel level = LogLevel.WARNING;
        when(mockLogger.isWarnEnabled(mockMarker)).thenReturn(true);
        boolean result = level.isEnabled(mockLogger, mockMarker);
        assertThat(result, is(true));
        verify(mockLogger, only()).isWarnEnabled(mockMarker);
    }

    @Test
    public void givenWarnDisabled_whenIsEnabledWithMarker_thenReturnsFalse() throws Exception {
        LogLevel level = LogLevel.WARNING;
        when(mockLogger.isWarnEnabled(mockMarker)).thenReturn(false);
        boolean result = level.isEnabled(mockLogger, mockMarker);
        assertThat(result, is(false));
        verify(mockLogger, only()).isWarnEnabled(mockMarker);
    }


    @Test
    public void givenErrorLevel_whenLogWithMessage_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.ERROR;
        level.log(mockLogger, MESSAGE);
        verify(mockLogger, only()).error(MESSAGE);
    }

    @Test
    public void givenErrorLevel_whenLogWithFormatAndSingleArg_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.ERROR;
        level.log(mockLogger, FORMAT, FIRST_ARG);
        verify(mockLogger, only()).error(FORMAT, FIRST_ARG);
    }

    @Test
    public void givenErrorLevel_whenLogWithFormatAndTwoArgs_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.ERROR;
        level.log(mockLogger, FORMAT, FIRST_ARG, SECOND_ARG);
        verify(mockLogger, only()).error(FORMAT, FIRST_ARG, SECOND_ARG);

    }

    @Test
    public void givenErrorLevel_whenLogWithFormatAndThreeArgs_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.ERROR;
        level.log(mockLogger, FORMAT, FIRST_ARG, SECOND_ARG, THIRD_ARG);
        verify(mockLogger, only()).error(FORMAT, FIRST_ARG, SECOND_ARG, THIRD_ARG);

    }

    @Test
    public void givenErrorLevel_whenLogWithMessageAndThrowable_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.ERROR;
        level.log(mockLogger, MESSAGE, mockThrowable);
        verify(mockLogger, only()).error(MESSAGE, mockThrowable);
    }

    @Test
    public void givenErrorLevel_whenLogWithMarkerAndMessage_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.ERROR;
        level.log(mockLogger, mockMarker, MESSAGE);
        verify(mockLogger, only()).error(mockMarker, MESSAGE);
    }


    @Test
    public void givenErrorLevel_whenLogWithMarkerAndFormatAndSingleArg_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.ERROR;
        level.log(mockLogger, mockMarker, FORMAT, FIRST_ARG);
        verify(mockLogger, only()).error(mockMarker, FORMAT, FIRST_ARG);
    }

    @Test
    public void givenErrorLevel_whenLogWithMarkerAndFormatAndTwoArgs_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.ERROR;
        level.log(mockLogger, mockMarker, FORMAT, FIRST_ARG, SECOND_ARG);
        verify(mockLogger, only()).error(mockMarker, FORMAT, FIRST_ARG, SECOND_ARG);

    }

    @Test
    public void givenErrorLevel_whenLogWithMarkerAndFormatAndThreeArgs_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.ERROR;
        level.log(mockLogger, mockMarker, FORMAT, FIRST_ARG, SECOND_ARG, THIRD_ARG);
        verify(mockLogger, only()).error(mockMarker, FORMAT, FIRST_ARG, SECOND_ARG, THIRD_ARG);

    }

    @Test
    public void givenErrorLevel_whenLogWithMarkerAndMessageAndThrowable_thenExpectedMethodInvoked() throws Exception {
        LogLevel level = LogLevel.ERROR;
        level.log(mockLogger, mockMarker, MESSAGE, mockThrowable);
        verify(mockLogger, only()).error(mockMarker, MESSAGE, mockThrowable);
    }

    @Test
    public void givenErrorEnabled_whenIsEnabled_thenReturnsTrue() throws Exception {
        LogLevel level = LogLevel.ERROR;
        when(mockLogger.isErrorEnabled()).thenReturn(true);
        boolean result = level.isEnabled(mockLogger);
        assertThat(result, is(true));
        verify(mockLogger, only()).isErrorEnabled();
    }

    @Test
    public void givenErrorDisabled_whenIsEnabled_thenReturnsFalse() throws Exception {
        LogLevel level = LogLevel.ERROR;
        when(mockLogger.isErrorEnabled()).thenReturn(false);
        boolean result = level.isEnabled(mockLogger);
        assertThat(result, is(false));
        verify(mockLogger, only()).isErrorEnabled();
    }

    @Test
    public void givenErrorEnabled_whenIsEnabledWithMarker_thenReturnsTrue() throws Exception {
        LogLevel level = LogLevel.ERROR;
        when(mockLogger.isErrorEnabled(mockMarker)).thenReturn(true);
        boolean result = level.isEnabled(mockLogger, mockMarker);
        assertThat(result, is(true));
        verify(mockLogger, only()).isErrorEnabled(mockMarker);
    }

    @Test
    public void givenErrorDisabled_whenIsEnabledWithMarker_thenReturnsFalse() throws Exception {
        LogLevel level = LogLevel.ERROR;
        when(mockLogger.isErrorEnabled(mockMarker)).thenReturn(false);
        boolean result = level.isEnabled(mockLogger, mockMarker);
        assertThat(result, is(false));
        verify(mockLogger, only()).isErrorEnabled(mockMarker);
    }

}
