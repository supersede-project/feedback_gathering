package ch.fhnw.cere.repository.serialization;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CustomDateDeserializer extends StdDeserializer<Timestamp> {

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");

    public CustomDateDeserializer() {
        this(null);
    }

    public CustomDateDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Timestamp deserialize(JsonParser jsonparser, DeserializationContext context) throws IOException, JsonProcessingException {
        try {
            String date = jsonparser.getText();
            Date parsedDate = formatter.parse(date);
            return new Timestamp(parsedDate.getTime());
        } catch (Exception e) {
            return new Timestamp(new Date().getTime());
        }
    }
}