package ssp.video.stream.s3object.transition;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CopyObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import ssp.video.stream.configuration.VideoMoveConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class S3ObjectTransitionerTest {

    @Test
    void should_move_video() {
        String videoId = "123";
        String sourceBucket = "source";
        String destinationBucket = "destination";

        S3Client s3Client = mock(S3Client.class);
        VideoMoveConfiguration config = mock(VideoMoveConfiguration.class);
        doReturn(sourceBucket).when(config).getSourceBucketName();
        doReturn(destinationBucket).when(config).getDestinationBucketName();

        S3ObjectTransitioner s3ObjectTransitioner = new S3ObjectTransitioner(s3Client, config);
        s3ObjectTransitioner.moveVideo(videoId);
        ArgumentCaptor<CopyObjectRequest> copyArgument = ArgumentCaptor.forClass(CopyObjectRequest.class);
        verify(s3Client).copyObject(copyArgument.capture());
        assertEquals(videoId, copyArgument.getValue().sourceKey());
        assertEquals(videoId, copyArgument.getValue().destinationKey());
        assertEquals(sourceBucket, copyArgument.getValue().sourceBucket());
        assertEquals(destinationBucket, copyArgument.getValue().destinationBucket());
        ArgumentCaptor<DeleteObjectRequest> deleteArgument = ArgumentCaptor.forClass(DeleteObjectRequest.class);
        verify(s3Client).deleteObject(deleteArgument.capture());
        assertEquals(sourceBucket, deleteArgument.getValue().bucket());
        assertEquals(videoId, deleteArgument.getValue().key());
    }
}
