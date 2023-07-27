package ssp.video.stream.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Singleton;
import ssp.video.stream.events.payloads.EventPayload;

@Singleton
public class EventParser {

    ObjectMapper objectMapper = new ObjectMapper();

    public <U extends EventPayload> Event<U> parseEventString(String bodyString) throws JsonProcessingException {
        TypeReference<Event<U>> ref = new TypeReference<>() {};
        return objectMapper.readValue(bodyString, ref);
    }
}
