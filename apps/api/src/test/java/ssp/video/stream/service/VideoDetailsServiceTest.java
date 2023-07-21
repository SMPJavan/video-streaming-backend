package ssp.video.stream.service;

import org.junit.jupiter.api.Test;
import ssp.video.stream.controller.data.VideoDetails;
import ssp.video.stream.repository.VideoDetailsRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class VideoDetailsServiceTest {

    @Test
    void should_get_video_details(){
        String videoId = "123";
        VideoDetails videoDetails = VideoDetails.builder().id(videoId).build();
        VideoDetailsRepository videoDetailsRepository = mock(VideoDetailsRepository.class);
        doReturn(videoDetails).when(videoDetailsRepository).getVideoDetails(videoId);
        VideoDetailsService videoDetailsService = new VideoDetailsService(videoDetailsRepository);
        assertEquals(videoDetails, videoDetailsService.getVideoDetails(videoId));
    }
}
