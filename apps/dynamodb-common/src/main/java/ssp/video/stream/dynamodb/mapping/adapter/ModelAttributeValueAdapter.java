package ssp.video.stream.dynamodb.mapping.adapter;

import jakarta.inject.Singleton;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;
import java.util.Optional;

@Singleton
public class ModelAttributeValueAdapter implements AttributeValueAdapter<AttributeValue> {

    @Override
    public String getString(Map<String, AttributeValue> item, String fieldName) {
        String value = null;
        AttributeValue attributeValue = item.get(fieldName);
        if(attributeValue != null && AttributeValue.Type.S.equals(attributeValue.type()))
        {
            value = attributeValue.s();
        }
        return value;
    }

    @Override
    public Integer getInteger(Map<String, AttributeValue> item, String fieldName) {
        Integer value = null;
        AttributeValue attributeValue = item.get(fieldName);
        if(attributeValue != null && AttributeValue.Type.N.equals(attributeValue.type()))
        {
            value = Integer.valueOf(attributeValue.n());
        }
        return value;
    }

    @Override
    public Boolean getBoolean(Map<String, AttributeValue> item, String fieldName) {
        Boolean value = null;
        AttributeValue attributeValue = item.get(fieldName);
        if(attributeValue != null && AttributeValue.Type.BOOL.equals(attributeValue.type()))
        {
            value = attributeValue.bool();
        }
        return value;
    }

    @Override
    public <U> Optional<AttributeValue> mapAttribute(U value) {
        return value != null ? Optional.of(mapToAttribute(value)) : Optional.empty();
    }

    private AttributeValue mapToAttribute(Object object) {
        if(object instanceof Integer) {
            return mapIntegerToAttribute((Integer)object);
        }
        else if (object instanceof String) {
            return mapStringToAttribute((String) object);
        }
        else if (object instanceof Boolean) {
            return mapBooleanToAttribute((Boolean) object);
        }
        else return null;
    }

    private AttributeValue mapStringToAttribute(String value) {
        return AttributeValue.builder().s(value).build();
    }

    private AttributeValue mapIntegerToAttribute(Integer value) {
        return AttributeValue.builder().n(String.valueOf(value)).build();
    }

    private AttributeValue mapBooleanToAttribute(Boolean value) {
        return AttributeValue.builder().bool(value).build();
    }
}
