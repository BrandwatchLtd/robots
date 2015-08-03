package com.brandwatch.mixpanel.client.config;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ClientConfigBuilderTest {
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void havingNullProjectToken_whenCreatingClientConfig_thenThrowException() {
        
        int maxBatchSize = 2;
        int maxBatchTime = 2;
                
        thrown.expect(ConfigInvalidException.class);
        thrown.expectMessage("No project token specified"); 
        
        new ClientConfigBuilder()
                .maxBatchSize(maxBatchSize)
                .maxBatchTimeInSeconds(maxBatchTime)
                .build();
        
    }
    
    @Test
    public void havingInvalidMaxBatchSize_whenCreatingClientConfig_thenThrowException() {
        
        String projectToken = "Bla";
        int baxBatchTime = 10;
                
        thrown.expect(ConfigInvalidException.class);
        thrown.expectMessage("No max batch size specified"); 
        
        new ClientConfigBuilder()
                .projectToken(projectToken)
                .maxBatchTimeInSeconds(baxBatchTime)
                .build();
        
    }
    
    @Test
    public void havingInvalidMaxBatchTime_whenCreatingClientConfig_thenThrowException() {
        
        String projectToken = "Bla";
        int maxBatchSize = 10;
                
        thrown.expect(ConfigInvalidException.class);
        thrown.expectMessage("No max batch time specified"); 
        
        new ClientConfigBuilder()
                .projectToken(projectToken)
                .maxBatchSize(maxBatchSize)
                .build();
        
    }
    
    @Test
    public void havingValidArguments_whenCreatingClientConfig_thenClientCongigCreated() {
        
        String projectToken = "Bla";
        int maxBatchSize = 2;
        int maxBatchTime = 2;
        boolean enforceConsistancy = true;
        
        ClientConfig clientConfig = new ClientConfigBuilder()
                .projectToken(projectToken)
                .maxBatchSize(maxBatchSize)
                .maxBatchTimeInSeconds(maxBatchTime)
                .build();
        
        assertThat(clientConfig.getProjectToken(), equalTo(projectToken));
        assertThat(clientConfig.getMaxBatchSize(), equalTo(maxBatchSize));
        assertThat(clientConfig.getMaxBatchTime(), equalTo(maxBatchTime));
        
    }

}
