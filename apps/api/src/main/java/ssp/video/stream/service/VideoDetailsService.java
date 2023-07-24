package ssp.video.stream.service;

import jakarta.inject.Singleton;
import ssp.video.stream.controller.data.VideoDetails;
import ssp.video.stream.repository.VideoDetailsRepository;
import ssp.video.stream.repository.VideoRepository;

@Singleton
public class VideoDetailsService {

    VideoDetailsRepository videoDetailsRepository;
    VideoRepository videoRepository;

    public VideoDetailsService(VideoDetailsRepository videoDetailsRepository, VideoRepository videoRepository) {
        this.videoDetailsRepository = videoDetailsRepository;
        this.videoRepository = videoRepository;
    }

    public VideoDetails getVideoDetails(String id) {
        return videoDetailsRepository.getVideoDetails(id);
    }

    public VideoDetails saveVideoDetails(VideoDetails videoDetails) {
        return videoDetailsRepository.saveVideoDetails(videoDetails);
    }

    public String getPresignedUrlForVideoUpload(String filename) {
        return videoRepository.getPresignedUrlForVideoUpload(filename);
    }
}
