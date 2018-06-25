
package ch.fhnw.cere.repository.repositories;


import ch.fhnw.cere.repository.models.RatingFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RatingFeedbackRepository extends JpaRepository<RatingFeedback, Long> {
    List<RatingFeedback> findByFeedbackId(@Param("feedbackId") long feedbackId);

    @Query(value = "SELECT r.id, r.feedback_id, r.title, r.rating, r.mechanism_id " +
            "    FROM rating_feedback r, feedback f " +
            "    WHERE r.feedback_id = f.id and f.application_id = ?1", nativeQuery = true)
    List<RatingFeedback> findByApplicationId(long applicationId);
}
