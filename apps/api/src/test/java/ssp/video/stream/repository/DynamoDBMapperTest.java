package ssp.video.stream.repository;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DynamoDBMapperTest {

    @Test
    void should_map_string_attribute() {
        String fieldName = "testField";
        String fieldValue = "123";
        var item = Map.of(fieldName, AttributeValue.builder().s(fieldValue).build());
        DynamoDBMapper mapper = new DynamoDBMapper();
        assertEquals(fieldValue, mapper.getString(item, fieldName));
    }

    @Test
    void should_not_map_int_as_string_attribute() {
        String fieldName = "testField";
        int fieldValue = 123;
        var item = Map.of(fieldName, AttributeValue.builder().n(Integer.toString(fieldValue)).build());
        DynamoDBMapper mapper = new DynamoDBMapper();
        assertNull(mapper.getString(item, fieldName));
    }

    @Test
    void should_not_map_null_field_as_string_attribute() {
        String fieldName = "testField";
        DynamoDBMapper mapper = new DynamoDBMapper();
        assertNull(mapper.getString(Collections.emptyMap(), fieldName));
    }
}
