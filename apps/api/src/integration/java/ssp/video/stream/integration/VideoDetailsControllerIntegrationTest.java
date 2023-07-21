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
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import ssp.video.stream.controller.data.VideoDetailsResponse;
import ssp.video.stream.repository.DynamoDBConnection;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@MicronautTest
@Property(name = "dynamodb.video-details.table-name", value = VideoDetailsControllerIntegrationTest.VIDEO_DETAILS_TABLE_NAME)
public class VideoDetailsControllerIntegrationTest {

    public static final String VIDEO_DETAILS_TABLE_NAME = "video_details";

    @Inject
    DynamoDBConnection dynamoDBConnection;

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void should_get_video_details_for_id() {
        var videoId = "123";
        var videoDetailsData = Map.of("videoId", AttributeValue.builder().s(videoId).build());
        doReturn(Optional.of(videoDetailsData)).when(dynamoDBConnection).getItem("videoId", videoId, VIDEO_DETAILS_TABLE_NAME);
        HttpRequest request = HttpRequest.GET("/videos/" + videoId);
        HttpResponse<VideoDetailsResponse> result = client.toBlocking().exchange(request, Argument.of(VideoDetailsResponse.class), Argument.STRING);
        assertEquals(HttpStatus.OK, result.getStatus());
        assertTrue(result.getBody(VideoDetailsResponse.class).isPresent());
        VideoDetailsResponse responseBody = result.getBody(VideoDetailsResponse.class).get();
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
}
