package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.FeedbackTag;
import ch.fhnw.cere.repository.repositories.FeedbackTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FeedbackTagServiceImpl implements FeedbackTagService {

    @Autowired
    private FeedbackTagRepository feedbackTagRepository;

    @Override
    public List<FeedbackTag> findAll() {
        return feedbackTagRepository.findAll();
    }

    @Override
    public FeedbackTag save(FeedbackTag feedbackTag) {
        return feedbackTagRepository.save(feedbackTag);
    }

    @Override
    public FeedbackTag find(long id) {
        return feedbackTagRepository.findOne(id);
    }

    @Override
    public List<String> findDistinctTag(long applicationId) {
        return feedbackTagRepository.findDistinctTag(applicationId);
    }

    @Override
    public void delete(long id) {
        feedbackTagRepository.delete(id);
    }
}
