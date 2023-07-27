package ssp.video.stream;

import com.amazonaws.services.s3.event.S3EventNotification;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ssp.video.stream.configuration.EventBridgeConfiguration;
import ssp.video.stream.eventbridge.common.EventPublisher;
import ssp.video.stream.events.payloads.VideoUploadedEvent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class VideoUploadedNotificationProcessorTest {

    @Test
    void should_process_record() throws JsonProcessingException {
        String key = "testKey";
        String bucket = "testBucket";
        String source = "testSource";
        String eventBus = "testBus";
        EventBridgeConfiguration eventBridgeConfiguration = mock(EventBridgeConfiguration.class);
        doReturn(source).when(eventBridgeConfiguration).getSource();
        doReturn(eventBus).when(eventBridgeConfiguration).getBusName();
        EventPublisher eventPublisher = mock(EventPublisher.class);
        S3EventNotification.S3EventNotificationRecord record = mock(S3EventNotification.S3EventNotificationRecord.class);
        doReturn("ObjectCreated").when(record).getEventName();
        S3EventNotification.S3Entity s3Entity = mock(S3EventNotification.S3Entity.class);
        S3EventNotification.S3BucketEntity s3BucketEntity = mock(S3EventNotification.S3BucketEntity.class);
        doReturn(bucket).when(s3BucketEntity).getName();
        S3EventNotification.S3ObjectEntity s3ObjectEntity = mock(S3EventNotification.S3ObjectEntity.class);
        doReturn(key).when(s3ObjectEntity).getKey();
        doReturn(s3Entity).when(record).getS3();
        doReturn(s3BucketEntity).when(s3Entity).getBucket();
        doReturn(s3ObjectEntity).when(s3Entity).getObject();

        VideoUploadedNotificationProcessor processor = new VideoUploadedNotificationProcessor(eventBridgeConfiguration, eventPublisher);

        processor.process(record);
        ArgumentCaptor<VideoUploadedEvent> argument = ArgumentCaptor.forClass(VideoUploadedEvent.class);
        verify(eventPublisher).publishEvent(eq(eventBus), eq(source), argument.capture());
        assertEquals(key, argument.getValue().getVideoId());
    }

    @Test
    void should_not_process_non_created_notification_record() throws JsonProcessingException {
        EventBridgeConfiguration eventBridgeConfiguration = mock(EventBridgeConfiguration.class);
        EventPublisher eventPublisher = mock(EventPublisher.class);
        S3EventNotification.S3EventNotificationRecord record = mock(S3EventNotification.S3EventNotificationRecord.class);
        doReturn("ObjectNotCreated").when(record).getEventName();

        VideoUploadedNotificationProcessor processor = new VideoUploadedNotificationProcessor(eventBridgeConfiguration, eventPublisher);

        processor.process(record);

        verify(eventPublisher, never()).publishEvent(anyString(), anyString(), any());
    }

    @Test
    void should_throw_json_exception() throws JsonProcessingException {
        String key = "testKey";
        String bucket = "testBucket";
        String source = "testSource";
        String eventBus = "testBus";
        EventBridgeConfiguration eventBridgeConfiguration = mock(EventBridgeConfiguration.class);
        doReturn(source).when(eventBridgeConfiguration).getSource();
        doReturn(eventBus).when(eventBridgeConfiguration).getBusName();
        EventPublisher eventPublisher = mock(EventPublisher.class);
        S3EventNotification.S3EventNotificationRecord record = mock(S3EventNotification.S3EventNotificationRecord.class);
        doReturn("ObjectCreated").when(record).getEventName();
        S3EventNotification.S3Entity s3Entity = mock(S3EventNotification.S3Entity.class);
        S3EventNotification.S3BucketEntity s3BucketEntity = mock(S3EventNotification.S3BucketEntity.class);
        doReturn(bucket).when(s3BucketEntity).getName();
        S3EventNotification.S3ObjectEntity s3ObjectEntity = mock(S3EventNotification.S3ObjectEntity.class);
        doReturn(key).when(s3ObjectEntity).getKey();
        doReturn(s3Entity).when(record).getS3();
        doReturn(s3BucketEntity).when(s3Entity).getBucket();
        doReturn(s3ObjectEntity).when(s3Entity).getObject();

        VideoUploadedNotificationProcessor processor = new VideoUploadedNotificationProcessor(eventBridgeConfiguration, eventPublisher);

        doThrow(JsonProcessingException.class). when(eventPublisher).publishEvent(anyString(), anyString(), any());
        assertThrows(JsonProcessingException.class, () -> processor.process(record));
    }
}
