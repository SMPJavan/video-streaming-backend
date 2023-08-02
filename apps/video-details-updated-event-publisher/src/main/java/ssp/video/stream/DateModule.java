package ssp.video.stream;

import java.io.IOException;
import java.io.Serial;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.PackageVersion;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

/*
    This is adapted from the AWS SDK with the imports changed to jackson objects
 */
public class DateModule extends SimpleModule {
    @Serial
    private static final long serialVersionUID = 1L;

    public static final class Serializer extends JsonSerializer<Date> {
        @Override
        public void serialize(Date date, JsonGenerator generator, SerializerProvider serializers) throws IOException {
            if (date != null) {
                generator.writeNumber(millisToSeconds(date.getTime()));
            }
        }
    }

    public static final class Deserializer extends JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            double dateSeconds = parser.getValueAsDouble();
            if (dateSeconds == 0.0) {
                return null;
            } else {
                return new Date((long) secondsToMillis(dateSeconds));
            }
        }
    }

    private static double millisToSeconds(double millis) {
        return millis / 1000.0;
    }

    private static double secondsToMillis(double seconds) {
        return seconds * 1000.0;
    }

    public DateModule() {
        super(PackageVersion.VERSION);
        addSerializer(Date.class, new Serializer());
        addDeserializer(Date.class, new Deserializer());
    }
}
