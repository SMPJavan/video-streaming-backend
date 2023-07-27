package ssp.video.stream.repository;

import io.micronaut.http.exceptions.HttpStatusException;
import org.junit.jupiter.api.Test;
import ssp.video.stream.configuration.DynamoConfiguration;
import ssp.video.stream.data.VideoDetails;
import ssp.video.stream.dynamodb.connection.DynamoDBConnection;
import ssp.video.stream.dynamodb.mapping.VideoDetailsMapper;

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
        VideoDetails expectedResponse = VideoDetails.builder().build();
        DynamoDBConnection dynamoDBConnection = mock(DynamoDBConnection.class);
        DynamoConfiguration dynamoConfiguration = mock(DynamoConfiguration.class);
        VideoDetailsMapper dynamoDBMapper = mock(VideoDetailsMapper.class);
        DynamoConfiguration.VideoDetails videoDetailsConfig = mock(DynamoConfiguration.VideoDetails.class);
        doReturn(Optional.of(expectedResponse)).when(dynamoDBConnection).getItem("videoId", videoId, tableName, dynamoDBMapper);
        VideoDetailsRepository repository = new VideoDetailsRepository(dynamoDBConnection, dynamoConfiguration, dynamoDBMapper);
        doReturn(videoDetailsConfig).when(dynamoConfiguration).getVideoDetails();
        doReturn(tableName).when(videoDetailsConfig).getTableName();
        VideoDetails response = repository.getVideoDetails(videoId);
        assertEquals(expectedResponse, response);
    }

    @Test
    void should_throw_http_exception_when_video_not_found() {
        String videoId = "123";
        String tableName = "video_details";
        DynamoDBConnection dynamoDBConnection = mock(DynamoDBConnection.class);
        DynamoConfiguration dynamoConfiguration = mock(DynamoConfiguration.class);
        VideoDetailsMapper dynamoDBMapper = mock(VideoDetailsMapper.class);
        DynamoConfiguration.VideoDetails videoDetailsConfig = mock(DynamoConfiguration.VideoDetails.class);
        VideoDetailsRepository repository = new VideoDetailsRepository(dynamoDBConnection, dynamoConfiguration, dynamoDBMapper);
        doReturn(videoDetailsConfig).when(dynamoConfiguration).getVideoDetails();
        doReturn(tableName).when(videoDetailsConfig).getTableName();
        doReturn(Optional.empty()).when(dynamoDBConnection).getItem("videoId", videoId, tableName);
        assertThrows(HttpStatusException.class, () -> repository.getVideoDetails(videoId));
    }
}
