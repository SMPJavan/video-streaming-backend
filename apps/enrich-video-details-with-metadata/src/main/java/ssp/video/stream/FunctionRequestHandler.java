package ssp.video.stream;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import io.micronaut.function.aws.MicronautRequestHandler;
import io.micronaut.json.JsonMapper;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import ssp.video.stream.configuration.MetadataEnrichConfiguration;
import ssp.video.stream.dynamodb.connection.DynamoDBConnection;
import ssp.video.stream.dynamodb.mapping.VideoDetailsMapper;
import ssp.video.stream.events.EventParser;
import ssp.video.stream.metadata.enrich.MetadataEnricher;
import ssp.video.stream.metadata.enrich.Processor;

import java.io.IOException;

public class FunctionRequestHandler extends MicronautRequestHandler<SQSEvent, Void> {
    @Inject
    JsonMapper objectMapper;

    @Inject
    DynamoDbClient dynamoDbClient;

    @Inject
    DynamoDBConnection dynamoDBConnection;

    @Inject
    VideoDetailsMapper videoDetailsMapper;

    @Inject
    MetadataEnricher metadataEnricher;

    @Inject
    MetadataEnrichConfiguration metadataEnrichConfiguration;

    @Inject
    Processor processor;

    @Inject
    private EventParser parser;

    @Override
    public Void execute(SQSEvent input) {
        try {
            for (SQSEvent.SQSMessage message : input.getRecords()) {
                processor.process(message);
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }
}
