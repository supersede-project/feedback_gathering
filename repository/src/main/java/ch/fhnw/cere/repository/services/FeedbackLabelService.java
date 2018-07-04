package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.FeedbackLabel;

import java.util.List;

public interface FeedbackLabelService {
    List<FeedbackLabel> findAll();

    FeedbackLabel save(FeedbackLabel feedbackStatus);

    FeedbackLabel find(long id);

    List<String> findDistinctLabel();

    void delete(long id);
}
