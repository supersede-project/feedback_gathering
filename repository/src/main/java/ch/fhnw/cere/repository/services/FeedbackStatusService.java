package ch.fhnw.cere.repository.services;


import ch.fhnw.cere.repository.models.ApiUser;
import ch.fhnw.cere.repository.models.Feedback;
import ch.fhnw.cere.repository.models.FeedbackStatus;
import ch.fhnw.cere.repository.repositories.ApiUserRepository;
import ch.fhnw.cere.repository.repositories.FeedbackStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public interface FeedbackStatusService{
    public List<FeedbackStatus> findAll();
    public FeedbackStatus save(FeedbackStatus feedback);
    public FeedbackStatus find(long id);
    public void delete(long id);

    FeedbackStatus findByFeedbackId(long feedbackId);
    List<FeedbackStatus> findByStatus(String status);
}