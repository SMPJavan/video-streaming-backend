package ssp.video.stream.repository;

import org.junit.jupiter.api.Test;
import ssp.video.stream.configuration.S3Configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class VideoRepositoryTest {

    @Test
    void should_get_presigned_url() {
        String filename = "12345";
        String bucket = "test-bucket";
        String url = "https://test-bucket/12345";
        S3Connection s3Connection = mock(S3Connection.class);
        S3Configuration s3Configuration = mock(S3Configuration.class);
        S3Configuration.VideosConfiguration videosConfiguration = mock(S3Configuration.VideosConfiguration.class);
        VideoRepository repository = new VideoRepository(s3Connection, s3Configuration);
        doReturn(videosConfiguration).when(s3Configuration).getVideosConfiguration();
        doReturn(bucket).when(videosConfiguration).getBucket();
        doReturn(url).when(s3Connection).generatePresignedPost(bucket, filename);
        assertEquals(url, repository.getPresignedUrlForVideoUpload(filename));
    }
}
