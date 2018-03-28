package ch.fhnw.cere.repository.integration;


import ch.fhnw.cere.repository.models.Feedback;
import ch.fhnw.cere.repository.models.Setting;
import ch.fhnw.cere.repository.services.SettingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.supersede.integration.api.analysis.proxies.DataProviderProxy;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


/**
 * Forwards the feedback data to WP2 if there is a kafka topic ID set in the setting table.
 */
@Component
public class DataProviderIntegrator {
    private DataProviderProxy proxy;

    @Autowired
    private SettingService settingService;

    private DataProviderProxy dataProviderProxy;

    public DataProviderIntegrator() {
        dataProviderProxy = new DataProviderProxy();
    }

    @Async
    public void ingestJsonData(Feedback feedback) {
        Setting setting = settingService.findByApplicationId(feedback.getApplicationId());
        if(setting == null) {
            return;
        }
        String topic = setting.getKafkaTopicId();

        if(topic != null && !topic.equals("")) {
            String json = toJson(feedback);
            dataProviderProxy.ingestData(json, topic);
        }
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
