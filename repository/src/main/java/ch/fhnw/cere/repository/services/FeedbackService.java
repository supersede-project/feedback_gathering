package ch.fhnw.cere.repository.services;


import ch.fhnw.cere.repository.models.Feedback;

import java.util.List;


public interface FeedbackService {
    List<Feedback> findAll();
    Feedback save(Feedback feedback);
    Feedback find(long id);
    void delete(long id);
    List<Feedback> findByApplicationId(long applicationId);
    List<Feedback> findByApplicationIdAndIsPublic(long applicationId, boolean isPublic);
    List<Feedback> findByUserIdentification(String userIdentification);
    List<Feedback> findAllByFeedbackIdIn(long applicationId, List<Long> idList);
    List<Feedback> findByUserIdentificationOrIsPublicAndApplicationId(long applicationId, String userIdentification, boolean isPublic);
    List<Feedback> findByIsReported(long applicationId);
    int countByUserIdentification(String userIdentification);
}



