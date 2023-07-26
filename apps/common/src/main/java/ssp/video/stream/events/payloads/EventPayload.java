package ssp.video.stream.events.payloads;

import com.fasterxml.jackson.annotation.JsonIgnore;

public interface EventPayload {
    @JsonIgnore
    default String getEventType() {
        return this.getClass().getSimpleName();
    }
}
