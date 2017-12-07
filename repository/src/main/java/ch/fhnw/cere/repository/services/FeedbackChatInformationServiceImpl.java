package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.EndUser;
import ch.fhnw.cere.repository.models.FeedbackChatInformation;
import ch.fhnw.cere.repository.repositories.EndUserRepository;
import ch.fhnw.cere.repository.repositories.FeedbackChatInformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class FeedbackChatInformationServiceImpl implements FeedbackChatInformationService {

    @Autowired
    private FeedbackChatInformationRepository feedbackChatInformationRepository;

    @Override
    public List<FeedbackChatInformation> findAll() {
        return feedbackChatInformationRepository.findAll();
    }

    @Override
    public FeedbackChatInformation save(FeedbackChatInformation feedbackChatInformation) {
        return feedbackChatInformationRepository.save(feedbackChatInformation);
    }

    @Override
    public FeedbackChatInformation find(long id) {
        return feedbackChatInformationRepository.findOne(id);
    }

    @Override
    public void delete(long id) {
        feedbackChatInformationRepository.delete(id);
    }

    @Override
    public FeedbackChatInformation findByUserId(long userId) {
        return feedbackChatInformationRepository.findByUserId(userId);
    }

    @Override
    public List<FeedbackChatInformation> findByFeedbackId(long feedbackId) {
        return feedbackChatInformationRepository.findByFeedbackId(feedbackId);
    }
}