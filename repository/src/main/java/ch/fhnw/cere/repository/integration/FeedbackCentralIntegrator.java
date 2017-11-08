package ch.fhnw.cere.repository.integration;


import ch.fhnw.cere.repository.models.Feedback;
import ch.fhnw.cere.repository.services.FeedbackCentralSender;
import ch.fhnw.cere.repository.services.SettingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.supersede.integration.api.analysis.proxies.DataProviderProxy;
import eu.supersede.integration.api.analysis.proxies.KafkaClient;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
public class FeedbackCentralIntegrator {

    @Autowired
    private FeedbackCentralSender feedbackCentralSender;

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(FeedbackCentralIntegrator.class);

    public FeedbackCentralIntegrator() {
    }

    @Async
    public void ingestJsonData(Feedback feedback) {
        // TODO change to value from conf
        String rawFeedbackTopic = "raw-feedback";
        String json = toJson(feedback);

        feedbackCentralSender.send(rawFeedbackTopic, json);

        LOGGER.info("Feedback sent to localhost 9092 to topic raw-feedback ='{}'", json);
    }

    private String toJson(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString= null;
        try {
            jsonString = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}
