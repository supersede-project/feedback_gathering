package ch.fhnw.cere.repository.serialization;

import ch.fhnw.cere.repository.models.FeedbackStatus;
import ch.fhnw.cere.repository.models.FeedbackTag;
import ch.fhnw.cere.repository.services.FeedbackStatusService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CustomFeedbackStatusDeserializer extends StdDeserializer<FeedbackStatus> {

    public CustomFeedbackStatusDeserializer() {
        this(null);
    }

    public CustomFeedbackStatusDeserializer(Class<?> vc) {
        super(vc);
    }

    @Autowired
    private FeedbackStatusService feedbackStatusService;

    @Override
    public FeedbackStatus deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException, JsonProcessingException {
        String feedbackStatusString = jsonParser.getText();
        return feedbackStatusService.findByStatus(feedbackStatusString);
    }

}