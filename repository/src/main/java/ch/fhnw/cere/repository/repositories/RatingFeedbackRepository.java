
package ch.fhnw.cere.repository.repositories;


import ch.fhnw.cere.repository.models.RatingFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RatingFeedbackRepository extends JpaRepository<RatingFeedback, Long> {
    List<RatingFeedback> findByFeedbackId(@Param("feedbackId") long feedbackId);
}
