package ssp.video.stream;

import com.amazonaws.services.s3.event.S3EventNotification;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import ssp.video.stream.configuration.EventBridgeConfiguration;
import ssp.video.stream.eventbridge.common.EventPublisher;
import ssp.video.stream.events.payloads.VideoUploadedEvent;

@Singleton
public class VideoUploadedNotificationProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(VideoUploadedNotificationProcessor.class);
    public static final String OBJECT_CREATED = "ObjectCreated";
    private final EventBridgeConfiguration eventBridgeConfiguration;

    private final EventPublisher eventPublisher;

    public VideoUploadedNotificationProcessor(EventBridgeConfiguration eventBridgeConfiguration, EventBridgeClient eventBridgeClient) {
        this(eventBridgeConfiguration, new EventPublisher(eventBridgeClient));
    }

    public VideoUploadedNotificationProcessor(EventBridgeConfiguration eventBridgeConfiguration, EventPublisher eventPublisher) {
        this.eventBridgeConfiguration = eventBridgeConfiguration;
        this.eventPublisher = eventPublisher;
    }

    public void process(S3EventNotification.S3EventNotificationRecord record) throws JsonProcessingException {
        if (record.getEventName().contains(OBJECT_CREATED)) {
            S3EventNotification.S3Entity s3Entity = record.getS3();
            String bucket = s3Entity.getBucket().getName();
            String key = s3Entity.getObject().getKey();
            LOG.info(String.format("File uploaded | Bucket: %s, Key: %s", bucket, key));
            eventPublisher.publishEvent(eventBridgeConfiguration.getBusName(), eventBridgeConfiguration.getSource(), VideoUploadedEvent.builder().videoId(key).build());
        }
    }
}
