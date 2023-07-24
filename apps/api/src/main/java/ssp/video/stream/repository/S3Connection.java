package ssp.video.stream.repository;

import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Requires(beans = {AwsCredentialsProvider.class})
@Singleton
public class S3Connection {
    private final S3Presigner s3Presigner;

    public S3Connection(AwsCredentialsProvider credentialsProvider) {
        this.s3Presigner = S3Presigner.builder().credentialsProvider(credentialsProvider).build();
    }

    S3Connection(S3Presigner s3Presigner) {
        this.s3Presigner = s3Presigner;
    }

    public String generatePresignedPost(String bucket, String key) {
        PutObjectPresignRequest putObjectPresignRequest =
                PutObjectPresignRequest.builder().putObjectRequest(
                                PutObjectRequest.builder().bucket(bucket).key(key).build())
                        .signatureDuration(Duration.of(5L, ChronoUnit.MINUTES))
                        .build();
        PresignedPutObjectRequest presignedPutObjectRequest = s3Presigner.presignPutObject(putObjectPresignRequest);
        return presignedPutObjectRequest.url().toString();
    }
}
