package com.brandwatch.robots.net;

import javax.annotation.Nonnegative;
import javax.annotation.Nullable;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.google.common.base.Preconditions.checkArgument;

public final class ByteLimitedInputStream extends FilterInputStream {

    private final long limit;
    private long count;
    private long markedCount = -1;

    public ByteLimitedInputStream(@Nullable InputStream delegate, @Nonnegative long limit) {
        super(delegate);
        checkArgument(limit >= 0, "limit is negative");
        this.limit = limit;
    }

    @Override
    public int read() throws IOException {
        int result = in.read();
        if (result != -1) {
            ++count;
            checkLimit();
        }
        return result;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int result = in.read(b, off, len);
        if (result != -1) {
            count += result;
            checkLimit();
        }
        return result;
    }

    @Override
    public long skip(long n) throws IOException {
        long result = in.skip(n);
        count += result;
        checkLimit();
        return result;
    }

    @Override
    public synchronized void mark(int readLimit) {
        in.mark(readLimit);
        markedCount = count;
    }

    @Override
    public synchronized void reset() throws IOException {
        in.reset();
        count = markedCount;
    }

    private void checkLimit() throws SizeLimitExceededException {
        if (count > limit) {
            throw new SizeLimitExceededException("Byte count " + count + " exceeds limit " + limit);
        }
    }

}
