package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.FeedbackStatus;

import java.util.List;

public interface FeedbackStatusService {
    public List<FeedbackStatus> findAll();

    public FeedbackStatus save(FeedbackStatus feedbackStatus);

    public FeedbackStatus find(long id);

    public FeedbackStatus findByStatus(String status);

    public void delete(long id);
}
