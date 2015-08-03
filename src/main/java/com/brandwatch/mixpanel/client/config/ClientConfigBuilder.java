package com.brandwatch.mixpanel.client.config;

public class ClientConfigBuilder {

    private String projectToken;
    private int maxBatchSize = -1;
    private int maxBatchTime = -1;

    public ClientConfigBuilder() {
    }

    public ClientConfigBuilder projectToken(String projectToken) {
        this.projectToken = projectToken;
        return this;
    }

    public ClientConfigBuilder maxBatchSize(int maxBatchSize) {
        this.maxBatchSize = maxBatchSize;
        return this;
    }

    public ClientConfigBuilder maxBatchTimeInSeconds(int maxBatchTime) {
        this.maxBatchTime = maxBatchTime;
        return this;
    }

    public ClientConfig build() {
        validateConfig();
        return new ClientConfig(projectToken, maxBatchSize, maxBatchTime);
    }

    private void validateConfig() throws ConfigInvalidException {
        if (projectToken == null) {
            throw new ConfigInvalidException("No project token specified");
        }

        if (maxBatchSize < 0) {
            throw new ConfigInvalidException("No max batch size specified");
        }

        if (maxBatchTime < 0) {
            throw new ConfigInvalidException("No max batch time specified");
        }
    }

}
