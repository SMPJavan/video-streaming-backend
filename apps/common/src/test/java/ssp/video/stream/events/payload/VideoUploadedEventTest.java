package ssp.video.stream.events.payload;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import ssp.video.stream.events.payloads.VideoUploadedEvent;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VideoUploadedEventTest {

    @Test
    void should_haven_correct_detail_type() {
        VideoUploadedEvent event = VideoUploadedEvent.builder().build();
        assertEquals(VideoUploadedEvent.class.getSimpleName(), event.getEventType());
    }

    @Test
    void should_serialize() throws IOException {
        String videoId = "123";
        ObjectMapper objectMapper = new ObjectMapper();
        assertEquals("{\"videoId\":\"123\"}", objectMapper.writeValueAsString(VideoUploadedEvent.builder().videoId(videoId).build()));
    }

    @Test
    void should_deserialize() throws JsonProcessingException {
        String videoId = "123";
        ObjectMapper objectMapper = new ObjectMapper();
        VideoUploadedEvent event = objectMapper.readValue("{\"videoId\":\"" + videoId + "\"}", VideoUploadedEvent.class);
        assertEquals(videoId, event.getVideoId());
    }
}
