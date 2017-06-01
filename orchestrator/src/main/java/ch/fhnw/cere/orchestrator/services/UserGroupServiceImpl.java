package ch.fhnw.cere.orchestrator.services;

import ch.fhnw.cere.orchestrator.models.UserGroup;
import ch.fhnw.cere.orchestrator.repositories.UserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class UserGroupServiceImpl implements UserGroupService {

    @Autowired
    private UserGroupRepository userGroupRepository;

    public List<UserGroup> findAll(){
        return userGroupRepository.findAll();
    }

    public UserGroup save(UserGroup userGroup){
        return userGroupRepository.save(userGroup);
    }

    public UserGroup find(long id){
        return userGroupRepository.findOne(id);
    }

    public void delete(long id){
        userGroupRepository.delete(id);
    }

    @Override
    public List<UserGroup> findByApplicationId(long id) {
        return userGroupRepository.findByApplicationId(id);
    }

    @Override
    public List<UserGroup> findByUserIdentification(long applicationId, String userIdentification) {
        return userGroupRepository.findByUserIdentification(applicationId, userIdentification);
    }
}