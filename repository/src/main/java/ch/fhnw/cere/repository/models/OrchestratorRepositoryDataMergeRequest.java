package ch.fhnw.cere.repository.models;

import ch.fhnw.cere.repository.models.orchestrator.Application;

import java.util.List;

public class OrchestratorRepositoryDataMergeRequest {

    private Application application;
    private List<Feedback> feedback;

    public OrchestratorRepositoryDataMergeRequest() {
    }

    public OrchestratorRepositoryDataMergeRequest(Application application, List<Feedback> feedback) {
        this.application = application;
        this.feedback = feedback;
    }

    @Override
    public String toString() {
        return "OrchestratorRepositoryDataMergeRequest{" +
                "application=" + application +
                ", feedback=" + feedback +
                '}';
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public List<Feedback> getFeedback() {
        return feedback;
    }

    public void setFeedback(List<Feedback> feedback) {
        this.feedback = feedback;
    }
}
