package ssp.video.stream.metadata.enrich;

import jakarta.inject.Singleton;
import ssp.video.stream.data.VideoDetails;

@Singleton
public class MetadataEnricher {

    public VideoDetails enrich(VideoDetails videoDetails) {
        videoDetails.setDuration(123);
        return videoDetails;
    }
}
