package ssp.video.stream.service;

import jakarta.inject.Singleton;
import ssp.video.stream.controller.data.VideoDetails;
import ssp.video.stream.repository.VideoDetailsRepository;

@Singleton
public class VideoDetailsService {

    VideoDetailsRepository videoDetailsRepository;

    public VideoDetailsService(VideoDetailsRepository videoDetailsRepository) {
        this.videoDetailsRepository = videoDetailsRepository;
    }

    public VideoDetails getVideoDetails(String id) {
        return videoDetailsRepository.getVideoDetails(id);
    }
}
