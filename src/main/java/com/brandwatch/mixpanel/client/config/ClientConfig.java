package com.brandwatch.mixpanel.client.config;

public class ClientConfig {

    private final String projectToken;
    private final int maxBatchSize;
    private final int maxBatchTime;

    ClientConfig(String projectToken, int maxBatchSize, int maxBatchTime) {
        this.projectToken = projectToken;
        this.maxBatchSize = maxBatchSize;
        this.maxBatchTime = maxBatchTime;
    }

    public String getProjectToken() {
        return projectToken;
    }

    public int getMaxBatchSize() {
        return maxBatchSize;
    }

    public int getMaxBatchTime() {
        return maxBatchTime;
    }

}
