package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.ApiUser;
import ch.fhnw.cere.repository.models.CommentFeedback;
import ch.fhnw.cere.repository.repositories.ApiUserRepository;
import ch.fhnw.cere.repository.repositories.CommentFeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class CommentFeedbackServiceImpl implements CommentFeedbackService {

    @Autowired
    private CommentFeedbackRepository commentFeedbackRepository;

    @Override
    public List<CommentFeedback> findAll() {
        return commentFeedbackRepository.findAll();
    }

    @Override
    public CommentFeedback save(CommentFeedback commentFeedback) {
        return commentFeedbackRepository.save(commentFeedback);
    }

    @Override
    public CommentFeedback find(long id) {
        return commentFeedbackRepository.findOne(id);
    }

    @Override
    public void delete(long id) {
        commentFeedbackRepository.delete(id);
    }

    @Override
    public List<CommentFeedback> findByUserIdentification(long userId) {
        return commentFeedbackRepository.findByUserIdentification(userId);
    }

    @Override
    public List<CommentFeedback> findByApplicationId(long applicationId) {
        return commentFeedbackRepository.findByApplicationId(applicationId);
    }
}