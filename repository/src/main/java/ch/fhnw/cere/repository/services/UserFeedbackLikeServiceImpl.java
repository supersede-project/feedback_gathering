package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.FeedbackSettings;
import ch.fhnw.cere.repository.models.UserFBLike;
import ch.fhnw.cere.repository.repositories.FeedbackSettingsRepository;
import ch.fhnw.cere.repository.repositories.UserFeedbackLikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class UserFeedbackLikeServiceImpl implements UserFeedbackLikeService {

    @Autowired
    private UserFeedbackLikeRepository userFeedbackLikeRepository;


    @Override
    public List<UserFBLike> findAll() {
        return userFeedbackLikeRepository.findAll();
    }

    @Override
    public UserFBLike save(UserFBLike userFBLike) {
        return userFeedbackLikeRepository.save(userFBLike);
    }

    @Override
    public UserFBLike find(long id) {
        return userFeedbackLikeRepository.findOne(id);
    }

    @Override
    public void delete(long id) {
        userFeedbackLikeRepository.delete(id);
    }

    @Override
    public List<UserFBLike> findByFeedbackId(long feedbackId) {
        return userFeedbackLikeRepository.findByFeedbackId(feedbackId);
    }

    @Override
    public List<UserFBLike> findByEnduserId(long userId) {
        return userFeedbackLikeRepository.findByEnduserId(userId);
    }

    @Override
    public UserFBLike findByEnduserIdAndFeedbackId(long userId, long feedbackId) {
        return userFeedbackLikeRepository.findByEnduserIdAndFeedbackId(userId,feedbackId);
    }
}