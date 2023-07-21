package ssp.video.stream.configuration;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;
import jakarta.validation.constraints.NotBlank;

@Requires(property = "s3.videos")
@ConfigurationProperties("s3")
public interface S3Configuration {
    @NotBlank
    VideosConfiguration getVideosConfiguration();

    @Requires(property = "s3.videos.bucket")
    @ConfigurationProperties("videos")
    interface VideosConfiguration {
        @NotBlank
        String getBucket();
    }
}
