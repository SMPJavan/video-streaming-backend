package ssp.video.stream.s3object.transition;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ssp.video.stream.configuration.VideoMoveConfiguration;
import ssp.video.stream.data.VideoDetails;
import ssp.video.stream.dynamodb.connection.DynamoDBConnection;
import ssp.video.stream.dynamodb.mapping.VideoDetailsMapper;
import ssp.video.stream.events.Event;
import ssp.video.stream.events.EventParser;
import ssp.video.stream.events.payloads.VideoDetailsUpdatedEvent;

import java.util.Optional;

@Requires(beans = {S3ObjectTransitioner.class, VideoMoveConfiguration.class, DynamoDBConnection.class, VideoDetailsMapper.class, VideoMoveConfiguration.class})
@Singleton
public class Processor {

    private static final Logger LOG = LoggerFactory.getLogger(Processor.class);

    private final S3ObjectTransitioner s3ObjectTransitioner;

    private final EventParser parser;

    private final DynamoDBConnection dynamoDBConnection;

    private final VideoDetailsMapper videoDetailsMapper;

    private final VideoMoveConfiguration videoMoveConfiguration;

    public Processor(S3ObjectTransitioner s3ObjectTransitioner, EventParser parser, DynamoDBConnection dynamoDBConnection, VideoDetailsMapper videoDetailsMapper, VideoMoveConfiguration videoMoveConfiguration) {
        this.s3ObjectTransitioner = s3ObjectTransitioner;
        this.parser = parser;
        this.dynamoDBConnection = dynamoDBConnection;
        this.videoDetailsMapper = videoDetailsMapper;
        this.videoMoveConfiguration = videoMoveConfiguration;
    }

    public void process(SQSEvent.SQSMessage message) throws JsonProcessingException {
        Event<VideoDetailsUpdatedEvent> event = parser.parseEventString(message.getBody());
        Optional<VideoDetails> videoDetails = getVideoDetails(event.getDetail().getVideoId());
        if (videoDetails.isPresent() && Optional.ofNullable(videoDetails.get().getAvailableToStream()).orElse(false)) {
            LOG.info(String.format("Moving video file to video store for video with Id: %s...", event.getDetail().getVideoId()));
            s3ObjectTransitioner.moveVideo(event.getDetail().getVideoId());
            LOG.info(String.format("Successfully moved video file to video store for video with Id: %s.", event.getDetail().getVideoId()));
        }
        else {
            LOG.info(String.format("Video is not yet ready to move to video store for video with Id: %s.", event.getDetail().getVideoId()));
        }
    }

    private Optional<VideoDetails> getVideoDetails(String videoId) {
        return dynamoDBConnection.getItem("videoId", videoId, videoMoveConfiguration.getDynamoTableName(), videoDetailsMapper);
    }
}
