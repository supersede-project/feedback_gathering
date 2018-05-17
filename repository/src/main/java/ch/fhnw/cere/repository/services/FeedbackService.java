package ch.fhnw.cere.repository.services;


import ch.fhnw.cere.repository.models.Feedback;

import java.util.List;


public interface FeedbackService {
    List<Feedback> findAll();
    Feedback save(Feedback feedback);
    Feedback find(long id);
    void delete(long id);
    List<Feedback> findByApplicationId(long applicationId);
    List<Feedback> findByIsPublic(boolean isPublic);
    List<Feedback> findByUserIdentification(String userIdentification);
    long countByUserIdentifictation(String userIdentification);
}



