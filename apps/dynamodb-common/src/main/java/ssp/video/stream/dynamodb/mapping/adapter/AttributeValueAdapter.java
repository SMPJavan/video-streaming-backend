package ssp.video.stream.dynamodb.mapping.adapter;


import java.util.Map;
import java.util.Optional;

public interface AttributeValueAdapter<T> {
    String getString(Map<String, T> item, String fieldName);

    Integer getInteger(Map<String, T> item, String fieldName);

    Boolean getBoolean(Map<String, T> item, String fieldName);

    <U> Optional<T> mapAttribute(U value);
}
