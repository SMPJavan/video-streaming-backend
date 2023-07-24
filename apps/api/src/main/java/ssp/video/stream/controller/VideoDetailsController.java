package ssp.video.stream.controller;

import io.micronaut.http.annotation.*;
import ssp.video.stream.controller.data.*;
import ssp.video.stream.service.VideoDetailsService;

@Controller("videos")
public class VideoDetailsController {

    private final VideoDetailsService videoDetailsService;

    public VideoDetailsController(VideoDetailsService videoDetailsService) {
        this.videoDetailsService = videoDetailsService;
    }

    @Get(uri = "/{videoId}")
    public GetVideoDetailsResponse getVideo(@PathVariable String videoId) {
        return new GetVideoDetailsResponse(videoDetailsService.getVideoDetails(videoId));
    }

    @Post
    public PostVideoDetailsResponse saveVideo(@Body PostVideoDetailsRequest postVideoDetailsRequest) {
        VideoDetails newVideoDetails = videoDetailsService.saveVideoDetails(postVideoDetailsRequest.getVideoDetails());
        return new PostVideoDetailsResponse(
                videoDetailsService.getPresignedUrlForVideoUpload(newVideoDetails.getId()),
                newVideoDetails);
    }
}
