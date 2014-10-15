package com.brandwatch.robots;

import java.io.IOException;

public class TemporaryAllow extends IOException {

    private static final long serialVersionUID = -5465749384575181415L;

    public TemporaryAllow() {
    }

    public TemporaryAllow(String s) {
        super(s);
    }

    public TemporaryAllow(String s, Throwable throwable) {
        super(s, throwable);
    }

    public TemporaryAllow(Throwable throwable) {
        super(throwable);
    }
}
