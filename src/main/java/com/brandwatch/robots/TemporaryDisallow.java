package com.brandwatch.robots;

import java.io.IOException;

public class TemporaryDisallow extends IOException {

    private static final long serialVersionUID = -4504038118861545829L;

    public TemporaryDisallow() {
    }

    public TemporaryDisallow(String s) {
        super(s);
    }

    public TemporaryDisallow(String s, Throwable throwable) {
        super(s, throwable);
    }

    public TemporaryDisallow(Throwable throwable) {
        super(throwable);
    }
}
