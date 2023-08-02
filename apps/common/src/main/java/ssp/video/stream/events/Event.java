package ssp.video.stream.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ssp.video.stream.events.payloads.EventPayload;
import ssp.video.stream.events.payloads.VideoDetailsUpdatedEvent;
import ssp.video.stream.events.payloads.VideoUploadedEvent;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Event<T extends EventPayload> {
    String version;
    String id;
    @JsonProperty("detail-type")
    String detailType;
    String source;
    String account;
    String time;
    String region;
    List<String> resources;
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXTERNAL_PROPERTY, property = "detail-type")
    @JsonSubTypes({
            @JsonSubTypes.Type( value = VideoUploadedEvent.class, name = "VideoUploadedEvent" ),
            @JsonSubTypes.Type( value = VideoDetailsUpdatedEvent.class, name = "VideoDetailsUpdatedEvent" )
    })
    T detail;
}
