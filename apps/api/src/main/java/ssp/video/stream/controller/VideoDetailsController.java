package ssp.video.stream.controller;

import io.micronaut.http.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ssp.video.stream.controller.data.*;
import ssp.video.stream.service.VideoDetailsService;

@Controller("videos")
public class VideoDetailsController {

    private static final Logger LOG = LoggerFactory.getLogger(VideoDetailsController.class);

    private final VideoDetailsService videoDetailsService;

    public VideoDetailsController(VideoDetailsService videoDetailsService) {
        this.videoDetailsService = videoDetailsService;
    }

    @Get(uri = "/{videoId}")
    public GetVideoDetailsResponse getVideo(@PathVariable String videoId) {
        LOG.info(String.format("Processing GET request for video details with id: %s...", videoId));
        GetVideoDetailsResponse response = new GetVideoDetailsResponse(videoDetailsService.getVideoDetails(videoId));
        LOG.info(String.format("Successfully processed GET request for video details with id: %s.", videoId));
        return response;
    }

    @Post
    public PostVideoDetailsResponse saveVideo(@Body PostVideoDetailsRequest postVideoDetailsRequest) {
        LOG.info("Processing POST request for new video details...");
        VideoDetails newVideoDetails = videoDetailsService.saveVideoDetails(postVideoDetailsRequest.getVideoDetails());
        PostVideoDetailsResponse postVideoDetailsResponse = new PostVideoDetailsResponse(
                videoDetailsService.getPresignedUrlForVideoUpload(newVideoDetails.getId()),
                newVideoDetails);
        LOG.info(String.format("Successfully processed POST request for new video details (id: %s).", newVideoDetails.getId()));
        return postVideoDetailsResponse;

    }
}
