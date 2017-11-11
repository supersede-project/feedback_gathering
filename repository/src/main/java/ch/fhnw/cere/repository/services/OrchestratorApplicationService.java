package ch.fhnw.cere.repository.services;


import ch.fhnw.cere.repository.models.orchestrator.Application;

import java.io.IOException;


public interface OrchestratorApplicationService {
    public Application loadApplication(String language, long applicationId) throws IOException;
}
