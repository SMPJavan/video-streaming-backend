package ssp.video.stream.repository;


import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;
import ssp.video.stream.configuration.S3Configuration;

@Requires(beans = {S3Configuration.class, S3Connection.class})
@Singleton
public class VideoRepository {

    private final S3Connection s3Connection;
    private final S3Configuration s3Configuration;

    public VideoRepository(S3Connection s3Connection, S3Configuration s3Configuration) {
        this.s3Connection = s3Connection;
        this.s3Configuration = s3Configuration;
    }

    public String getPresignedUrlForVideoUpload(String filename) {
        return this.s3Connection.generatePresignedPost(this.s3Configuration.getVideosConfiguration().getBucket(), filename);
    }
}
