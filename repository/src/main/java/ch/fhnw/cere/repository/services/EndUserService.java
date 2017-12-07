package ch.fhnw.cere.repository.services;


import ch.fhnw.cere.repository.models.CommentFeedback;
import ch.fhnw.cere.repository.models.EndUser;

import java.util.List;


public interface EndUserService {
    public List<EndUser> findAll();
    public EndUser save(EndUser endUser);
    public EndUser find(long id);
    public void delete(long id);
    EndUser findByUsername(String username);
    List<EndUser> findByApplicationId(long applicationId);
}



