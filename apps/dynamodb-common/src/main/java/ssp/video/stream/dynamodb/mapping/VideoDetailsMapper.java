package ssp.video.stream.dynamodb.mapping;

import jakarta.inject.Singleton;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import ssp.video.stream.data.VideoDetails;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class VideoDetailsMapper extends Mapper<VideoDetails> {

    private final String ID_FIELD_NAME = "videoId";
    private final String TITLE_FIELD_NAME = "title";
    private final String DESCRIPTION_FIELD_NAME = "description";
    private final String DURATION_FIELD_NAME = "duration";

    @Override
    public VideoDetails map(Map<String, AttributeValue> item) {
        return VideoDetails.builder()
                .id(getString(item, ID_FIELD_NAME))
                .title(getString(item, TITLE_FIELD_NAME))
                .description(getString(item, DESCRIPTION_FIELD_NAME))
                .duration(getInteger(item, DURATION_FIELD_NAME))
                .build();
    }

    @Override
    public Map<String, AttributeValue> toAttributeMap(VideoDetails data) {
        Map<String, AttributeValue> attributeMap = new HashMap<>();
        mapAttribute(data.getId()).ifPresent(attributeValue -> attributeMap.put(ID_FIELD_NAME, attributeValue));
        mapAttribute(data.getTitle()).ifPresent(attributeValue -> attributeMap.put(TITLE_FIELD_NAME, attributeValue));
        mapAttribute(data.getDescription()).ifPresent(attributeValue -> attributeMap.put(DESCRIPTION_FIELD_NAME, attributeValue));
        mapAttribute(data.getDuration()).ifPresent(attributeValue -> attributeMap.put(DURATION_FIELD_NAME, attributeValue));
        return attributeMap;
    }
}
