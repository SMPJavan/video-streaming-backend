package ssp.video.stream;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.ApplicationContextBuilder;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.function.aws.HandlerUtils;
import io.micronaut.function.aws.LambdaApplicationContextBuilder;
import io.micronaut.function.aws.MicronautLambdaContext;
import io.micronaut.function.executor.AbstractFunctionExecutor;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import ssp.video.stream.configuration.EventBridgeConfiguration;
import ssp.video.stream.dynamodb.mapping.adapter.StreamAttributeValueAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/*
    This class is necessary as it is not currently possible to deserialize
    com.amazonaws.services.lambda.runtime.events.DynamodbEvent with Micronaut's
    io.micronaut.function.aws.MicronautRequestHandler class
 */
@Introspected
public class FunctionRequestHandler extends AbstractFunctionExecutor<InputStream, Void, Context> implements RequestStreamHandler, MicronautLambdaContext {
    private static final Logger LOG = LoggerFactory.getLogger(FunctionRequestHandler.class);

    @Inject
    private EventBridgeClient eventBridgeClient;

    @Inject
    private EventBridgeConfiguration eventBridgeConfiguration;

    @Inject
    private StreamAttributeValueAdapter streamAttributeValueAdapter;

    @Inject
    private VideoDetailsUpdatedProcessor processor;

    public FunctionRequestHandler() {
        try {
            buildApplicationContext(null);
            injectIntoApplicationContext();
        } catch (Exception e) {
            LOG.error("Exception initializing handler", e);
            throw e;
        }
    }

    /**
     * Constructor used to inject a preexisting {@link ApplicationContext}.
     * @param applicationContext the application context
     */
    public FunctionRequestHandler(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;

        try {
            startEnvironment(applicationContext);
            injectIntoApplicationContext();
        } catch (Exception e) {
            LOG.error("Exception initializing handler: " + e.getMessage() , e);
            throw e;
        }
    }

    /**
     * Constructor used to inject a preexisting {@link ApplicationContextBuilder}.
     * @param applicationContextBuilder the application context builder
     */
    public FunctionRequestHandler(ApplicationContextBuilder applicationContextBuilder) {
        this(applicationContextBuilder.build());
    }

    @Override
    public Void execute(InputStream inputStr) {
        try {
            String inputString = new String(inputStr.readAllBytes(), StandardCharsets.UTF_8);
            LOG.info(inputString);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
            objectMapper.registerModule(new DateModule());
            objectMapper.registerModule(new JodaModule());
            DynamodbEvent input = objectMapper.readValue(inputString, DynamodbEvent.class);
            LOG.info(input.toString());
            for (DynamodbEvent.DynamodbStreamRecord record : input.getRecords()) {
                processor.process(record);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    protected ApplicationContext buildApplicationContext(Context context) {
        applicationContext = super.buildApplicationContext(context);
        startEnvironment(applicationContext);
        return applicationContext;
    }

    @Override
    @NonNull
    protected ApplicationContextBuilder newApplicationContextBuilder() {
        return new LambdaApplicationContextBuilder();
    }

    private void injectIntoApplicationContext() {
        applicationContext.inject(this);
    }

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) {
        HandlerUtils.configureWithContext(this, context);
        this.execute(input);
    }
}
