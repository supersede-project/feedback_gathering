package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.FeedbackResponse;
import ch.fhnw.cere.repository.repositories.FeedbackResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FeedbackResponseServiceImpl implements FeedbackResponseService {

    @Autowired
    private FeedbackResponseRepository feedbackResponseRepository;

    public List<FeedbackResponse> findAll(){
        return feedbackResponseRepository.findAll();
    }

    public FeedbackResponse save(FeedbackResponse feedbackResponse){
        return feedbackResponseRepository.save(feedbackResponse);
    }

    public FeedbackResponse find(long id){
        return feedbackResponseRepository.findOne(id);
    }

    public void delete(long id){
        feedbackResponseRepository.delete(id);
    }

    @Override
    public List<FeedbackResponse> findByFeedbackId(long feedbackId) {
        return feedbackResponseRepository.findByFeedbackId(feedbackId);
    }

    @Override
    public long countByUserId(long userId) {
        return feedbackResponseRepository.countByUserId(userId);
    }

}
