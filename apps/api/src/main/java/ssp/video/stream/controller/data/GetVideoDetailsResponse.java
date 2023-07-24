package ssp.video.stream.controller.data;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Introspected
@Serdeable.Deserializable
@Serdeable.Serializable
public class GetVideoDetailsResponse {
    VideoDetails videoDetails;
}
