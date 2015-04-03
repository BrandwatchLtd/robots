package com.brandwatch.robots.net;

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

import com.brandwatch.robots.util.LogLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.Math.max;

public class LoggingReader extends FilterReader {

    @Nonnull
    private final LogLevel level;
    @Nonnull
    private final Logger logger;

    LoggingReader(@Nonnull Reader reader, Logger logger, @Nonnull LogLevel level) {
        super(reader);
        this.logger = checkNotNull(logger, "logger");
        this.level = checkNotNull(level, "level is null");
    }

    public LoggingReader(@Nonnull Reader reader, @Nonnull Class<?> context, @Nonnull LogLevel level) {
        this(reader, LoggerFactory.getLogger(checkNotNull(context, "context")), level);
    }

    @Override
    public int read() throws IOException {
        final int value = super.read();
        level.log(logger, "read() : {}", value);
        return value;
    }

    @Override
    public int read(char[] chars, int off, int len) throws IOException {
        final int count = super.read(chars, off, len);
        if (level.isEnabled(logger)) {
            level.log(logger, "read(char[], {}, {}) : {} (\"{}\")", off, len, count,
                    String.copyValueOf(chars, off, max(count, 0)));
        }
        return count;
    }

    @Override
    public long skip(long len) throws IOException {
        final long count = super.skip(len);
        level.log(logger, "skip({}) : {}", len, count);
        return count;
    }

    @Override
    public boolean ready() throws IOException {
        final boolean ready = super.ready();
        level.log(logger, "ready() : {}", ready);
        return ready;
    }

    @Override
    public boolean markSupported() {
        final boolean markSupported = super.markSupported();
        level.log(logger, "markSupported() : {}", markSupported);
        return markSupported;
    }

    @Override
    public void mark(int readAheadLimit) throws IOException {
        level.log(logger, "mark({})", readAheadLimit);
        super.mark(readAheadLimit);
    }

    @Override
    public void reset() throws IOException {
        level.log(logger, "reset()");
        super.reset();
    }

    @Override
    public void close() throws IOException {
        level.log(logger, "close()");
        super.close();
    }
}
