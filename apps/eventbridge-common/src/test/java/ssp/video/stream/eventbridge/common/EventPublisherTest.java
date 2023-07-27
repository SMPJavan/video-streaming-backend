package ssp.video.stream.eventbridge.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import software.amazon.awssdk.services.eventbridge.model.PutEventsRequest;
import ssp.video.stream.eventbridge.common.EventPublisher;
import ssp.video.stream.events.payloads.VideoUploadedEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class EventPublisherTest {

    @Test
    public void should_publish_event() throws JsonProcessingException {
        String eventBusName = "bus";
        String source = "source";
        VideoUploadedEvent payload = VideoUploadedEvent.builder().build();
        EventBridgeClient eventBridgeClient = mock(EventBridgeClient.class);
        EventPublisher eventPublisher = new EventPublisher(eventBridgeClient);
        eventPublisher.publishEvent(eventBusName, source, payload);
        ArgumentCaptor<PutEventsRequest> argument = ArgumentCaptor.forClass(PutEventsRequest.class);
        verify(eventBridgeClient).putEvents(argument.capture());
        assertEquals(1, argument.getValue().entries().size());
        assertEquals(eventBusName, argument.getValue().entries().get(0).eventBusName());
        assertEquals(source, argument.getValue().entries().get(0).source());
        assertEquals(new ObjectMapper().writeValueAsString(payload), argument.getValue().entries().get(0).detail());
    }
}
