package ssp.video.stream.dynamodb.mapping;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Map;
import java.util.Optional;

public abstract class Mapper<T> {
    public abstract T map(Map<String, AttributeValue> item);

    public abstract Map<String, AttributeValue> toAttributeMap(T data);

    protected String getString(Map<String, AttributeValue> item, String fieldName) {
        String value = null;
        AttributeValue attributeValue = item.get(fieldName);
        if(attributeValue != null && AttributeValue.Type.S.equals(attributeValue.type()))
        {
            value = attributeValue.s();
        }
        return value;
    }

    protected Integer getInteger(Map<String, AttributeValue> item, String fieldName) {
        Integer value = null;
        AttributeValue attributeValue = item.get(fieldName);
        if(attributeValue != null && AttributeValue.Type.N.equals(attributeValue.type()))
        {
            value = Integer.valueOf(attributeValue.n());
        }
        return value;
    }

    protected <U> Optional<AttributeValue> mapAttribute(U value) {
        return value != null ? Optional.of(mapToAttribute(value)) : Optional.empty();
    }

    protected AttributeValue mapToAttribute(Object object) {
        if(object instanceof Integer) {
            return mapIntegerToAttribute((Integer)object);
        }
        else if (object instanceof String) {
            return mapStringToAttribute((String) object);
        }
        else return null;
    }

    protected AttributeValue mapStringToAttribute(String value) {
        return AttributeValue.builder().s(value).build();
    }

    protected AttributeValue mapIntegerToAttribute(Integer value) {
        return AttributeValue.builder().n(String.valueOf(value)).build();
    }
}
