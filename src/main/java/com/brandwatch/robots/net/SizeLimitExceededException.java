package com.brandwatch.robots.net;

import javax.annotation.Nonnull;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkNotNull;

public class SizeLimitExceededException extends IOException {

    private static final long serialVersionUID = -5529112735532832385L;

    public SizeLimitExceededException() {
    }

    public SizeLimitExceededException(@Nonnull String message) {
        super(checkNotNull(message, "message is null"));
    }

}
