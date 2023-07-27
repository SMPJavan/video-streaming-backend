package ssp.video.stream.service;

import org.junit.jupiter.api.Test;
import ssp.video.stream.data.VideoDetails;
import ssp.video.stream.repository.VideoDetailsRepository;
import ssp.video.stream.repository.VideoRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class VideoDetailsServiceTest {

    @Test
    void should_get_video_details(){
        String videoId = "123";
        VideoDetails videoDetails = VideoDetails.builder().id(videoId).build();
        VideoDetailsRepository videoDetailsRepository = mock(VideoDetailsRepository.class);
        VideoRepository videoRepository = mock(VideoRepository.class);
        doReturn(videoDetails).when(videoDetailsRepository).getVideoDetails(videoId);
        VideoDetailsService videoDetailsService = new VideoDetailsService(videoDetailsRepository, videoRepository);
        assertEquals(videoDetails, videoDetailsService.getVideoDetails(videoId));
    }

    @Test
    void should_save_video_details() {
        String videoId = "123";
        VideoDetails videoDetails = VideoDetails.builder().id(videoId).build();
        VideoDetailsRepository videoDetailsRepository = mock(VideoDetailsRepository.class);
        VideoRepository videoRepository = mock(VideoRepository.class);
        doReturn(videoDetails).when(videoDetailsRepository).saveVideoDetails(videoDetails);
        VideoDetailsService videoDetailsService = new VideoDetailsService(videoDetailsRepository, videoRepository);
        assertEquals(videoDetails, videoDetailsService.saveVideoDetails(videoDetails));
    }

    @Test
    void should_get_presigned_url_for_video_upload() {
        String filename = "123";
        String url = "https://test-bucket/123";
        VideoDetailsRepository videoDetailsRepository = mock(VideoDetailsRepository.class);
        VideoRepository videoRepository = mock(VideoRepository.class);
        VideoDetailsService videoDetailsService = new VideoDetailsService(videoDetailsRepository, videoRepository);
        doReturn(url).when(videoRepository).getPresignedUrlForVideoUpload(filename);
        assertEquals(url, videoDetailsService.getPresignedUrlForVideoUpload(filename));
    }
}
