package ssp.video.stream.dynamodb.mapping.adapter;

import jakarta.inject.Singleton;
import com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue;

import java.util.Map;
import java.util.Optional;

@Singleton
public class StreamAttributeValueAdapter implements AttributeValueAdapter<AttributeValue> {

    @Override
    public String getString(Map<String, AttributeValue> item, String fieldName) {
        String value = null;
        AttributeValue attributeValue = item.get(fieldName);
        if(attributeValue != null && attributeValue.getS() != null)
        {
            value = attributeValue.getS();
        }
        return value;
    }

    @Override
    public Integer getInteger(Map<String, AttributeValue> item, String fieldName) {
        Integer value = null;
        AttributeValue attributeValue = item.get(fieldName);
        if(attributeValue != null && attributeValue.getN() != null)
        {
            value = Integer.valueOf(attributeValue.getN());
        }
        return value;
    }

    @Override
    public Boolean getBoolean(Map<String, AttributeValue> item, String fieldName) {
        Boolean value = null;
        AttributeValue attributeValue = item.get(fieldName);
        if(attributeValue != null && attributeValue.getBOOL() != null)
        {
            value = attributeValue.getBOOL();
        }
        return value;
    }

    @Override
    public <U> Optional<AttributeValue> mapAttribute(U value) {
        return Optional.empty();
    }
}
