package ch.fhnw.cere.repository.services;



import ch.fhnw.cere.repository.models.FeedbackSettings;

import java.util.List;


public interface FeedbackSettingsService {
    public List<FeedbackSettings> findAll();
    public FeedbackSettings save(FeedbackSettings feedbackSettings);
    public FeedbackSettings find(long id);
    public void delete(long id);
    FeedbackSettings findByFeedbackId(long feedbackId);
    List<FeedbackSettings> findByUserId(long userId);
}



