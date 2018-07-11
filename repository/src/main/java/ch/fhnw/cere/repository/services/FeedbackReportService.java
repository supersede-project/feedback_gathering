package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.FeedbackReport;

import java.util.List;

public interface FeedbackReportService {
    List<FeedbackReport> findAll();
    FeedbackReport save(FeedbackReport feedbackReport);
    FeedbackReport find(long id);
    void delete(long id);
    List<FeedbackReport> findByApplicationId(long applicationId);
}
