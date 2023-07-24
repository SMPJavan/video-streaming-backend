package ssp.video.stream.service;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ssp.video.stream.controller.data.VideoDetails;
import ssp.video.stream.repository.VideoDetailsRepository;
import ssp.video.stream.repository.VideoRepository;

@Singleton
public class VideoDetailsService {

    private static final Logger LOG = LoggerFactory.getLogger(VideoDetailsService.class);

    private final VideoDetailsRepository videoDetailsRepository;
    private final VideoRepository videoRepository;

    public VideoDetailsService(VideoDetailsRepository videoDetailsRepository, VideoRepository videoRepository) {
        this.videoDetailsRepository = videoDetailsRepository;
        this.videoRepository = videoRepository;
    }

    public VideoDetails getVideoDetails(String id) {
        LOG.debug(String.format("Retrieving video details for id: %s...", id));
        VideoDetails details = videoDetailsRepository.getVideoDetails(id);
        LOG.debug(String.format("Successfully retrieve video details for id: %s.", id));
        return details;
    }

    public VideoDetails saveVideoDetails(VideoDetails videoDetails) {
        LOG.debug("Saving new video details...");
        VideoDetails details = videoDetailsRepository.saveVideoDetails(videoDetails);
        LOG.debug(String.format("Successfully saved new video details for id: %s.", details.getId()));
        return details;
    }

    public String getPresignedUrlForVideoUpload(String filename) {
        LOG.debug(String.format("Getting presignedUrl for video with filename: %s...", filename));
        String url = videoRepository.getPresignedUrlForVideoUpload(filename);
        LOG.debug(String.format("Successfully got presignedUrl for video with filename: %s.", filename));
        return url;
    }
}
