package ch.fhnw.cere.repository.services;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import scala.util.parsing.json.JSON;


@Service
public class OrchestratorApplicationServiceImpl implements OrchestratorApplicationService {

    @Value("${supersede.orchestrator_host}")
    private String orchestratorHost;

    public JSONObject loadApplication(String language, long applicationId) {
        RestTemplate restTemplate = new RestTemplate();
        String url = orchestratorHost + language + "/applications/" + applicationId;
        String applicationJsonString = restTemplate.getForObject(url, String.class);
        JSONObject applicationJson = new JSONObject(applicationJsonString);
        return applicationJson;
    }
}
