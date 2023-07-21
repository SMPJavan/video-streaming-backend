package ssp.video.stream.repository;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import jakarta.inject.Singleton;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import ssp.video.stream.configuration.DynamoConfiguration;
import ssp.video.stream.controller.data.VideoDetails;

import java.util.Map;

@Requires(beans = {DynamoConfiguration.class, DynamoDBConnection.class})
@Singleton
public class VideoDetailsRepository {

    private final DynamoDBConnection dynamoDBConnection;
    private final DynamoConfiguration dynamoConfiguration;

    public VideoDetailsRepository(DynamoDBConnection dynamoDBConnection,
                                  DynamoConfiguration dynamoConfiguration) {
        this.dynamoDBConnection = dynamoDBConnection;
        this.dynamoConfiguration = dynamoConfiguration;

    }

    public VideoDetails getVideoDetails(String id) {
        return dynamoDBConnection.getItem("videoId", id, dynamoConfiguration.getVideoDetails().getTableName())
                .map(this::videoDetailsOf)
                .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND, String.format("Video not found for Id: %s.", id)));
    }

    private VideoDetails videoDetailsOf(Map<String, AttributeValue> item) {
        return VideoDetails.builder().id(item.get("videoId").s()).build();
    }
}
