package ssp.video.stream.metadata.enrich;

import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import ssp.video.stream.configuration.MetadataEnrichConfiguration;
import ssp.video.stream.data.VideoDetails;

@Singleton
@Requires(classes = {S3Client.class})
public class MetadataEnricher {

    private static final Logger LOG = LoggerFactory.getLogger(MetadataEnricher.class);
    private static final String METADATA_LIST_SEPARATOR = ", ";

    private final S3Client s3Client;
    private final MetadataEnrichConfiguration metadataEnrichConfiguration;

    public MetadataEnricher(S3Client s3Client, MetadataEnrichConfiguration metadataEnrichConfiguration) {
        this.s3Client = s3Client;
        this.metadataEnrichConfiguration = metadataEnrichConfiguration;
    }

    public VideoDetails enrich(VideoDetails videoDetails) {
        String videoId = videoDetails.getId();
        var s3ObjectResponse = s3Client.getObject(
                GetObjectRequest.builder().bucket(metadataEnrichConfiguration.getS3BucketName()).key(videoId).build());

        //TODO: Replace logging with data enrichment
        var metadataStringBuilder = new StringBuilder();
        metadataStringBuilder.append(String.format("VideoId: %s | Metadata: {", videoDetails.getId()));
        s3ObjectResponse.response().metadata().forEach((key, value) -> metadataStringBuilder.append(jsonFormat(key)).append(": ")
                .append(jsonFormat(value))
                .append(METADATA_LIST_SEPARATOR));
        metadataStringBuilder.delete(metadataStringBuilder.length() - METADATA_LIST_SEPARATOR.length(), metadataStringBuilder.length());
        metadataStringBuilder.append("}");
        LOG.info(metadataStringBuilder.toString());

        //TODO: Replace dummy value here with results of Rekognition scan
        videoDetails.setAvailableToStream(true);

        return videoDetails;
    }

    private String jsonFormat(String input) {
        return String.format("\"%s\"", input);
    }
}
