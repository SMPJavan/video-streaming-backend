package ssp.video.stream.controller;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import ssp.video.stream.controller.data.VideoDetailsResponse;
import ssp.video.stream.controller.data.VideosSetResponse;
import ssp.video.stream.service.VideoDetailsService;

@Controller("videos")
public class VideoDetailsController {

    private VideoDetailsService videoDetailsService;

    public VideoDetailsController(VideoDetailsService videoDetailsService) {
        this.videoDetailsService = videoDetailsService;
    }

    @Get
    public VideosSetResponse getVideos() {
        return new VideosSetResponse();
    }

    @Get(uri = "/{videoId}")
    public VideoDetailsResponse getVideo(@PathVariable String videoId) {
        return new VideoDetailsResponse(videoDetailsService.getVideoDetails(videoId));
    }
}
