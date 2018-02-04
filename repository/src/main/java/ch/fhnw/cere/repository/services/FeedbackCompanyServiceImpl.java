package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.FeedbackChatInformation;
import ch.fhnw.cere.repository.models.FeedbackCompany;
import ch.fhnw.cere.repository.repositories.FeedbackChatInformationRepository;
import ch.fhnw.cere.repository.repositories.FeedbackCompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class FeedbackCompanyServiceImpl implements FeedbackCompanyService {

    @Autowired
    private FeedbackCompanyRepository feedbackCompanyRepository;

    @Override
    public List<FeedbackCompany> findAll() {
        return feedbackCompanyRepository.findAll();
    }

    @Override
    public FeedbackCompany save(FeedbackCompany feedbackCompany) {
        return feedbackCompanyRepository.save(feedbackCompany);
    }

    @Override
    public FeedbackCompany find(long id) {
        return feedbackCompanyRepository.findOne(id);
    }

    @Override
    public void delete(long id) {
        feedbackCompanyRepository.delete(id);
    }

    @Override
    public List<FeedbackCompany> findByPromote(boolean promote) {
        return feedbackCompanyRepository.findByPromote(promote);
    }
}