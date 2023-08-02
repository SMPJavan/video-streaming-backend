package ssp.video.stream.dynamodb.mapping.adapter;

import org.junit.jupiter.api.Test;
import com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class StreamAttributeValueAdapterTest {

    @Test
    void should_get_string() {
        String fieldName = "testField";
        String fieldValue = "testValue";
        Map<String, AttributeValue> item = Map.of(fieldName, new AttributeValue(fieldValue));
        StreamAttributeValueAdapter adapter = new StreamAttributeValueAdapter();
        assertEquals(fieldValue, adapter.getString(item, fieldName));
    }

    @Test
    void should_not_get_string_when_value_is_null() {
        String fieldName = "testField";
        Map<String, AttributeValue> item = new HashMap<>();
        item.put(fieldName, null);
        StreamAttributeValueAdapter adapter = new StreamAttributeValueAdapter();
        assertNull(adapter.getString(item, fieldName));
    }

    @Test
    void should_not_get_string_when_string_value_is_null() {
        String fieldName = "testField";
        String fieldValue = null;
        Map<String, AttributeValue> item = Map.of(fieldName, new AttributeValue(fieldValue));
        StreamAttributeValueAdapter adapter = new StreamAttributeValueAdapter();
        assertNull(adapter.getString(item, fieldName));
    }

    @Test
    void should_get_integer() {
        String fieldName = "testField";
        int fieldValue = 123;
        Map<String, AttributeValue> item = Map.of(fieldName, new AttributeValue().withN(String.valueOf(fieldValue)));
        StreamAttributeValueAdapter adapter = new StreamAttributeValueAdapter();
        assertEquals(fieldValue, adapter.getInteger(item, fieldName));
    }

    @Test
    void should_not_get_integer_when_value_is_null() {
        String fieldName = "testField";
        Map<String, AttributeValue> item = new HashMap<>();
        item.put(fieldName, null);
        StreamAttributeValueAdapter adapter = new StreamAttributeValueAdapter();
        assertNull(adapter.getInteger(item, fieldName));
    }

    @Test
    void should_not_get_integer_when_integer_value_is_null() {
        String fieldName = "testField";
        String fieldValue = null;
        Map<String, AttributeValue> item = Map.of(fieldName, new AttributeValue().withN(fieldValue));
        StreamAttributeValueAdapter adapter = new StreamAttributeValueAdapter();
        assertNull(adapter.getInteger(item, fieldName));
    }

    @Test
    void should_get_boolean() {
        String fieldName = "testField";
        Boolean fieldValue = true;
        Map<String, AttributeValue> item = Map.of(fieldName, new AttributeValue().withBOOL(fieldValue));
        StreamAttributeValueAdapter adapter = new StreamAttributeValueAdapter();
        assertEquals(fieldValue, adapter.getBoolean(item, fieldName));
    }

    @Test
    void should_not_get_boolean_when_value_is_null() {
        String fieldName = "testField";
        Map<String, AttributeValue> item = new HashMap<>();
        item.put(fieldName, null);
        StreamAttributeValueAdapter adapter = new StreamAttributeValueAdapter();
        assertNull(adapter.getBoolean(item, fieldName));
    }

    @Test
    void should_not_get_boolean_when_boolean_value_is_null() {
        String fieldName = "testField";
        Boolean fieldValue = null;
        Map<String, AttributeValue> item = Map.of(fieldName, new AttributeValue().withBOOL(fieldValue));
        StreamAttributeValueAdapter adapter = new StreamAttributeValueAdapter();
        assertNull(adapter.getBoolean(item, fieldName));
    }

    @Test
    void should_map_string_value() {
        String fieldValue = "testValue";
        StreamAttributeValueAdapter adapter = new StreamAttributeValueAdapter();
        assertFalse(adapter.mapAttribute(fieldValue).isPresent());
    }
}
