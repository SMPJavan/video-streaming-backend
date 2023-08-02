package ssp.video.stream.metadata.enrich;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import ssp.video.stream.configuration.MetadataEnrichConfiguration;
import ssp.video.stream.data.VideoDetails;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class MetadataEnricherTest {

    @Test
    void should_enrich_video_details() {
        String videoId = "123";
        String bucket = "test-bucket";
        S3Client s3Client = mock(S3Client.class);
        MetadataEnrichConfiguration config = mock(MetadataEnrichConfiguration.class);
        doReturn(bucket).when(config).getS3BucketName();
        GetObjectResponse getObjectResponse = mock(GetObjectResponse.class);
        ResponseInputStream<GetObjectResponse> responseInputStream = mock(ResponseInputStream.class);
        doReturn(getObjectResponse).when(responseInputStream).response();
        Map<String, String> metadata = Map.of("testkey1", "testval1", "testkey2", "testval2");
        doReturn(metadata).when(getObjectResponse).metadata();
        doReturn(responseInputStream).when(s3Client).getObject(any(GetObjectRequest.class));

        MetadataEnricher enricher = new MetadataEnricher(s3Client, config);
        VideoDetails response = enricher.enrich(VideoDetails.builder().id(videoId).build());

        assertTrue(response.getAvailableToStream());
    }
}
