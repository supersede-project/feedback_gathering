package ch.fhnw.cere.repository.services;


import ch.fhnw.cere.repository.models.Feedback;
import ch.fhnw.cere.repository.models.TextFeedback;
import scala.collection.immutable.Page;

import java.awt.print.Pageable;
import java.util.List;


public interface FeedbackService {
    public List<Feedback> findAll();
    public Feedback save(Feedback feedback);
    public Feedback find(long id);
    public void delete(long id);
    List<Feedback> findByApplicationId(long applicationId);
    List<Feedback> findByUserIdentification(long userIdentification);
//    Page<Feedback> firstFeedback();
    List<Feedback> findAllByOrderByIdDesc();

    List<Feedback> findByPublishedAndVisibility(boolean published, boolean visibility);
    List<Feedback> findByPublished(boolean published);
    List<Feedback> findByVisibility(boolean visibility);
    List<Feedback> findByBlocked(boolean is_blocked);

}



