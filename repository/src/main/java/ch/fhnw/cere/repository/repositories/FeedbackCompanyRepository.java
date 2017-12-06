package ch.fhnw.cere.repository.repositories;


import ch.fhnw.cere.repository.models.FeedbackChatInformation;
import ch.fhnw.cere.repository.models.FeedbackCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FeedbackCompanyRepository extends JpaRepository<FeedbackCompany, Long> {
    List<FeedbackCompany> findByFeedbackId(@Param("feedbackId") long feedbackId);
}
