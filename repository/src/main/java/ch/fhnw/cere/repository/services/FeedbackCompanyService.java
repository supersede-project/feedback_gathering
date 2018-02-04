package ch.fhnw.cere.repository.services;


import ch.fhnw.cere.repository.models.EndUser;
import ch.fhnw.cere.repository.models.FeedbackCompany;

import java.util.List;


public interface FeedbackCompanyService {
    public List<FeedbackCompany> findAll();
    public FeedbackCompany save(FeedbackCompany feedbackCompany);
    public FeedbackCompany find(long id);
    public void delete(long id);
    List<FeedbackCompany> findByPromote(boolean promote);
}



