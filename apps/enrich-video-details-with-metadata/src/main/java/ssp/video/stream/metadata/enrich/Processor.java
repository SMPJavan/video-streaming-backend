package ssp.video.stream.metadata.enrich;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ssp.video.stream.configuration.MetadataEnrichConfiguration;
import ssp.video.stream.data.VideoDetails;
import ssp.video.stream.dynamodb.connection.DynamoDBConnection;
import ssp.video.stream.dynamodb.mapping.VideoDetailsMapper;
import ssp.video.stream.events.Event;
import ssp.video.stream.events.EventParser;
import ssp.video.stream.events.payloads.VideoUploadedEvent;

import java.util.Optional;

@Requires(beans = {MetadataEnricher.class, VideoDetailsMapper.class, EventParser.class, DynamoDBConnection.class, MetadataEnrichConfiguration.class})
@Singleton
public class Processor {

    private static final Logger LOG = LoggerFactory.getLogger(Processor.class);
    private final VideoDetailsMapper videoDetailsMapper;

    private final MetadataEnricher metadataEnricher;

    private final EventParser parser;

    private final DynamoDBConnection dynamoDBConnection;

    private final MetadataEnrichConfiguration metadataEnrichConfiguration;

    public Processor(VideoDetailsMapper videoDetailsMapper, MetadataEnricher metadataEnricher, EventParser parser, DynamoDBConnection dynamoDBConnection, MetadataEnrichConfiguration metadataEnrichConfiguration) {
        this.videoDetailsMapper = videoDetailsMapper;
        this.metadataEnricher = metadataEnricher;
        this.parser = parser;
        this.dynamoDBConnection = dynamoDBConnection;
        this.metadataEnrichConfiguration = metadataEnrichConfiguration;
    }

    public void process(SQSEvent.SQSMessage message) throws JsonProcessingException {
        Event<VideoUploadedEvent> event = parser.parseEventString(message.getBody());
        LOG.info(String.format("Enriching video details for video with Id %s...", event.getDetail().getVideoId()));
        Optional<VideoDetails> videoDetails = dynamoDBConnection.getItem("videoId", event.getDetail().getVideoId(),metadataEnrichConfiguration.getTableName(), videoDetailsMapper);
        videoDetails.ifPresentOrElse(this::updateVideoDetails, () -> {throw new RuntimeException("Video details not found.");});
        LOG.info(String.format("Successfully enriched video details for video with Id %s.", event.getDetail().getVideoId()));
    }

    private void updateVideoDetails(VideoDetails videoDetails) {
        videoDetails = metadataEnricher.enrich(videoDetails);
        dynamoDBConnection.saveItem(videoDetails, metadataEnrichConfiguration.getTableName(), videoDetailsMapper);
    }
}
