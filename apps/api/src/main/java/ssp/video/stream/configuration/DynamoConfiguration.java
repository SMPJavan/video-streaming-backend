package ssp.video.stream.configuration;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;
import jakarta.validation.constraints.NotBlank;

@Requires(property = "dynamodb.video-details")
@ConfigurationProperties("dynamodb")
public interface DynamoConfiguration {
    @NotBlank
    VideoDetails getVideoDetails();

    @Requires(property = "dynamodb.video-details.table-name")
    @ConfigurationProperties("video-details")
    interface VideoDetails {
        @NotBlank
        String getTableName();
    }
}
