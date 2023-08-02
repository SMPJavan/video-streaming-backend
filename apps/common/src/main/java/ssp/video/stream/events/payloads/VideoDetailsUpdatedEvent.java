package ssp.video.stream.events.payloads;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoDetailsUpdatedEvent implements EventPayload {
    String videoId;
}
