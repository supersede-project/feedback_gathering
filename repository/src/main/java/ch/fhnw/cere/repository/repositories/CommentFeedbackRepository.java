package ch.fhnw.cere.repository.repositories;


import ch.fhnw.cere.repository.models.CommentFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentFeedbackRepository extends JpaRepository<CommentFeedback, Long> {
    List<CommentFeedback> findByUserIdentification(@Param("userId") long userId);
    List<CommentFeedback> findByApplicationId(@Param("applicationId") long applicationId);
}
