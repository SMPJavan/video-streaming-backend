package ssp.video.stream.s3object.transition;

import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import ssp.video.stream.configuration.VideoMoveConfiguration;

@Singleton
@Requires(classes = {S3Client.class, VideoMoveConfiguration.class})
public class S3ObjectTransitioner {

    private final S3Client s3Client;
    private final VideoMoveConfiguration videoMoveConfiguration;

    public S3ObjectTransitioner(S3Client s3Client, VideoMoveConfiguration videoMoveConfiguration) {
        this.s3Client = s3Client;
        this.videoMoveConfiguration = videoMoveConfiguration;
    }

    public void moveVideo(String objectKey) {
        s3Client.copyObject(CopyObjectRequest.builder().sourceBucket(videoMoveConfiguration.getSourceBucketName())
                .destinationBucket(videoMoveConfiguration.getDestinationBucketName()).sourceKey(objectKey)
                .destinationKey(objectKey).build());
        s3Client.deleteObject(DeleteObjectRequest.builder().bucket(videoMoveConfiguration.getSourceBucketName())
                .key(objectKey).build());
    }
}
