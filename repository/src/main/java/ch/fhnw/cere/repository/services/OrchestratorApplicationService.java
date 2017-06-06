package ch.fhnw.cere.repository.services;

import org.json.JSONObject;

public interface OrchestratorApplicationService {
    public JSONObject loadApplication(String language, long applicationId);
}
