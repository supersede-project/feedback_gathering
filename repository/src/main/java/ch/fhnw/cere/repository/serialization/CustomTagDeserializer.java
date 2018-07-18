package ch.fhnw.cere.repository.serialization;

import ch.fhnw.cere.repository.models.FeedbackTag;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CustomTagDeserializer extends StdDeserializer<List<FeedbackTag>> {

    public CustomTagDeserializer() {
        this(null);
    }

    public CustomTagDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public List<FeedbackTag> deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException, JsonProcessingException {
        if (jsonParser.getCurrentToken() == JsonToken.START_ARRAY) {
            List<FeedbackTag> feedbackTagList = new ArrayList<>();
            while(jsonParser.nextToken() != JsonToken.END_ARRAY) {
                feedbackTagList.add(new FeedbackTag(null, jsonParser.getValueAsString()));
            }
            return feedbackTagList;
        }

        throw new JsonParseException(jsonParser, "Parsing of TagArray Failed");
    }


}