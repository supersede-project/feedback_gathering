package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.FeedbackReport;
import ch.fhnw.cere.repository.repositories.FeedbackReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FeedbackReportServiceImpl implements FeedbackReportService {

    @Autowired
    private FeedbackReportRepository feedbackReportRepository;

    @Override
    public List<FeedbackReport> findAll() {
        return feedbackReportRepository.findAll();
    }

    @Override
    public FeedbackReport save(FeedbackReport feedbackReport) {
        return feedbackReportRepository.save(feedbackReport);
    }

    @Override
    public FeedbackReport find(long id) {
        return feedbackReportRepository.findOne(id);
    }

    @Override
    public void delete(long id) {
        feedbackReportRepository.delete(id);
    }

    @Override
    public List<FeedbackReport> findByApplicationId(long applicationId) {
        return feedbackReportRepository.findByApplicationIdQuery(applicationId);
    }
}
