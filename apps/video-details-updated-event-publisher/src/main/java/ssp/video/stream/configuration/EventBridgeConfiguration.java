package ssp.video.stream.configuration;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Requires;
import jakarta.validation.constraints.NotBlank;

@Requires(property = "eventbridge")
@ConfigurationProperties("eventbridge")
public interface EventBridgeConfiguration {
    @NotBlank
    String getSource();

    @NotBlank
    String getBusName();
}
