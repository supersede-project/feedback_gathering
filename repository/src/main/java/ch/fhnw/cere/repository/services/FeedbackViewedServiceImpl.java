package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.FeedbackViewed;
import ch.fhnw.cere.repository.repositories.FeedbackViewedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Aydinli on 23.01.2018.
 */
@Service
public class FeedbackViewedServiceImpl implements FeedbackViewedService{
    @Autowired
    private FeedbackViewedRepository feedbackViewedRepository;

    @Override
    public List<FeedbackViewed> findAll() {
        return feedbackViewedRepository.findAll();
    }

    @Override
    public FeedbackViewed save(FeedbackViewed feedbackViewed) {
        return feedbackViewedRepository.save(feedbackViewed);
    }

    @Override
    public FeedbackViewed find(long id) {
        return feedbackViewedRepository.findOne(id);
    }

    @Override
    public void delete(long id) {
        feedbackViewedRepository.delete(id);
    }

    @Override
    public List<FeedbackViewed> findByFeedbackId(long feedbackId) {
        return feedbackViewedRepository.findByFeedbackId(feedbackId);
    }

    @Override
    public List<FeedbackViewed> findByEnduserId(long userId) {
        return feedbackViewedRepository.findByEnduserId(userId);
    }

    @Override
    public FeedbackViewed findByEnduserIdAndFeedbackId(long userId, long feedbackId) {
        return feedbackViewedRepository.findByEnduserIdAndFeedbackId(userId,feedbackId);
    }
}
