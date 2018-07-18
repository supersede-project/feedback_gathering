package ch.fhnw.cere.repository.serialization;

import ch.fhnw.cere.repository.models.FeedbackStatus;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class CustomFeedbackStatusSerializer extends StdSerializer<FeedbackStatus> {

    public CustomFeedbackStatusSerializer() {
        this(null);
    }

    public CustomFeedbackStatusSerializer(Class<FeedbackStatus> t) {
        super(t);
    }

    @Override
    public void serialize(FeedbackStatus feedbackStatus, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeObject(feedbackStatus.getStatus());

    }
}
