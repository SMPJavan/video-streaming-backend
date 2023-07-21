package ssp.video.stream.repository;

import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;

import java.util.Map;
import java.util.Optional;

@Requires(beans = { DynamoDbClient.class })
@Singleton
public class DynamoDBConnection {

    private final DynamoDbClient dynamoDbClient;

    public DynamoDBConnection(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    public Optional<Map<String, AttributeValue>> getItem(String partitionKeyName, String partitionKeyValue, String sortKeyName, String sortKeyValue, String tableName) {
        return getItem(Map.of(partitionKeyName, AttributeValue.builder().s(partitionKeyValue).build(),
                        sortKeyName, AttributeValue.builder().s(sortKeyValue).build()),
                tableName);
    }

    public Optional<Map<String, AttributeValue>> getItem(String partitionKeyName, String partitionKeyValue, String tableName) {
        return getItem(Map.of(partitionKeyName, AttributeValue.builder().s(partitionKeyValue).build()),
                tableName);
    }

    private Optional<Map<String, AttributeValue>> getItem(Map<String, AttributeValue> key, String tableName) {
        GetItemResponse getItemResponse = dynamoDbClient.getItem(
                GetItemRequest.builder()
                        .tableName(tableName)
                        .key(key).build());
        return !getItemResponse.hasItem() ? Optional.empty() : Optional.of(getItemResponse.item());
    }
}
