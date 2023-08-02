package ssp.video.stream.dynamodb.mapping;

import org.junit.jupiter.api.Test;
import ssp.video.stream.data.VideoDetails;
import ssp.video.stream.dynamodb.mapping.adapter.AttributeValueAdapter;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class VideoDetailsMapperTest {

    @Test
    void should_map_to_video_details() {
        String videoId = "testId";
        String title = "test title";
        String description = "test description";
        int duration = 123;
        Boolean availableToStream = true;
        VideoDetailsMapper mapper = new VideoDetailsMapper();
        Map<String, String> itemMap = mock(Map.class);
        AttributeValueAdapter<String> adapter = mock(AttributeValueAdapter.class);
        doReturn(videoId).when(adapter).getString(itemMap, "videoId");
        doReturn(title).when(adapter).getString(itemMap, "title");
        doReturn(description).when(adapter).getString(itemMap, "description");
        doReturn(duration).when(adapter).getInteger(itemMap, "duration");
        doReturn(availableToStream).when(adapter).getBoolean(itemMap, "availableToStream");
        VideoDetails videoDetails = mapper.map(itemMap, adapter);
        assertEquals(videoId, videoDetails.getId());
        assertEquals(title, videoDetails.getTitle());
        assertEquals(description, videoDetails.getDescription());
        assertEquals(duration, videoDetails.getDuration());
        assertEquals(availableToStream, videoDetails.getAvailableToStream());
    }

    @Test
    void should_map_from_video_details() {
        String videoId = "testId";
        String title = "test title";
        String description = "test description";
        int duration = 123;
        Boolean availableToStream = true;
        VideoDetails videoDetails = VideoDetails.builder().id(videoId).title(title).description(description).duration(duration).availableToStream(availableToStream).build();
        VideoDetailsMapper mapper = new VideoDetailsMapper();
        AttributeValueAdapter<String> adapter = mock(AttributeValueAdapter.class);
        doReturn(Optional.of(videoId)).when(adapter).mapAttribute(videoId);
        doReturn(Optional.of(title)).when(adapter).mapAttribute(title);
        doReturn(Optional.of(description)).when(adapter).mapAttribute(description);
        doReturn(Optional.of(String.valueOf(duration))).when(adapter).mapAttribute(duration);
        doReturn(Optional.of(availableToStream)).when(adapter).mapAttribute(availableToStream);
        Map<String, String> itemMap = mapper.toModelAttributeMap(videoDetails, adapter);
        assertEquals(videoId, itemMap.get("videoId"));
        assertEquals(title, itemMap.get("title"));
        assertEquals(description, itemMap.get("description"));
        assertEquals(String.valueOf(duration), itemMap.get("duration"));
        assertEquals(availableToStream, itemMap.get("availableToStream"));
    }
}
