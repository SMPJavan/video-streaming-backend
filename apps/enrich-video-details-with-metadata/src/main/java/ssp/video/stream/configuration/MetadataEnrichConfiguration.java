package ssp.video.stream.configuration;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;
import jakarta.validation.constraints.NotBlank;

@Requires(property = "metadata.enrich")
@ConfigurationProperties("metadata.enrich")
public interface MetadataEnrichConfiguration {

    @NotBlank
    String getTableName();

    @NotBlank
    String getS3BucketName();
}
