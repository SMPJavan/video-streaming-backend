package ssp.video.stream.s3object.transition;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import ssp.video.stream.configuration.VideoMoveConfiguration;
import ssp.video.stream.data.VideoDetails;
import ssp.video.stream.dynamodb.connection.DynamoDBConnection;
import ssp.video.stream.dynamodb.mapping.VideoDetailsMapper;
import ssp.video.stream.events.Event;
import ssp.video.stream.events.EventParser;
import ssp.video.stream.events.payloads.VideoDetailsUpdatedEvent;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class ProcessorTest {

    @Test
    void should_process_ready_to_move_video() throws JsonProcessingException {
        String videoId = "123";
        String tableName = "videos";
        String eventBodyString = "test";
        VideoDetails videoDetails = VideoDetails.builder().id(videoId).availableToStream(true).build();

        S3ObjectTransitioner s3ObjectTransitioner = mock(S3ObjectTransitioner.class);
        EventParser parser = mock(EventParser.class);
        DynamoDBConnection dynamoDBConnection = mock(DynamoDBConnection.class);
        VideoDetailsMapper mapper = mock(VideoDetailsMapper.class);
        VideoMoveConfiguration config = mock(VideoMoveConfiguration.class);
        SQSEvent.SQSMessage message = mock(SQSEvent.SQSMessage.class);
        Event<VideoDetailsUpdatedEvent> event = mock(Event.class);

        doReturn(VideoDetailsUpdatedEvent.builder().videoId(videoId).build()).when(event).getDetail();
        doReturn(tableName).when(config).getDynamoTableName();
        doReturn(eventBodyString).when(message).getBody();
        doReturn(event).when(parser).parseEventString(eventBodyString);
        doReturn(Optional.of(videoDetails)).when(dynamoDBConnection).getItem("videoId", videoId, tableName, mapper);

        Processor processor = new Processor(s3ObjectTransitioner, parser, dynamoDBConnection, mapper, config);
        processor.process(message);
        verify(s3ObjectTransitioner).moveVideo(videoId);
    }

    @Test
    void should_not_process_unready_to_move_video() throws JsonProcessingException {
        String videoId = "123";
        String tableName = "videos";
        String eventBodyString = "test";
        VideoDetails videoDetails = VideoDetails.builder().id(videoId).build();

        S3ObjectTransitioner s3ObjectTransitioner = mock(S3ObjectTransitioner.class);
        EventParser parser = mock(EventParser.class);
        DynamoDBConnection dynamoDBConnection = mock(DynamoDBConnection.class);
        VideoDetailsMapper mapper = mock(VideoDetailsMapper.class);
        VideoMoveConfiguration config = mock(VideoMoveConfiguration.class);
        SQSEvent.SQSMessage message = mock(SQSEvent.SQSMessage.class);
        Event<VideoDetailsUpdatedEvent> event = mock(Event.class);

        doReturn(VideoDetailsUpdatedEvent.builder().videoId(videoId).build()).when(event).getDetail();
        doReturn(tableName).when(config).getDynamoTableName();
        doReturn(eventBodyString).when(message).getBody();
        doReturn(event).when(parser).parseEventString(eventBodyString);
        doReturn(Optional.of(videoDetails)).when(dynamoDBConnection).getItem("videoId", videoId, tableName, mapper);

        Processor processor = new Processor(s3ObjectTransitioner, parser, dynamoDBConnection, mapper, config);
        processor.process(message);
        verify(s3ObjectTransitioner, never()).moveVideo(videoId);
    }
}
