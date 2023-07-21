package ssp.video.stream.repository;

import io.micronaut.http.exceptions.HttpStatusException;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import ssp.video.stream.configuration.DynamoConfiguration;
import ssp.video.stream.controller.data.VideoDetails;
import ssp.video.stream.repository.DynamoDBConnection;
import ssp.video.stream.repository.VideoDetailsRepository;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class VideoDetailsRepositoryTest {

    @Test
    void should_get_video_details() {
        String videoId = "123";
        String tableName = "video_details";
        DynamoDBConnection dynamoDBConnection = mock(DynamoDBConnection.class);
        DynamoConfiguration dynamoConfiguration = mock(DynamoConfiguration.class);
        DynamoConfiguration.VideoDetails videoDetailsConfig = mock(DynamoConfiguration.VideoDetails.class);
        VideoDetailsRepository repository = new VideoDetailsRepository(dynamoDBConnection, dynamoConfiguration);
        doReturn(videoDetailsConfig).when(dynamoConfiguration).getVideoDetails();
        doReturn(tableName).when(videoDetailsConfig).getTableName();
        var videoDetailsData = Map.of("videoId", AttributeValue.builder().s(videoId).build());
        doReturn(Optional.of(videoDetailsData)).when(dynamoDBConnection).getItem("videoId", videoId, tableName);
        VideoDetails videoDetails = repository.getVideoDetails(videoId);
        assertEquals(videoId, videoDetails.getId());
    }

    @Test
    void should_throw_http_exception_when_video_not_found() {
        String videoId = "123";
        String tableName = "video_details";
        DynamoDBConnection dynamoDBConnection = mock(DynamoDBConnection.class);
        DynamoConfiguration dynamoConfiguration = mock(DynamoConfiguration.class);
        DynamoConfiguration.VideoDetails videoDetailsConfig = mock(DynamoConfiguration.VideoDetails.class);
        VideoDetailsRepository repository = new VideoDetailsRepository(dynamoDBConnection, dynamoConfiguration);
        doReturn(videoDetailsConfig).when(dynamoConfiguration).getVideoDetails();
        doReturn(tableName).when(videoDetailsConfig).getTableName();
        doReturn(Optional.empty()).when(dynamoDBConnection).getItem("videoId", videoId, tableName);
        assertThrows(HttpStatusException.class, () -> repository.getVideoDetails(videoId));
    }
}
