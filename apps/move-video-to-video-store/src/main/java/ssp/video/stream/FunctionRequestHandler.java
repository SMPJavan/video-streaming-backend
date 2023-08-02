package ssp.video.stream;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import io.micronaut.function.aws.MicronautRequestHandler;
import io.micronaut.json.JsonMapper;
import jakarta.inject.Inject;
import software.amazon.awssdk.services.s3.S3Client;
import ssp.video.stream.configuration.VideoMoveConfiguration;
import ssp.video.stream.dynamodb.connection.DynamoDBConnection;
import ssp.video.stream.dynamodb.mapping.VideoDetailsMapper;
import ssp.video.stream.events.EventParser;
import ssp.video.stream.s3object.transition.Processor;
import ssp.video.stream.s3object.transition.S3ObjectTransitioner;

import java.io.IOException;

public class FunctionRequestHandler extends MicronautRequestHandler<SQSEvent, Void> {

    @Inject
    JsonMapper objectMapper;

    @Inject
    S3ObjectTransitioner s3ObjectTransitioner;

    @Inject
    VideoMoveConfiguration videoMoveConfiguration;

    @Inject
    Processor processor;

    @Inject
    private EventParser parser;

    @Inject
    private S3Client s3Client;

    @Inject
    private DynamoDBConnection dynamoDBConnection;

    @Inject
    private VideoDetailsMapper videoDetailsMapper;

    @Override
    public Void execute(SQSEvent input) {
        try {
            for (SQSEvent.SQSMessage message : input.getRecords()) {
                processor.process(message);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
