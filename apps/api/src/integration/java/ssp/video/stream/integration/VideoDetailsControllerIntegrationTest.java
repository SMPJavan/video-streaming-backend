package ssp.video.stream.integration;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import ssp.video.stream.controller.data.GetVideoDetailsResponse;
import ssp.video.stream.data.VideoDetails;
import ssp.video.stream.dynamodb.connection.DynamoDBConnection;
import ssp.video.stream.dynamodb.mapping.VideoDetailsMapper;
import ssp.video.stream.repository.S3Connection;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@MicronautTest
@Property(name = "dynamodb.video-details.table-name", value = VideoDetailsControllerIntegrationTest.VIDEO_DETAILS_TABLE_NAME)
@Property(name = "s3.videos.bucket", value = VideoDetailsControllerIntegrationTest.UPLOAD_BUCKET_NAME)
public class VideoDetailsControllerIntegrationTest {

    public static final String VIDEO_DETAILS_TABLE_NAME = "video_details";
    public static final String UPLOAD_BUCKET_NAME = "test-bucket";

    @Inject
    VideoDetailsMapper videoDetailsMapper;

    @Inject
    DynamoDBConnection dynamoDBConnection;

    @Inject
    S3Connection s3Connection;

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void should_get_video_details_for_id() {
        var videoId = "123";
        var videoDetailsData = VideoDetails.builder().id(videoId).build();
        doReturn(Optional.of(videoDetailsData)).when(dynamoDBConnection).getItem("videoId", videoId, VIDEO_DETAILS_TABLE_NAME, videoDetailsMapper);
        HttpRequest request = HttpRequest.GET("/videos/" + videoId);
        HttpResponse<GetVideoDetailsResponse> result = client.toBlocking().exchange(request, Argument.of(GetVideoDetailsResponse.class), Argument.STRING);
        assertEquals(HttpStatus.OK, result.getStatus());
        assertTrue(result.getBody(GetVideoDetailsResponse.class).isPresent());
        GetVideoDetailsResponse responseBody = result.getBody(GetVideoDetailsResponse.class).get();
        assertEquals(videoId, responseBody.getVideoDetails().getId());
    }

    @Test
    void should_respond_404_when_video_details_not_found() {
        var videoId = "123";
        HttpClientResponseException thrown = assertThrows(HttpClientResponseException.class, () -> {
            client.toBlocking().exchange("/videos/" + videoId);
        });

        assertEquals(HttpStatus.NOT_FOUND, thrown.getStatus());
    }


    @MockBean(DynamoDBConnection.class)
    public DynamoDBConnection dynamoDBConnection() {
        return mock(DynamoDBConnection.class);
    }


    @MockBean(S3Connection.class)
    public S3Connection s3Connection() {
        return mock(S3Connection.class);
    }
}
