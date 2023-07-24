package ssp.video.stream.repository;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import ssp.video.stream.configuration.DynamoConfiguration;
import ssp.video.stream.controller.data.VideoDetails;

import java.util.Map;
import java.util.UUID;

@Requires(beans = {DynamoConfiguration.class, DynamoDBConnection.class})
@Singleton
public class VideoDetailsRepository {

    private static final Logger LOG = LoggerFactory.getLogger(VideoDetailsRepository.class);

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
        String id = UUID.randomUUID().toString();
        LOG.debug(String.format("Persisting video details for video with Id: %s...", id));
        VideoDetails persistedVideoDetails = videoDetailsOf(dynamoDBConnection.saveItem(Map.of(
                        ID_FIELD_NAME, AttributeValue.builder().s(id).build(),
                        TITLE_FIELD_NAME, AttributeValue.builder().s(videoDetails.getTitle()).build(),
                        DESCRIPTION_FIELD_NAME, AttributeValue.builder().s(videoDetails.getDescription()).build()),
                dynamoConfiguration.getVideoDetails().getTableName()));
        LOG.debug(String.format("Successfully persisted video details for video with Id: %s.", id));
        return persistedVideoDetails;
    }

    public VideoDetails getVideoDetails(String id) {
        LOG.debug(String.format("Retrieving video details for video with Id: %s...", id));
        VideoDetails videoDetails = dynamoDBConnection.getItem("videoId", id, dynamoConfiguration.getVideoDetails().getTableName())
                .map(this::videoDetailsOf)
                .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND, String.format("Video not found for Id: %s.", id)));
        LOG.debug(String.format("Successfully retrieved video details for video with Id: %s.", id));
        return videoDetails;
    }

    private VideoDetails videoDetailsOf(Map<String, AttributeValue> item) {
        return VideoDetails.builder()
                .id(dynamoDBMapper.getString(item, ID_FIELD_NAME))
                .title(dynamoDBMapper.getString(item, TITLE_FIELD_NAME))
                .description(dynamoDBMapper.getString(item, DESCRIPTION_FIELD_NAME))
                .build();
    }
}
