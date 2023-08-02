package ssp.video.stream;

import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue;
import com.amazonaws.services.lambda.runtime.events.models.dynamodb.StreamRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ssp.video.stream.configuration.EventBridgeConfiguration;
import ssp.video.stream.data.VideoDetails;
import ssp.video.stream.dynamodb.mapping.VideoDetailsMapper;
import ssp.video.stream.dynamodb.mapping.adapter.StreamAttributeValueAdapter;
import ssp.video.stream.eventbridge.common.EventPublisher;
import ssp.video.stream.events.payloads.VideoDetailsUpdatedEvent;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class VideoDetailsUpdatedProcessorTest {

    @Test
    void should_process_record_update() throws JsonProcessingException {
        String videoId = "123";
        String eventBridgeBus = "test-bus";
        String source = "unitTest";
        VideoDetails videoDetails = VideoDetails.builder().id(videoId).build();

        EventBridgeConfiguration eventBridgeConfiguration = mock(EventBridgeConfiguration.class);
        EventPublisher eventPublisher = mock(EventPublisher.class);
        VideoDetailsMapper videoDetailsMapper = mock(VideoDetailsMapper.class);
        StreamAttributeValueAdapter streamAttributeValueAdapter = mock(StreamAttributeValueAdapter.class);
        DynamodbEvent.DynamodbStreamRecord record = mock(DynamodbEvent.DynamodbStreamRecord.class);

        doReturn("MODIFY").when(record).getEventName();
        StreamRecord dynamoDb = mock(StreamRecord.class);
        doReturn(dynamoDb).when(record).getDynamodb();
        Map<String, AttributeValue> oldImage = Collections.emptyMap();
        doReturn(oldImage).when(dynamoDb).getOldImage();
        doReturn(videoDetails).when(videoDetailsMapper).map(oldImage, streamAttributeValueAdapter);
        doReturn(eventBridgeBus).when(eventBridgeConfiguration).getBusName();
        doReturn(source).when(eventBridgeConfiguration).getSource();

        VideoDetailsUpdatedProcessor processor = new VideoDetailsUpdatedProcessor(eventBridgeConfiguration, eventPublisher, videoDetailsMapper, streamAttributeValueAdapter);
        processor.process(record);
        ArgumentCaptor<VideoDetailsUpdatedEvent> eventArgumentCaptor = ArgumentCaptor.forClass(VideoDetailsUpdatedEvent.class);
        verify(eventPublisher).publishEvent(eq(eventBridgeBus), eq(source), eventArgumentCaptor.capture());
        assertEquals(videoId, eventArgumentCaptor.getValue().getVideoId());
    }

    @Test
    void should_not_process_non_modified_notification_record() throws JsonProcessingException {
        String eventBridgeBus = "test-bus";
        String source = "unitTest";

        EventBridgeConfiguration eventBridgeConfiguration = mock(EventBridgeConfiguration.class);
        EventPublisher eventPublisher = mock(EventPublisher.class);
        VideoDetailsMapper videoDetailsMapper = mock(VideoDetailsMapper.class);
        StreamAttributeValueAdapter streamAttributeValueAdapter = mock(StreamAttributeValueAdapter.class);
        DynamodbEvent.DynamodbStreamRecord record = mock(DynamodbEvent.DynamodbStreamRecord.class);

        doReturn("CREATE").when(record).getEventName();
        doReturn(eventBridgeBus).when(eventBridgeConfiguration).getBusName();
        doReturn(source).when(eventBridgeConfiguration).getSource();

        VideoDetailsUpdatedProcessor processor = new VideoDetailsUpdatedProcessor(eventBridgeConfiguration, eventPublisher, videoDetailsMapper, streamAttributeValueAdapter);
        processor.process(record);
        verify(eventPublisher, never()).publishEvent(eq(eventBridgeBus), eq(source), any(VideoDetailsUpdatedEvent.class));
    }

    @Test
    void should_throw_json_exception() throws JsonProcessingException {
        String videoId = "123";
        String eventBridgeBus = "test-bus";
        String source = "unitTest";
        VideoDetails videoDetails = VideoDetails.builder().id(videoId).build();

        EventBridgeConfiguration eventBridgeConfiguration = mock(EventBridgeConfiguration.class);
        EventPublisher eventPublisher = mock(EventPublisher.class);
        VideoDetailsMapper videoDetailsMapper = mock(VideoDetailsMapper.class);
        StreamAttributeValueAdapter streamAttributeValueAdapter = mock(StreamAttributeValueAdapter.class);
        DynamodbEvent.DynamodbStreamRecord record = mock(DynamodbEvent.DynamodbStreamRecord.class);

        doReturn("MODIFY").when(record).getEventName();
        StreamRecord dynamoDb = mock(StreamRecord.class);
        doReturn(dynamoDb).when(record).getDynamodb();
        Map<String, AttributeValue> oldImage = Collections.emptyMap();
        doReturn(oldImage).when(dynamoDb).getOldImage();
        doReturn(videoDetails).when(videoDetailsMapper).map(oldImage, streamAttributeValueAdapter);
        doReturn(eventBridgeBus).when(eventBridgeConfiguration).getBusName();
        doReturn(source).when(eventBridgeConfiguration).getSource();
        doThrow(JsonProcessingException.class).when(eventPublisher).publishEvent(eq(eventBridgeBus), eq(source), any(VideoDetailsUpdatedEvent.class));

        VideoDetailsUpdatedProcessor processor = new VideoDetailsUpdatedProcessor(eventBridgeConfiguration, eventPublisher, videoDetailsMapper, streamAttributeValueAdapter);
        assertThrows(JsonProcessingException.class, () -> processor.process(record));
    }
}
