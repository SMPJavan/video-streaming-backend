package ssp.video.stream.eventbridge.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequest;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequestEntry;
import ssp.video.stream.events.payloads.EventPayload;

public class EventPublisher {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final EventBridgeClient eventBridgeClient;

    public EventPublisher(EventBridgeClient eventBridgeClient) {
        this.eventBridgeClient = eventBridgeClient;
    }

    public void publishEvent(String eventBusName, String source, EventPayload payload) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(payload);

        PutEventsRequestEntry entry = PutEventsRequestEntry.builder()
                .source(source)
                .detail(json)
                .detailType(payload.getEventType())
                .eventBusName(eventBusName)
                .build();

        PutEventsRequest eventsRequest = PutEventsRequest.builder()
                .entries(entry)
                .build();

        eventBridgeClient.putEvents(eventsRequest);
    }
}
