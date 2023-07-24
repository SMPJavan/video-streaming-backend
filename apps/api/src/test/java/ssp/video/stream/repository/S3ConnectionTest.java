package ssp.video.stream.repository;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class S3ConnectionTest {

    @Test
    void should_construct_with_s3_presigner_from_aws_creds_provider() {
        AwsCredentialsProvider awsCredsProvider = mock(AwsCredentialsProvider.class);
        assertNotNull(new S3Connection(awsCredsProvider));
    }

    @Test
    void should_get_presigned_url() throws MalformedURLException {
        String bucket = "bucket";
        String key = "key";
        String url = "https://test/123";
        S3Presigner s3Presigner = mock(S3Presigner.class);
        S3Connection s3Connection = new S3Connection(s3Presigner);
        PresignedPutObjectRequest presignedPutObjectRequest = mock(PresignedPutObjectRequest.class);
        doReturn(new URL(url)).when(presignedPutObjectRequest).url();
        doReturn(presignedPutObjectRequest).when(s3Presigner).presignPutObject(any(PutObjectPresignRequest.class));
        assertEquals(url, s3Connection.generatePresignedPost(bucket, key));
    }
}
