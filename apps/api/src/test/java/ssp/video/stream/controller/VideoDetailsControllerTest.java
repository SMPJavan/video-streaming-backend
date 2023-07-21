package ssp.video.stream.controller;

import org.junit.jupiter.api.Test;
import ssp.video.stream.controller.data.VideoDetails;
import ssp.video.stream.service.VideoDetailsService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class VideoDetailsControllerTest {

    @Test
    void should_get_video_details() {
        String videoId = "123";
        VideoDetails videoDetails = VideoDetails.builder().id(videoId).build();
        VideoDetailsService videoDetailsService = mock(VideoDetailsService.class);
        doReturn(videoDetails).when(videoDetailsService).getVideoDetails(videoId);
        VideoDetailsController controller = new VideoDetailsController(videoDetailsService);
        assertEquals(videoDetails, controller.getVideo(videoId).getVideoDetails());
    }
}
