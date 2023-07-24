package ssp.video.stream.repository;


import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ssp.video.stream.configuration.S3Configuration;

@Requires(beans = {S3Configuration.class, S3Connection.class})
@Singleton
public class VideoRepository {

    private static final Logger LOG = LoggerFactory.getLogger(VideoRepository.class);

    private final S3Connection s3Connection;
    private final S3Configuration s3Configuration;

    public VideoRepository(S3Connection s3Connection, S3Configuration s3Configuration) {
        this.s3Connection = s3Connection;
        this.s3Configuration = s3Configuration;
    }

    public String getPresignedUrlForVideoUpload(String filename) {
        LOG.debug(String.format("Requesting presigned url from AWS for filename: %s...", filename));
        String url = this.s3Connection.generatePresignedPost(this.s3Configuration.getVideosConfiguration().getBucket(), filename);
        LOG.debug(String.format("Successfully received presigned url from AWS for filename: %s.", filename));
        return url;
    }
}
