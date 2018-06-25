package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.RatingFeedback;
import ch.fhnw.cere.repository.models.reporting.ReportResponse;
import ch.fhnw.cere.repository.models.reporting.ReportResponseFactory;
import ch.fhnw.cere.repository.repositories.RatingFeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    private RatingFeedbackRepository ratingFeedbackRepository;

    @Autowired
    private ReportResponseFactory reportResponseFactory;

    @Override
    public ReportResponse findForApplicationId(long id) {
        List<RatingFeedback> ratingFeedbacks = ratingFeedbackRepository.findByApplicationId(id);
        return reportResponseFactory.createFromRatingFeedbacks(ratingFeedbacks);
    }
}