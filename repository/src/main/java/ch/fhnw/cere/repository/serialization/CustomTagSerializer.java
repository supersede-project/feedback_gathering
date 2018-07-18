package ch.fhnw.cere.repository.serialization;

import ch.fhnw.cere.repository.models.FeedbackTag;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomTagSerializer extends StdSerializer<List<FeedbackTag>> {

    public CustomTagSerializer() {
        this(null);
    }

    public CustomTagSerializer(Class<List<FeedbackTag>> t) {
        super(t);
    }

    @Override
    public void serialize(List<FeedbackTag> feedbackTagList, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        List<String> feedbackTagStringList = new ArrayList<>();

        for (FeedbackTag feedbackTag: feedbackTagList) {
            feedbackTagStringList.add(feedbackTag.getTag());
        }
        jgen.writeObject(feedbackTagStringList);
    }

}
