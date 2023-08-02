package ssp.video.stream;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import ssp.video.stream.configuration.EventBridgeConfiguration;
import ssp.video.stream.data.VideoDetails;
import ssp.video.stream.dynamodb.mapping.VideoDetailsMapper;
import ssp.video.stream.dynamodb.mapping.adapter.StreamAttributeValueAdapter;
import ssp.video.stream.eventbridge.common.EventPublisher;
import ssp.video.stream.events.payloads.VideoDetailsUpdatedEvent;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent.DynamodbStreamRecord;

@Singleton
public class VideoDetailsUpdatedProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(VideoDetailsUpdatedProcessor.class);
    public static final String OBJECT_MODIFIED = "MODIFY";
    private final EventBridgeConfiguration eventBridgeConfiguration;

    private final EventPublisher eventPublisher;

    private final VideoDetailsMapper videoDetailsMapper;

    private final StreamAttributeValueAdapter streamAttributeValueAdapter;

    public VideoDetailsUpdatedProcessor(EventBridgeConfiguration eventBridgeConfiguration, EventBridgeClient eventBridgeClient, VideoDetailsMapper videoDetailsMapper, StreamAttributeValueAdapter streamAttributeValueAdapter) {
        this(eventBridgeConfiguration, new EventPublisher(eventBridgeClient), videoDetailsMapper, streamAttributeValueAdapter);
    }

    public VideoDetailsUpdatedProcessor(EventBridgeConfiguration eventBridgeConfiguration, EventPublisher eventPublisher, VideoDetailsMapper videoDetailsMapper, StreamAttributeValueAdapter streamAttributeValueAdapter) {
        this.eventBridgeConfiguration = eventBridgeConfiguration;
        this.eventPublisher = eventPublisher;
        this.videoDetailsMapper = videoDetailsMapper;
        this.streamAttributeValueAdapter = streamAttributeValueAdapter;
    }

    public void process(DynamodbStreamRecord record) throws JsonProcessingException {
        if (record.getEventName().contains(OBJECT_MODIFIED)) {
            VideoDetails videoDetails = videoDetailsMapper.map(record.getDynamodb().getOldImage(), streamAttributeValueAdapter);
            LOG.info(String.format("Record updated | videoId: %s", videoDetails.getId()));
            eventPublisher.publishEvent(eventBridgeConfiguration.getBusName(), eventBridgeConfiguration.getSource(), VideoDetailsUpdatedEvent.builder().videoId(videoDetails.getId()).build());
        }
    }
}
