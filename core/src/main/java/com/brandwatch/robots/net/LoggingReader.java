package com.brandwatch.robots.net;

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

    public LoggingReader(@Nonnull Reader reader, @Nonnull Class<?> context, @Nonnull LogLevel level) {
        super(reader);
        this.logger = LoggerFactory.getLogger(checkNotNull(context, "context"));
        this.level = checkNotNull(level, "level is null");
    }

    public LoggingReader(@Nonnull Reader reader, @Nonnull LogLevel level) {
        this(reader, LoggingReader.class, level);
    }

    public LoggingReader(@Nonnull Reader reader, @Nonnull Class<?> context) {
        this(reader, context, LogLevel.DEBUG);
    }

    public LoggingReader(@Nonnull Reader reader) {
        this(reader, LoggingReader.class, LogLevel.DEBUG);
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
