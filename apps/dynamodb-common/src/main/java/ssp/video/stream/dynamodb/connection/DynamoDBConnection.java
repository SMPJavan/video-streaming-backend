package ssp.video.stream.dynamodb.connection;

import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import ssp.video.stream.dynamodb.mapping.Mapper;
import ssp.video.stream.dynamodb.mapping.adapter.ModelAttributeValueAdapter;

import java.util.Map;
import java.util.Optional;

@Requires(beans = { DynamoDbClient.class, ModelAttributeValueAdapter.class })
@Singleton
public class DynamoDBConnection {

    private final DynamoDbClient dynamoDbClient;

    private final ModelAttributeValueAdapter modelAttributeValueAdapter;

    public DynamoDBConnection(DynamoDbClient dynamoDbClient, ModelAttributeValueAdapter modelAttributeValueAdapter) {
        this.dynamoDbClient = dynamoDbClient;
        this.modelAttributeValueAdapter = modelAttributeValueAdapter;
    }

    public <T> Optional<T> getItem(String partitionKeyName, String partitionKeyValue, String sortKeyName, String sortKeyValue, String tableName, Mapper<T> mapper) {
        return Optional.of(getItem(partitionKeyName, partitionKeyValue, sortKeyName, sortKeyValue, tableName).map(item -> mapper.map(item, modelAttributeValueAdapter))).orElse(Optional.empty());
    }

    public <T> Optional<T> getItem(String partitionKeyName, String partitionKeyValue, String tableName, Mapper<T> mapper) {
        return Optional.of(getItem(partitionKeyName, partitionKeyValue, tableName).map(item -> mapper.map(item, modelAttributeValueAdapter))).orElse(Optional.empty());
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

    public <T> T saveItem(T item, String tableName, Mapper<T> mapper) {
        return mapper.map(saveItem(mapper.toModelAttributeMap(item, modelAttributeValueAdapter), tableName), modelAttributeValueAdapter);
    }

    public Map<String, AttributeValue> saveItem(Map<String, AttributeValue> item, String tableName) {
        dynamoDbClient.putItem(PutItemRequest.builder().tableName(tableName).item(item).build());
        return item;
    }

    private Optional<Map<String, AttributeValue>> getItem(Map<String, AttributeValue> key, String tableName) {
        GetItemResponse getItemResponse = dynamoDbClient.getItem(
                GetItemRequest.builder()
                        .tableName(tableName)
                        .key(key).build());
        return !getItemResponse.hasItem() ? Optional.empty() : Optional.of(getItemResponse.item());
    }
}
