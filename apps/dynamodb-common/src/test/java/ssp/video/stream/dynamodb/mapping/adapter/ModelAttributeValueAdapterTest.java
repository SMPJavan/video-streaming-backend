package ssp.video.stream.dynamodb.mapping.adapter;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ModelAttributeValueAdapterTest {

    @Test
    void should_get_string() {
        String fieldName = "testField";
        String fieldValue = "testValue";
        Map<String, AttributeValue> item = Map.of(fieldName, AttributeValue.builder().s(fieldValue).build());
        ModelAttributeValueAdapter adapter = new ModelAttributeValueAdapter();
        assertEquals(fieldValue, adapter.getString(item, fieldName));
    }

    @Test
    void should_not_get_string_when_value_is_null() {
        String fieldName = "testField";
        Map<String, AttributeValue> item = new HashMap<>();
        item.put(fieldName, null);
        ModelAttributeValueAdapter adapter = new ModelAttributeValueAdapter();
        assertNull(adapter.getString(item, fieldName));
    }

    @Test
    void should_not_get_string_when_type_is_number() {
        String fieldName = "testField";
        String fieldValue = "123";
        Map<String, AttributeValue> item = Map.of(fieldName, AttributeValue.builder().n(fieldValue).build());
        ModelAttributeValueAdapter adapter = new ModelAttributeValueAdapter();
        assertNull(adapter.getString(item, fieldName));
    }

    @Test
    void should_get_integer() {
        String fieldName = "testField";
        int fieldValue = 123;
        Map<String, AttributeValue> item = Map.of(fieldName, AttributeValue.builder().n(String.valueOf(fieldValue)).build());
        ModelAttributeValueAdapter adapter = new ModelAttributeValueAdapter();
        assertEquals(fieldValue, adapter.getInteger(item, fieldName));
    }

    @Test
    void should_not_get_integer_when_value_is_null() {
        String fieldName = "testField";
        Map<String, AttributeValue> item = new HashMap<>();
        item.put(fieldName, null);
        ModelAttributeValueAdapter adapter = new ModelAttributeValueAdapter();
        assertNull(adapter.getInteger(item, fieldName));
    }

    @Test
    void should_not_get_integer_when_type_is_string() {
        String fieldName = "testField";
        String fieldValue = "testValue";
        Map<String, AttributeValue> item = Map.of(fieldName, AttributeValue.builder().s(fieldValue).build());
        ModelAttributeValueAdapter adapter = new ModelAttributeValueAdapter();
        assertNull(adapter.getInteger(item, fieldName));
    }

    @Test
    void should_get_boolean() {
        String fieldName = "testField";
        Boolean fieldValue = true;
        Map<String, AttributeValue> item = Map.of(fieldName, AttributeValue.builder().bool(fieldValue).build());
        ModelAttributeValueAdapter adapter = new ModelAttributeValueAdapter();
        assertEquals(fieldValue, adapter.getBoolean(item, fieldName));
    }

    @Test
    void should_not_get_boolean_when_value_is_null() {
        String fieldName = "testField";
        Map<String, AttributeValue> item = new HashMap<>();
        item.put(fieldName, null);
        ModelAttributeValueAdapter adapter = new ModelAttributeValueAdapter();
        assertNull(adapter.getBoolean(item, fieldName));
    }

    @Test
    void should_not_get_boolean_when_type_is_string() {
        String fieldName = "testField";
        String fieldValue = "true";
        Map<String, AttributeValue> item = Map.of(fieldName, AttributeValue.builder().s(fieldValue).build());
        ModelAttributeValueAdapter adapter = new ModelAttributeValueAdapter();
        assertNull(adapter.getBoolean(item, fieldName));
    }

    @Test
    void should_map_string_value() {
        String fieldValue = "testValue";
        ModelAttributeValueAdapter adapter = new ModelAttributeValueAdapter();
        assertEquals(fieldValue, adapter.mapAttribute(fieldValue).get().s());
    }

    @Test
    void should_map_integer_value() {
        Integer fieldValue = 123;
        ModelAttributeValueAdapter adapter = new ModelAttributeValueAdapter();
        assertEquals(String.valueOf(fieldValue), adapter.mapAttribute(fieldValue).get().n());
    }

    @Test
    void should_map_boolean_value() {
        Boolean fieldValue = true;
        ModelAttributeValueAdapter adapter = new ModelAttributeValueAdapter();
        assertEquals(fieldValue, adapter.mapAttribute(fieldValue).get().bool());
    }
}
