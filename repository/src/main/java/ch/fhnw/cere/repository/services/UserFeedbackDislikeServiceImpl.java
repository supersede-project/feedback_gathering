package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.UserFBDislike;
import ch.fhnw.cere.repository.models.UserFBLike;
import ch.fhnw.cere.repository.repositories.UserFeedbackDislikeRepository;
import ch.fhnw.cere.repository.repositories.UserFeedbackLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class UserFeedbackDislikeServiceImpl implements UserFeedbackDislikeService {

    @Autowired
    private UserFeedbackDislikeRepository userFeedbackDislikeRepository;

    @Override
    public List<UserFBDislike> findAll() {
        return userFeedbackDislikeRepository.findAll();
    }

    @Override
    public UserFBDislike save(UserFBDislike userFBDislike) {
        return userFeedbackDislikeRepository.save(userFBDislike);
    }

    @Override
    public UserFBDislike find(long id) {
        return userFeedbackDislikeRepository.findOne(id);
    }

    @Override
    public void delete(long id) {
        userFeedbackDislikeRepository.delete(id);
    }

    @Override
    public List<UserFBDislike> findByFeedbackId(long feedbackId) {
        return userFeedbackDislikeRepository.findByFeedbackId(feedbackId);
    }

    @Override
    public List<UserFBDislike> findByEnduserId(long userId) {
        return userFeedbackDislikeRepository.findByEnduserId(userId);
    }
}