package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.CommentFeedback;
import ch.fhnw.cere.repository.models.EndUser;
import ch.fhnw.cere.repository.repositories.CommentFeedbackRepository;
import ch.fhnw.cere.repository.repositories.EndUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class EndUserServiceImpl implements EndUserService {

    @Autowired
    private EndUserRepository endUserRepository;

    @Override
    public List<EndUser> findAll() {
        return endUserRepository.findAll();
    }

    @Override
    public EndUser save(EndUser endUser) {
        return endUserRepository.save(endUser);
    }

    @Override
    public EndUser find(long id) {
        return endUserRepository.findOne(id);
    }

    @Override
    public void delete(long id) {
        endUserRepository.delete(id);
    }

    @Override
    public EndUser findByUsername(String username) {
        return endUserRepository.findByUsername(username);
    }

    @Override
    public List<EndUser> findByApplicationId(long applicationId) {
        return endUserRepository.findByApplicationId(applicationId);
    }
}