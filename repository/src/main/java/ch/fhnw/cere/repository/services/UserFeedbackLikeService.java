package ch.fhnw.cere.repository.services;


import ch.fhnw.cere.repository.models.FeedbackSettings;
import ch.fhnw.cere.repository.models.UserFBLike;

import java.util.List;


public interface UserFeedbackLikeService {
    public List<UserFBLike> findAll();
    public UserFBLike save(UserFBLike userFBLike);
    public UserFBLike find(long id);
    public void delete(long id);
    List<UserFBLike> findByFeedbackId(long feedbackId);
    List<UserFBLike> findByEnduserId(long userId);
}



