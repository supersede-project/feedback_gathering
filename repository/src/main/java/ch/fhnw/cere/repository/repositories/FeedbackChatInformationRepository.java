package ch.fhnw.cere.repository.repositories;


import ch.fhnw.cere.repository.models.EndUser;
import ch.fhnw.cere.repository.models.FeedbackChatInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FeedbackChatInformationRepository extends JpaRepository<FeedbackChatInformation, Long> {
    List<FeedbackChatInformation> findByUserId(@Param("userId") long userId);
    List<FeedbackChatInformation> findByFeedbackId(@Param("feedbackId") long feedbackId);
}
