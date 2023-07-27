package ssp.video.stream.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import ssp.video.stream.events.payloads.VideoUploadedEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventParserTest {

    @Test
    void should_parse_event_string() throws JsonProcessingException {
        String videoId = "123";
        String eventString = "{\"version\":\"0\",\"id\":\"d443ff52-2fde-6b91-831d-d372288db48e\",\"detail-type\":\"VideoUploadedEvent\",\"source\":\"video-upload-event-publisher\",\"account\":\"568060381530\",\"time\":\"2023-07-27T09:36:25Z\",\"region\":\"eu-west-2\",\"resources\":[],\"detail\":{\"videoId\":\"" + videoId + "\"}}";
        EventParser parser = new EventParser();
        Event<VideoUploadedEvent> event = parser.parseEventString(eventString);
        assertEquals(videoId, event.getDetail().getVideoId());
    }
}
