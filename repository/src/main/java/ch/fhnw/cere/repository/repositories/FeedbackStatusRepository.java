package ch.fhnw.cere.repository.repositories;


import ch.fhnw.cere.repository.models.ApiUser;
import ch.fhnw.cere.repository.models.Feedback;
import ch.fhnw.cere.repository.models.FeedbackStatus;
import ch.fhnw.cere.repository.models.TextFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FeedbackStatusRepository extends JpaRepository<FeedbackStatus, Long> {
    FeedbackStatus findByFeedbackId(@Param("feedbackId") long feedbackId);
    List<FeedbackStatus> findByStatus(@Param("status") String status);
}
