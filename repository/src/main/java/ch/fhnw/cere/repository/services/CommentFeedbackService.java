package ch.fhnw.cere.repository.services;


import ch.fhnw.cere.repository.models.ApiUser;
import ch.fhnw.cere.repository.models.CommentFeedback;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CommentFeedbackService {
    public List<CommentFeedback> findAll();
    public CommentFeedback save(CommentFeedback commentFeedback);
    public CommentFeedback find(long id);
    public void delete(long id);
    List<CommentFeedback> findByUserIdentification(long userId);
    List<CommentFeedback> findByApplicationId(long applicationId);
}



