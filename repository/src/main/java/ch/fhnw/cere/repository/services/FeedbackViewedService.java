package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.FeedbackViewed;

import java.util.List;

/**
 * Created by Aydinli on 23.01.2018.
 */
public interface FeedbackViewedService {
    public List<FeedbackViewed> findAll();
    public FeedbackViewed save(FeedbackViewed feedbackViewed);
    public FeedbackViewed find(long id);
    public void delete(long id);
    List<FeedbackViewed> findByFeedbackId(long feedbackId);
    List<FeedbackViewed> findByEnduserId(long userId);
    FeedbackViewed findByEnduserIdAndFeedbackId(long userId, long feedbackId);
}
