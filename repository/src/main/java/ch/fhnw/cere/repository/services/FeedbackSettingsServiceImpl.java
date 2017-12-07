package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.Feedback;
import ch.fhnw.cere.repository.models.FeedbackSettings;
import ch.fhnw.cere.repository.repositories.FeedbackRepository;
import ch.fhnw.cere.repository.repositories.FeedbackSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class FeedbackSettingsServiceImpl implements FeedbackSettingsService {

    @Autowired
    private FeedbackSettingsRepository feedbackSettingsRepository;

    @Override
    public List<FeedbackSettings> findAll() {
        return feedbackSettingsRepository.findAll();
    }

    @Override
    public FeedbackSettings save(FeedbackSettings feedbackSettings) {
        return feedbackSettingsRepository.save(feedbackSettings);
    }

    @Override
    public FeedbackSettings find(long id) {
        return feedbackSettingsRepository.findOne(id);
    }

    @Override
    public void delete(long id) {
        feedbackSettingsRepository.delete(id);
    }

    @Override
    public FeedbackSettings findByFeedbackId(long feedbackId) {
        return feedbackSettingsRepository.findByFeedbackId(feedbackId);
    }

    @Override
    public List<FeedbackSettings> findByUserId(String userId) {
        return feedbackSettingsRepository.findByUserId(userId);
    }
}