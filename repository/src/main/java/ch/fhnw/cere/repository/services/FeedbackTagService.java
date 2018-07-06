package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.FeedbackTag;

import java.util.List;

public interface FeedbackTagService {
    List<FeedbackTag> findAll();

    FeedbackTag save(FeedbackTag feedbackStatus);

    FeedbackTag find(long id);

    List<String> findDistinctTag(long applicationId);

    void delete(long id);
}
