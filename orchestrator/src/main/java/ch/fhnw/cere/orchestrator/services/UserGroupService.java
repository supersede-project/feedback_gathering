package ch.fhnw.cere.orchestrator.services;


import ch.fhnw.cere.orchestrator.models.UserGroup;

import java.util.List;


public interface UserGroupService {
    public List<UserGroup> findAll();
    public UserGroup save(UserGroup userGroup);
    public UserGroup find(long id);
    public void delete(long id);
    public List<UserGroup> findByApplicationId(long id);
    public List<UserGroup> findByUserIdentification(long applicationId, String userIdentification);
}



