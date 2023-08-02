package ssp.video.stream.dynamodb.mapping;

import ssp.video.stream.dynamodb.mapping.adapter.AttributeValueAdapter;

import java.util.Map;

public interface Mapper<T> {
    <U> T map(Map<String, U> item, AttributeValueAdapter<U> adapter);

    <U> Map<String, U> toModelAttributeMap(T data, AttributeValueAdapter<U> adapter);
}
