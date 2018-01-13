package ch.fhnw.cere.repository.services;


import ch.fhnw.cere.repository.models.EndUser;
import ch.fhnw.cere.repository.models.FeedbackChatInformation;

import java.util.List;


public interface FeedbackChatInformationService {
    public List<FeedbackChatInformation> findAll();
    public FeedbackChatInformation save(FeedbackChatInformation feedbackChatInformation);
    public FeedbackChatInformation find(long id);
    public void delete(long id);
    List<FeedbackChatInformation> findByUserId(long userId);
    List<FeedbackChatInformation> findByFeedbackId(long feedbackId);
}



