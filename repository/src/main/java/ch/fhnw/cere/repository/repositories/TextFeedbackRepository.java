
package ch.fhnw.cere.repository.repositories;


import ch.fhnw.cere.repository.models.TextFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TextFeedbackRepository extends JpaRepository<TextFeedback, Long> {
    List<TextFeedback> findByFeedbackId(@Param("feedbackId") long feedbackId);
}
