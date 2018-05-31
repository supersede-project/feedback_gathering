package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.FeedbackStatus;
import ch.fhnw.cere.repository.repositories.FeedbackStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class FeedbackStatusServiceImpl implements FeedbackStatusService {

    @Autowired
    private FeedbackStatusRepository feedbackStatusRepository;

    @Override
    public List<FeedbackStatus> findAll() {
       return feedbackStatusRepository.findAll();
    }

    @Override
    public FeedbackStatus save(FeedbackStatus feedbackStatus) {
        return feedbackStatusRepository.save(feedbackStatus);
    }

    @Override
    public FeedbackStatus find(long id) {
        return feedbackStatusRepository.findOne(id);
    }

    @Override
    public List<FeedbackStatus> findByStatusType(String statusType) {
        return feedbackStatusRepository.findByStatusType(statusType);
    }

    @Override
    public void delete(long id) {
        feedbackStatusRepository.delete(id);
    }
}
