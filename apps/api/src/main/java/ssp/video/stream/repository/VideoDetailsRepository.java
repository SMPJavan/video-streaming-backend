package ssp.video.stream.repository;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import jakarta.inject.Singleton;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import ssp.video.stream.configuration.DynamoConfiguration;
import ssp.video.stream.controller.data.VideoDetails;

import java.util.Map;
import java.util.UUID;

@Requires(beans = {DynamoConfiguration.class, DynamoDBConnection.class})
@Singleton
public class VideoDetailsRepository {

    private final DynamoDBConnection dynamoDBConnection;
    private final DynamoConfiguration dynamoConfiguration;
    private final DynamoDBMapper dynamoDBMapper;

    private final String ID_FIELD_NAME = "videoId";
    private final String TITLE_FIELD_NAME = "title";
    private final String DESCRIPTION_FIELD_NAME = "description";

    public VideoDetailsRepository(DynamoDBConnection dynamoDBConnection,
                                  DynamoConfiguration dynamoConfiguration,
                                  DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBConnection = dynamoDBConnection;
        this.dynamoConfiguration = dynamoConfiguration;
        this.dynamoDBMapper = dynamoDBMapper;

    }

    public VideoDetails saveVideoDetails(VideoDetails videoDetails) {
        return videoDetailsOf(dynamoDBConnection.saveItem(Map.of(
                        ID_FIELD_NAME, AttributeValue.builder().s(UUID.randomUUID().toString()).build(),
                        TITLE_FIELD_NAME, AttributeValue.builder().s(videoDetails.getTitle()).build(),
                        DESCRIPTION_FIELD_NAME, AttributeValue.builder().s(videoDetails.getDescription()).build()),
                dynamoConfiguration.getVideoDetails().getTableName()));
    }

    public VideoDetails getVideoDetails(String id) {
        return dynamoDBConnection.getItem("videoId", id, dynamoConfiguration.getVideoDetails().getTableName())
                .map(this::videoDetailsOf)
                .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND, String.format("Video not found for Id: %s.", id)));
    }

    private VideoDetails videoDetailsOf(Map<String, AttributeValue> item) {
        return VideoDetails.builder()
                .id(dynamoDBMapper.getString(item, ID_FIELD_NAME))
                .title(dynamoDBMapper.getString(item, TITLE_FIELD_NAME))
                .description(dynamoDBMapper.getString(item, DESCRIPTION_FIELD_NAME))
                .build();
    }
}
