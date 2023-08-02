package ssp.video.stream.dynamodb.mapping;

import jakarta.inject.Singleton;
import ssp.video.stream.data.VideoDetails;
import ssp.video.stream.dynamodb.mapping.adapter.AttributeValueAdapter;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class VideoDetailsMapper implements Mapper<VideoDetails> {

    private final String ID_FIELD_NAME = "videoId";
    private final String TITLE_FIELD_NAME = "title";
    private final String DESCRIPTION_FIELD_NAME = "description";
    private final String DURATION_FIELD_NAME = "duration";
    private final String AVAILABLE_TO_STREAM_FIELD_NAME = "availableToStream";

    @Override
    public <T> VideoDetails map(Map<String, T> item, AttributeValueAdapter<T> adapter) {
        return VideoDetails.builder()
                .id(adapter.getString(item, ID_FIELD_NAME))
                .title(adapter.getString(item, TITLE_FIELD_NAME))
                .description(adapter.getString(item, DESCRIPTION_FIELD_NAME))
                .duration(adapter.getInteger(item, DURATION_FIELD_NAME))
                .availableToStream(adapter.getBoolean(item, AVAILABLE_TO_STREAM_FIELD_NAME))
                .build();
    }

    @Override
    public <T> Map<String, T> toModelAttributeMap(VideoDetails data, AttributeValueAdapter<T> adapter) {
        Map<String, T> attributeMap = new HashMap<>();
        adapter.mapAttribute(data.getId()).ifPresent(attributeValue -> attributeMap.put(ID_FIELD_NAME, attributeValue));
        adapter.mapAttribute(data.getTitle()).ifPresent(attributeValue -> attributeMap.put(TITLE_FIELD_NAME, attributeValue));
        adapter.mapAttribute(data.getDescription()).ifPresent(attributeValue -> attributeMap.put(DESCRIPTION_FIELD_NAME, attributeValue));
        adapter.mapAttribute(data.getDuration()).ifPresent(attributeValue -> attributeMap.put(DURATION_FIELD_NAME, attributeValue));
        adapter.mapAttribute(data.getAvailableToStream()).ifPresent(attributeValue -> attributeMap.put(AVAILABLE_TO_STREAM_FIELD_NAME, attributeValue));
        return attributeMap;
    }
}
