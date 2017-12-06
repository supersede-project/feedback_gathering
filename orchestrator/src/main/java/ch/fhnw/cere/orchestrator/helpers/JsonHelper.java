package ch.fhnw.cere.orchestrator.helpers;


import org.springframework.stereotype.Component;


@Component
public class JsonHelper {

    public String stripOffIds(String payload) {
        payload = payload.replaceAll("\"id\":.*,?", "");
        payload = payload.replaceAll("\\\"id\\\":\\d*,?", "");
        payload = payload.replaceAll("\"configurationsId\":.*,?", "");
        payload = payload.replaceAll("\\\"configurationsId\\\":\\d*,?", "");
        return payload;
    }

    public String stripOffTimestamps(String payload) {
        payload = payload.replaceAll("\"createdAt\":.*,?", "");
        payload = payload.replaceAll("\\\"createdAt\\\":\\d*,?", "");
        payload = payload.replaceAll("\"updatedAt\":.*,?", "");
        payload = payload.replaceAll("\\\"updatedAt\\\":\\d*,?", "");
        return payload;
    }

    public String stripOffInvalidCommas(String payload) {
        payload = payload.replaceAll(",\\n\\s*}", "\n}");
        return payload;
    }

    public String stripOffEmptyLines(String payload) {
        payload = payload.replaceAll("\\n\\s*\\n", "\n");
        return payload;
    }
}
