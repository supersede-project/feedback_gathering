package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.orchestrator.Application;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;


@Service
public class OrchestratorApplicationServiceImpl implements OrchestratorApplicationService {

    @Value("${supersede.orchestrator_host}")
    private String orchestratorHost;

    public Application loadApplication(String language, long applicationId) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        String url = orchestratorHost + language + "/applications/" + applicationId;
        String applicationJsonString = restTemplate.getForObject(url, String.class);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(applicationJsonString, Application.class);
    }
}
