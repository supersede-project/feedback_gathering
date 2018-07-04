package ch.fhnw.cere.repository.repositories;

import ch.fhnw.cere.repository.models.FeedbackResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackResponseRepository  extends JpaRepository<FeedbackResponse, Long> {
    List<FeedbackResponse> findByFeedbackId(@Param("feedbackId") long feedbackId);
    long countByUserId(@Param("user") long user);
}
