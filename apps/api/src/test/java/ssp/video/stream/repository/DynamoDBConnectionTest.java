package ssp.video.stream.repository;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import ssp.video.stream.repository.DynamoDBConnection;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DynamoDBConnectionTest {

    @Test
    void should_get_item_by_partition_key() {
        String partitionKeyName = "partKey";
        String partitionKeyValue = "123";
        String tableName = "testTable";
        var data = Map.of(partitionKeyName, AttributeValue.builder().s(partitionKeyValue).build());
        DynamoDbClient dynamoDBClient = mock(DynamoDbClient.class);
        GetItemResponse getItemResponse = mock(GetItemResponse.class);
        doReturn(data).when(getItemResponse).item();
        doReturn(true).when(getItemResponse).hasItem();
        doReturn(getItemResponse).when(dynamoDBClient).getItem(any(GetItemRequest.class));
        DynamoDBConnection dynamoDBConnection = new DynamoDBConnection(dynamoDBClient);
        var result = dynamoDBConnection.getItem(partitionKeyName, partitionKeyValue, tableName);
        assertTrue(result.isPresent());
        assertEquals(data, result.get());
        ArgumentCaptor<GetItemRequest> argument = ArgumentCaptor.forClass(GetItemRequest.class);
        verify(dynamoDBClient).getItem(argument.capture());
        assertEquals(partitionKeyValue, argument.getValue().key().get(partitionKeyName).s());
    }

    @Test
    void should_get_item_by_partition_key_and_sort_key() {
        String partitionKeyName = "partKey";
        String partitionKeyValue = "123";
        String sortKeyName = "sortKey";
        String sortKeyValue = "456";
        String tableName = "testTable";
        var data = Map.of(partitionKeyName, AttributeValue.builder().s(partitionKeyValue).build(),
                sortKeyName, AttributeValue.builder().s(sortKeyValue).build());
        DynamoDbClient dynamoDBClient = mock(DynamoDbClient.class);
        GetItemResponse getItemResponse = mock(GetItemResponse.class);
        doReturn(data).when(getItemResponse).item();
        doReturn(true).when(getItemResponse).hasItem();
        doReturn(getItemResponse).when(dynamoDBClient).getItem(any(GetItemRequest.class));
        DynamoDBConnection dynamoDBConnection = new DynamoDBConnection(dynamoDBClient);
        var result = dynamoDBConnection.getItem(partitionKeyName, partitionKeyValue, sortKeyName, sortKeyValue, tableName);
        assertTrue(result.isPresent());
        assertEquals(data, result.get());
        ArgumentCaptor<GetItemRequest> argument = ArgumentCaptor.forClass(GetItemRequest.class);
        verify(dynamoDBClient).getItem(argument.capture());
        assertEquals(partitionKeyValue, argument.getValue().key().get(partitionKeyName).s());
        assertEquals(sortKeyValue, argument.getValue().key().get(sortKeyName).s());
    }

    @Test
    void should_get_empty_optional_when_item_not_found() {
        String partitionKeyName = "partKey";
        String partitionKeyValue = "123";
        String tableName = "testTable";
        DynamoDbClient dynamoDBClient = mock(DynamoDbClient.class);
        GetItemResponse getItemResponse = mock(GetItemResponse.class);
        doReturn(false).when(getItemResponse).hasItem();
        doReturn(getItemResponse).when(dynamoDBClient).getItem(any(GetItemRequest.class));
        DynamoDBConnection dynamoDBConnection = new DynamoDBConnection(dynamoDBClient);
        var result = dynamoDBConnection.getItem(partitionKeyName, partitionKeyValue, tableName);
        assertFalse(result.isPresent());
    }
}
