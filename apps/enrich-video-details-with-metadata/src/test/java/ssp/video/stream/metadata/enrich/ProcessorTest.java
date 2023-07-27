package ssp.video.stream.metadata.enrich;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import ssp.video.stream.configuration.MetadataEnrichConfiguration;
import ssp.video.stream.data.VideoDetails;
import ssp.video.stream.dynamodb.connection.DynamoDBConnection;
import ssp.video.stream.dynamodb.mapping.VideoDetailsMapper;
import ssp.video.stream.events.Event;
import ssp.video.stream.events.EventParser;
import ssp.video.stream.events.payloads.VideoUploadedEvent;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class ProcessorTest {

    @Test
    void should_process_metadata_enrich() throws JsonProcessingException {
        String bodyString = "dummy";
        String videoId = "123";
        String tableName = "video_details";
        VideoDetailsMapper videoDetailsMapper = mock(VideoDetailsMapper.class);
        MetadataEnricher metadataEnricher = mock(MetadataEnricher.class);
        DynamoDBConnection dynamoDBConnection = mock(DynamoDBConnection.class);
        MetadataEnrichConfiguration metadataEnrichConfiguration = mock(MetadataEnrichConfiguration.class);
        doReturn(tableName).when(metadataEnrichConfiguration).getTableName();
        VideoDetails videoDetails = VideoDetails.builder().id(videoId).build();
        doReturn(videoDetails).when(metadataEnricher).enrich(videoDetails);
        doReturn(Optional.of(videoDetails)).when(dynamoDBConnection).getItem("videoId", videoId, tableName, videoDetailsMapper);
        EventParser parser = mock(EventParser.class);
        SQSEvent.SQSMessage message = mock(SQSEvent.SQSMessage.class);
        doReturn(bodyString).when(message).getBody();
        Event<VideoUploadedEvent> event = Event.<VideoUploadedEvent>builder().detail(VideoUploadedEvent.builder().videoId(videoId).build()).build();
        doReturn(event).when(parser).parseEventString(bodyString);
        Processor processor = new Processor(videoDetailsMapper, metadataEnricher, parser, dynamoDBConnection, metadataEnrichConfiguration);
        processor.process(message);
        verify(dynamoDBConnection).saveItem(videoDetails, tableName, videoDetailsMapper);
    }
}
