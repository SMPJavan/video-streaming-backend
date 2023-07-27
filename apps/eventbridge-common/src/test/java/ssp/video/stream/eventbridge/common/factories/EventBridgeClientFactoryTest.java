package ssp.video.stream.eventbridge.common.factories;

import io.micronaut.aws.sdk.v2.service.AWSServiceConfiguration;
import io.micronaut.aws.ua.UserAgentProvider;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProviderChain;
import software.amazon.awssdk.http.SdkHttpClient;
import software.amazon.awssdk.http.async.SdkAsyncHttpClient;
import software.amazon.awssdk.regions.providers.AwsRegionProviderChain;
import software.amazon.awssdk.services.eventbridge.EventBridgeAsyncClient;
import software.amazon.awssdk.services.eventbridge.EventBridgeAsyncClientBuilder;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import software.amazon.awssdk.services.eventbridge.EventBridgeClientBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class EventBridgeClientFactoryTest {

    @Test
    void should_create_sync_builder() {
        AwsCredentialsProviderChain credentialsProvider = mock(AwsCredentialsProviderChain.class);
        AwsRegionProviderChain regionProvider = mock(AwsRegionProviderChain.class);
        UserAgentProvider userAgentProvider = mock(UserAgentProvider.class);
        AWSServiceConfiguration awsServiceConfiguration = mock(AWSServiceConfiguration.class);
        EventBridgeClientFactory factory = new EventBridgeClientFactory(credentialsProvider, regionProvider, userAgentProvider, awsServiceConfiguration);
        EventBridgeClientBuilder builder = factory.createSyncBuilder();
        assertNotNull(builder);
    }

    @Test
    void should_create_async_builder() {
        AwsCredentialsProviderChain credentialsProvider = mock(AwsCredentialsProviderChain.class);
        AwsRegionProviderChain regionProvider = mock(AwsRegionProviderChain.class);
        UserAgentProvider userAgentProvider = mock(UserAgentProvider.class);
        AWSServiceConfiguration awsServiceConfiguration = mock(AWSServiceConfiguration.class);
        EventBridgeClientFactory factory = new EventBridgeClientFactory(credentialsProvider, regionProvider, userAgentProvider, awsServiceConfiguration);
        EventBridgeAsyncClientBuilder builder = factory.createAsyncBuilder();
        assertNotNull(builder);
    }

    @Test
    void should_sync_builder() {
        AwsCredentialsProviderChain credentialsProvider = mock(AwsCredentialsProviderChain.class);
        AwsRegionProviderChain regionProvider = mock(AwsRegionProviderChain.class);
        UserAgentProvider userAgentProvider = mock(UserAgentProvider.class);
        AWSServiceConfiguration awsServiceConfiguration = mock(AWSServiceConfiguration.class);
        SdkHttpClient httpClient = mock(SdkHttpClient.class);
        EventBridgeClientFactory factory = new EventBridgeClientFactory(credentialsProvider, regionProvider, userAgentProvider, awsServiceConfiguration);
        EventBridgeClientBuilder builder = factory.syncBuilder(httpClient);
        assertNotNull(builder);
    }

    @Test
    void should_sync_client() {
        AwsCredentialsProviderChain credentialsProvider = mock(AwsCredentialsProviderChain.class);
        AwsRegionProviderChain regionProvider = mock(AwsRegionProviderChain.class);
        UserAgentProvider userAgentProvider = mock(UserAgentProvider.class);
        AWSServiceConfiguration awsServiceConfiguration = mock(AWSServiceConfiguration.class);
        EventBridgeClientBuilder builder = mock(EventBridgeClientBuilder.class);
        EventBridgeClient client = mock(EventBridgeClient.class);
        doReturn(client).when(builder).build();
        EventBridgeClientFactory factory = new EventBridgeClientFactory(credentialsProvider, regionProvider, userAgentProvider, awsServiceConfiguration);
        EventBridgeClient result = factory.syncClient(builder);
        assertEquals(client, result);
    }

    @Test
    void should_async_builder() {
        AwsCredentialsProviderChain credentialsProvider = mock(AwsCredentialsProviderChain.class);
        AwsRegionProviderChain regionProvider = mock(AwsRegionProviderChain.class);
        UserAgentProvider userAgentProvider = mock(UserAgentProvider.class);
        AWSServiceConfiguration awsServiceConfiguration = mock(AWSServiceConfiguration.class);
        SdkAsyncHttpClient httpClient = mock(SdkAsyncHttpClient.class);
        EventBridgeClientFactory factory = new EventBridgeClientFactory(credentialsProvider, regionProvider, userAgentProvider, awsServiceConfiguration);
        EventBridgeAsyncClientBuilder builder = factory.asyncBuilder(httpClient);
        assertNotNull(builder);
    }

    @Test
    void should_async_client() {
        AwsCredentialsProviderChain credentialsProvider = mock(AwsCredentialsProviderChain.class);
        AwsRegionProviderChain regionProvider = mock(AwsRegionProviderChain.class);
        UserAgentProvider userAgentProvider = mock(UserAgentProvider.class);
        AWSServiceConfiguration awsServiceConfiguration = mock(AWSServiceConfiguration.class);
        EventBridgeAsyncClientBuilder builder = mock(EventBridgeAsyncClientBuilder.class);
        EventBridgeAsyncClient client = mock(EventBridgeAsyncClient.class);
        doReturn(client).when(builder).build();
        EventBridgeClientFactory factory = new EventBridgeClientFactory(credentialsProvider, regionProvider, userAgentProvider, awsServiceConfiguration);
        EventBridgeAsyncClient result = factory.asyncClient(builder);
        assertEquals(client, result);
    }
}
