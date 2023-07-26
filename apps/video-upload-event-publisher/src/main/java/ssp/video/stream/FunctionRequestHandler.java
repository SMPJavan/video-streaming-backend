package ssp.video.stream;

import com.amazonaws.services.s3.event.S3EventNotification;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.function.aws.MicronautRequestHandler;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import ssp.video.stream.configuration.EventBridgeConfiguration;

@Introspected
public class FunctionRequestHandler
        extends MicronautRequestHandler<S3EventNotification, Void> {
    private static final Logger LOG = LoggerFactory.getLogger(FunctionRequestHandler.class);

    @Inject
    private EventBridgeClient eventBridgeClient;

    @Inject
    private EventBridgeConfiguration eventBridgeConfiguration;

    @Inject
    private VideoUploadedNotificationProcessor processor;

    @Override
    public Void execute(S3EventNotification input) {
        LOG.info(input.toString());
        if (input.getRecords() == null) {
            LOG.error("Records null.");
            return null;
        } else if (input.getRecords().size() == 0) {
            LOG.error("Records is empty.");
        }
        for (S3EventNotification.S3EventNotificationRecord record : input.getRecords()) {
            try {
                processor.process(record);
            } catch (JsonProcessingException e) {
                LOG.error("JSON parsing exception.", e);
            }
        }
        return null;
    }
}
