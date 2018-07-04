package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.FeedbackLabel;
import ch.fhnw.cere.repository.repositories.FeedbackLabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FeedbackLabelServiceImpl implements FeedbackLabelService {

    @Autowired
    private FeedbackLabelRepository feedbackLabelRepository;

    @Override
    public List<FeedbackLabel> findAll() {
        return feedbackLabelRepository.findAll();
    }

    @Override
    public FeedbackLabel save(FeedbackLabel feedbackLabel) {
        return feedbackLabelRepository.save(feedbackLabel);
    }

    @Override
    public FeedbackLabel find(long id) {
        return feedbackLabelRepository.findOne(id);
    }

    @Override
    public List<String> findDistinctLabel() {
        return feedbackLabelRepository.findDistinctLabel();
    }

    @Override
    public void delete(long id) {
        feedbackLabelRepository.delete(id);
    }
}
