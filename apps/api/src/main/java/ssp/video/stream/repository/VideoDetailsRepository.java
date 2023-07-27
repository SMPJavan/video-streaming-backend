package ssp.video.stream.repository;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ssp.video.stream.configuration.DynamoConfiguration;
import ssp.video.stream.data.VideoDetails;
import ssp.video.stream.dynamodb.connection.DynamoDBConnection;
import ssp.video.stream.dynamodb.mapping.VideoDetailsMapper;

import java.util.UUID;

@Requires(beans = {DynamoDBConnection.class, VideoDetailsMapper.class})
@Singleton
public class VideoDetailsRepository {

    private static final Logger LOG = LoggerFactory.getLogger(VideoDetailsRepository.class);

    private final DynamoDBConnection dynamoDBConnection;
    private final DynamoConfiguration dynamoConfiguration;

    private final VideoDetailsMapper videoDetailsMapper;

    public VideoDetailsRepository(DynamoDBConnection dynamoDBConnection,
                                  DynamoConfiguration dynamoConfiguration,
                                  VideoDetailsMapper videoDetailsMapper) {
        this.dynamoDBConnection = dynamoDBConnection;
        this.dynamoConfiguration = dynamoConfiguration;
        this.videoDetailsMapper = videoDetailsMapper;

    }

    public VideoDetails saveVideoDetails(VideoDetails videoDetails) {
        String id = UUID.randomUUID().toString();
        LOG.debug(String.format("Persisting video details for video with Id: %s...", id));
        videoDetails.setId(id);
        VideoDetails persistedVideoDetails = dynamoDBConnection.saveItem(videoDetails,
                dynamoConfiguration.getVideoDetails().getTableName(), videoDetailsMapper);
        LOG.debug(String.format("Successfully persisted video details for video with Id: %s.", id));
        return persistedVideoDetails;
    }

    public VideoDetails getVideoDetails(String id) {
        LOG.debug(String.format("Retrieving video details for video with Id: %s...", id));
        VideoDetails videoDetails = dynamoDBConnection.getItem("videoId", id, dynamoConfiguration.getVideoDetails().getTableName(), videoDetailsMapper)
                .orElseThrow(() -> new HttpStatusException(HttpStatus.NOT_FOUND, String.format("Video not found for Id: %s.", id)));
        LOG.debug(String.format("Successfully retrieved video details for video with Id: %s.", id));
        return videoDetails;
    }
}
