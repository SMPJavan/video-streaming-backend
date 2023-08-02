package ssp.video.stream.configuration;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;
import jakarta.validation.constraints.NotBlank;

@Requires(property = "video.move")
@ConfigurationProperties("video.move")
public interface VideoMoveConfiguration {

    @NotBlank
    String getSourceBucketName();

    @NotBlank
    String getDestinationBucketName();

    @NotBlank
    String getDynamoTableName();
}
