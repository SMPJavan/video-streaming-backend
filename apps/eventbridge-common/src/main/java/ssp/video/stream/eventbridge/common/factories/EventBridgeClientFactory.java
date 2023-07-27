package ssp.video.stream.eventbridge.common.factories;

import io.micronaut.aws.sdk.v2.service.AWSServiceConfiguration;
import io.micronaut.aws.sdk.v2.service.AwsClientFactory;
import io.micronaut.aws.ua.UserAgentProvider;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.Nullable;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProviderChain;
import software.amazon.awssdk.http.SdkHttpClient;
import software.amazon.awssdk.http.async.SdkAsyncHttpClient;
import software.amazon.awssdk.regions.providers.AwsRegionProviderChain;
import software.amazon.awssdk.services.eventbridge.EventBridgeAsyncClient;
import software.amazon.awssdk.services.eventbridge.EventBridgeAsyncClientBuilder;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import software.amazon.awssdk.services.eventbridge.EventBridgeClientBuilder;

/**
 * Factory that creates an EventBridge client.
 */
@Factory
public class EventBridgeClientFactory extends AwsClientFactory<EventBridgeClientBuilder, EventBridgeAsyncClientBuilder, EventBridgeClient, EventBridgeAsyncClient> {

    /**
     * Constructor.
     *
     * @param credentialsProvider The credentials provider
     * @param regionProvider The region provider
     * @param userAgentProvider User-Agent Provider
     * @param awsServiceConfiguration  AWS Service Configuration
     */
    public EventBridgeClientFactory(AwsCredentialsProviderChain credentialsProvider,
                                    AwsRegionProviderChain regionProvider,
                                    @Nullable UserAgentProvider userAgentProvider,
                                    @Nullable @Named(EventBridgeClient.SERVICE_NAME) AWSServiceConfiguration awsServiceConfiguration) {
        super(credentialsProvider, regionProvider, userAgentProvider, awsServiceConfiguration);
    }

    @Override
    protected EventBridgeClientBuilder createSyncBuilder() {
        return EventBridgeClient.builder();
    }

    @Override
    protected EventBridgeAsyncClientBuilder createAsyncBuilder() {
        return EventBridgeAsyncClient.builder();
    }

    @Override
    @Singleton
    public EventBridgeClientBuilder syncBuilder(SdkHttpClient httpClient) {
        return super.syncBuilder(httpClient);
    }

    @Override
    @Bean(preDestroy = "close")
    @Singleton
    public EventBridgeClient syncClient(EventBridgeClientBuilder builder) {
        return super.syncClient(builder);
    }

    @Override
    @Singleton
    @Requires(beans = SdkAsyncHttpClient.class)
    public EventBridgeAsyncClientBuilder asyncBuilder(SdkAsyncHttpClient httpClient) {
        return super.asyncBuilder(httpClient);
    }

    @Override
    @Bean(preDestroy = "close")
    @Singleton
    @Requires(beans = SdkAsyncHttpClient.class)
    public EventBridgeAsyncClient asyncClient(EventBridgeAsyncClientBuilder builder) {
        return super.asyncClient(builder);
    }
}
