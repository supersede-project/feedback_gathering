package ch.fhnw.cere.repository.services;


import ch.fhnw.cere.repository.models.Feedback;

import java.util.List;


public interface FeedbackService {
    public List<Feedback> findAll();
    public Feedback save(Feedback feedback);
    public Feedback find(long id);
    public void delete(long id);
    List<Feedback> findByApplicationId(long applicationId);
    List<Feedback> findByUserIdentification(String userIdentification);
}



