package ch.fhnw.cere.orchestrator.serialization;


import ch.fhnw.cere.orchestrator.models.Parameter;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;


public class ParameterDefaultSerializer extends JsonSerializer<Parameter> {

    private final JsonSerializer<Object> defaultSerializer;

    public ParameterDefaultSerializer(JsonSerializer<Object> defaultSerializer) {
        this.defaultSerializer = checkNotNull(defaultSerializer);
    }

    @Override
    public void serialize(Parameter parameter, JsonGenerator jgen,
                          SerializerProvider provider) throws IOException,
            JsonProcessingException {
        defaultSerializer.serialize(parameter, jgen, provider);
    }
}
