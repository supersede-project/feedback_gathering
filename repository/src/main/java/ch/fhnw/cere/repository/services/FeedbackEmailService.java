package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.Feedback;
import freemarker.template.TemplateException;

import java.io.IOException;

public interface FeedbackEmailService {
    public void sendFeedbackNotification(Feedback feedback);
    public void sendMail(Feedback feedback, String recipients) throws IOException, TemplateException;
    public void setOrchestratorService(OrchestratorApplicationService orchestratorApplicationService);
}
