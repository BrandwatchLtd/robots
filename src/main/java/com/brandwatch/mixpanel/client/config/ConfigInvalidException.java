package com.brandwatch.mixpanel.client.config;

public class ConfigInvalidException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ConfigInvalidException(String message) {
        super(message);
    }

}
