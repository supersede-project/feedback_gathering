package ch.fhnw.cere.repository.services;


import ch.fhnw.cere.repository.models.UserFBDislike;
import ch.fhnw.cere.repository.models.UserFBLike;

import java.util.List;


public interface UserFeedbackDislikeService {
    public List<UserFBDislike> findAll();
    public UserFBDislike save(UserFBDislike userFBDislike);
    public UserFBDislike find(long id);
    public void delete(long id);
    List<UserFBDislike> findByFeedbackId(long feedbackId);
    List<UserFBDislike> findByUserId(long userId);
}



