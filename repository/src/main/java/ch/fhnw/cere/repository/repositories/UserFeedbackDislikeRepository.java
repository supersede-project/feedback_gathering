package ch.fhnw.cere.repository.repositories;


import ch.fhnw.cere.repository.models.FeedbackSettings;
import ch.fhnw.cere.repository.models.UserFBDislike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserFeedbackDislikeRepository extends JpaRepository<UserFBDislike, Long> {
    List<UserFBDislike> findByFeedbackId(@Param("feedbackId") long feedbackId);
    List<UserFBDislike> findByEnduserId(@Param("userId") long userId);
}
