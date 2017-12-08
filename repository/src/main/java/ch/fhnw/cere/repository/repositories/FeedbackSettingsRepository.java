package ch.fhnw.cere.repository.repositories;


import ch.fhnw.cere.repository.models.Feedback;
import ch.fhnw.cere.repository.models.FeedbackCompany;
import ch.fhnw.cere.repository.models.FeedbackSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FeedbackSettingsRepository extends JpaRepository<FeedbackSettings, Long> {
    FeedbackSettings findByFeedbackId(@Param("feedbackId") long feedbackId);
    List<FeedbackSettings> findByUserId(@Param("userId") long userId);
}
