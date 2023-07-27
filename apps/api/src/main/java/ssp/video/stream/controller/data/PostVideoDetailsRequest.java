package ssp.video.stream.controller.data;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.serde.annotation.SerdeImport;
import io.micronaut.serde.annotation.Serdeable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ssp.video.stream.data.VideoDetails;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Serdeable.Deserializable
@Serdeable.Serializable
@SerdeImport(VideoDetails.class)
@Introspected(classes = {PostVideoDetailsRequest.class, VideoDetails.class})
public class PostVideoDetailsRequest {
    VideoDetails videoDetails;
}
