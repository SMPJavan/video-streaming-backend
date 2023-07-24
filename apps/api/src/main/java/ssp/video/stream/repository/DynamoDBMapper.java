package ssp.video.stream.repository;

import jakarta.inject.Singleton;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;

@Singleton
public class DynamoDBMapper {
    public String getString(Map<String, AttributeValue> item, String fieldName) {
        String value = null;
        AttributeValue attributeValue = item.get(fieldName);
        if(attributeValue != null && AttributeValue.Type.S.equals(attributeValue.type()))
        {
            value = attributeValue.s();
        }
        return value;
    }
}
