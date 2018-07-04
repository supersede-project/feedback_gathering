package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.FeedbackResponse;

import java.util.List;

public interface FeedbackResponseService {
    List<FeedbackResponse> findAll();
    FeedbackResponse save(FeedbackResponse feedback);
    FeedbackResponse find(long id);
    void delete(long id);
    List<FeedbackResponse> findByFeedbackId(long applicationId);
    long countByUserId(long userId);
}
