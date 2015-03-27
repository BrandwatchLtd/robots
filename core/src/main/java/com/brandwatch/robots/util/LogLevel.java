package com.brandwatch.robots.util;

/*
 * #%L
 * Robots (core)
 * %%
 * Copyright (C) 2015 Brandwatch
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

import org.slf4j.Logger;
import org.slf4j.Marker;

import javax.annotation.Nonnull;

/**
 * Enum of SLF4 logging level constants, allowing the log level to to be parametrized.
 */
public enum LogLevel {

    DISABLED {
        @Override
        public void log(Logger log, String message) {
        }

        @Override
        public void log(Logger log, Marker marker, String msg) {
        }

        @Override
        public void log(Logger log, String format, Object arg) {
        }

        @Override
        public void log(Logger log, String format, Object... arguments) {
        }

        @Override
        public void log(Logger log, String msg, Throwable t) {
        }

        @Override
        public void log(Logger log, Marker marker, String format, Object arg) {
        }

        @Override
        public void log(Logger log, Marker marker, String format,
                        Object... arguments) {
        }

        @Override
        public void log(Logger log, Marker marker, String msg, Throwable t) {
        }

        @Override
        public void log(Logger log, String format, Object arg1, Object arg2) {
        }

        @Override
        public void log(Logger log, Marker marker, String format, Object arg1,
                        Object arg2) {
        }

        @Override
        public boolean isEnabled(Logger log) {
            return false;
        }

        @Override
        public boolean isEnabled(Logger log, Marker marker) {
            return false;
        }
    },
    TRACE {
        @Override
        public void log(@Nonnull Logger log, String message) {
            log.trace(message);
        }

        @Override
        public void log(@Nonnull Logger log, Marker marker, String msg) {
            log.trace(marker, msg);
        }

        @Override
        public void log(@Nonnull Logger log, String format, Object arg) {
            log.trace(format, arg);
        }

        @Override
        public void log(@Nonnull Logger log, String format, Object... arguments) {
            log.trace(format, arguments);
        }

        @Override
        public void log(@Nonnull Logger log, String msg, Throwable t) {
            log.trace(msg, t);
        }

        @Override
        public void log(@Nonnull Logger log, Marker marker, String format, Object arg) {
            log.trace(marker, format, arg);
        }

        @Override
        public void log(@Nonnull Logger log, Marker marker, String format,
                        Object... arguments) {
            log.trace(marker, format, arguments);
        }

        @Override
        public void log(@Nonnull Logger log, Marker marker, String msg, Throwable t) {
            log.trace(marker, msg, t);
        }

        @Override
        public void log(@Nonnull Logger log, String format, Object arg1, Object arg2) {
            log.trace(format, arg1, arg2);
        }

        @Override
        public void log(@Nonnull Logger log, Marker marker, String format, Object arg1,
                        Object arg2) {
            log.trace(marker, format, arg1, arg2);
        }

        @Override
        public boolean isEnabled(@Nonnull Logger log) {
            return log.isTraceEnabled();
        }

        @Override
        public boolean isEnabled(@Nonnull Logger log, Marker marker) {
            return log.isTraceEnabled(marker);
        }
    },
    DEBUG {
        @Override
        public void log(@Nonnull Logger log, String message) {
            log.debug(message);
        }

        @Override
        public void log(@Nonnull Logger log, Marker marker, String msg) {
            log.debug(marker, msg);
        }

        @Override
        public void log(@Nonnull Logger log, String format, Object arg) {
            log.debug(format, arg);
        }

        @Override
        public void log(@Nonnull Logger log, String format, Object... arguments) {
            log.debug(format, arguments);
        }

        @Override
        public void log(@Nonnull Logger log, String msg, Throwable t) {
            log.debug(msg, t);
        }

        @Override
        public void log(@Nonnull Logger log, Marker marker, String format, Object arg) {
            log.debug(marker, format, arg);
        }

        @Override
        public void log(@Nonnull Logger log, Marker marker, String format,
                        Object... arguments) {
            log.debug(marker, format, arguments);
        }

        @Override
        public void log(@Nonnull Logger log, Marker marker, String msg, Throwable t) {
            log.debug(marker, msg, t);
        }

        @Override
        public void log(@Nonnull Logger log, String format, Object arg1, Object arg2) {
            log.debug(format, arg1, arg2);
        }

        @Override
        public void log(@Nonnull Logger log, Marker marker, String format, Object arg1,
                        Object arg2) {
            log.debug(marker, format, arg1, arg2);
        }

        @Override
        public boolean isEnabled(@Nonnull Logger log) {
            return log.isDebugEnabled();
        }

        @Override
        public boolean isEnabled(@Nonnull Logger log, Marker marker) {
            return log.isDebugEnabled(marker);
        }
    },
    INFO {
        @Override
        public void log(@Nonnull Logger log, String message) {
            log.info(message);
        }

        @Override
        public void log(@Nonnull Logger log, Marker marker, String msg) {
            log.info(marker, msg);
        }

        @Override
        public void log(@Nonnull Logger log, String format, Object arg) {
            log.info(format, arg);
        }

        @Override
        public void log(@Nonnull Logger log, String format, Object... arguments) {
            log.info(format, arguments);
        }

        @Override
        public void log(@Nonnull Logger log, String msg, Throwable t) {
            log.info(msg, t);
        }

        @Override
        public void log(@Nonnull Logger log, Marker marker, String format, Object arg) {
            log.info(marker, format, arg);
        }

        @Override
        public void log(@Nonnull Logger log, Marker marker, String format,
                        Object... arguments) {
            log.info(marker, format, arguments);
        }

        @Override
        public void log(@Nonnull Logger log, Marker marker, String msg, Throwable t) {
            log.info(marker, msg, t);
        }

        @Override
        public void log(@Nonnull Logger log, String format, Object arg1, Object arg2) {
            log.info(format, arg1, arg2);
        }

        @Override
        public void log(@Nonnull Logger log, Marker marker, String format, Object arg1,
                        Object arg2) {
            log.info(marker, format, arg1, arg2);
        }

        @Override
        public boolean isEnabled(@Nonnull Logger log) {
            return log.isInfoEnabled();
        }

        @Override
        public boolean isEnabled(@Nonnull Logger log, Marker marker) {
            return log.isInfoEnabled(marker);
        }
    },
    WARNING {
        @Override
        public void log(@Nonnull Logger log, String message) {
            log.warn(message);
        }

        @Override
        public void log(@Nonnull Logger log, Marker marker, String msg) {
            log.warn(marker, msg);
        }

        @Override
        public void log(@Nonnull Logger log, String format, Object arg) {
            log.warn(format, arg);
        }

        @Override
        public void log(@Nonnull Logger log, String format, Object... arguments) {
            log.warn(format, arguments);
        }

        @Override
        public void log(@Nonnull Logger log, String msg, Throwable t) {
            log.warn(msg, t);
        }

        @Override
        public void log(@Nonnull Logger log, Marker marker, String format, Object arg) {
            log.warn(marker, format, arg);
        }

        @Override
        public void log(@Nonnull Logger log, Marker marker, String format,
                        Object... arguments) {
            log.warn(marker, format, arguments);
        }

        @Override
        public void log(@Nonnull Logger log, Marker marker, String msg, Throwable t) {
            log.warn(marker, msg, t);
        }

        @Override
        public void log(@Nonnull Logger log, String format, Object arg1, Object arg2) {
            log.warn(format, arg1, arg2);
        }

        @Override
        public void log(@Nonnull Logger log, Marker marker, String format, Object arg1,
                        Object arg2) {
            log.warn(marker, format, arg1, arg2);
        }

        @Override
        public boolean isEnabled(@Nonnull Logger log) {
            return log.isWarnEnabled();
        }

        @Override
        public boolean isEnabled(@Nonnull Logger log, Marker marker) {
            return log.isWarnEnabled(marker);
        }
    },
    ERROR {
        @Override
        public void log(@Nonnull Logger log, String message) {
            log.error(message);
        }

        @Override
        public void log(@Nonnull Logger log, Marker marker, String msg) {
            log.error(marker, msg);
        }

        @Override
        public void log(@Nonnull Logger log, String format, Object arg) {
            log.error(format, arg);
        }

        @Override
        public void log(@Nonnull Logger log, String format, Object... arguments) {
            log.error(format, arguments);
        }

        @Override
        public void log(@Nonnull Logger log, String msg, Throwable t) {
            log.error(msg, t);
        }

        @Override
        public void log(@Nonnull Logger log, Marker marker, String format, Object arg) {
            log.error(marker, format, arg);
        }

        @Override
        public void log(@Nonnull Logger log, Marker marker, String format,
                        Object... arguments) {
            log.error(marker, format, arguments);
        }

        @Override
        public void log(@Nonnull Logger log, Marker marker, String msg, Throwable t) {
            log.error(marker, msg, t);
        }

        @Override
        public void log(@Nonnull Logger log, String format, Object arg1, Object arg2) {
            log.error(format, arg1, arg2);
        }

        @Override
        public void log(@Nonnull Logger log, Marker marker, String format, Object arg1,
                        Object arg2) {
            log.error(marker, format, arg1, arg2);
        }

        @Override
        public boolean isEnabled(@Nonnull Logger log) {
            return log.isErrorEnabled();
        }

        @Override
        public boolean isEnabled(@Nonnull Logger log, Marker marker) {
            return log.isErrorEnabled(marker);
        }
    };

    public abstract void log(Logger log, String message);

    public abstract void log(Logger log, String format, Object arg);

    public abstract void log(Logger log, String format, Object arg1, Object arg2);

    public abstract void log(Logger log, String format, Object... arguments);

    public abstract void log(Logger log, String msg, Throwable t);

    public abstract void log(Logger log, Marker marker, String msg);

    public abstract void log(Logger log, Marker marker, String format, Object arg);

    public abstract void log(Logger log, Marker marker, String format, Object arg1,
                             Object arg2);

    public abstract void log(Logger log, Marker marker, String format,
                             Object... arguments);

    public abstract void log(Logger log, Marker marker, String msg, Throwable t);

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public abstract boolean isEnabled(Logger log);

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public abstract boolean isEnabled(Logger log, Marker marker);
}