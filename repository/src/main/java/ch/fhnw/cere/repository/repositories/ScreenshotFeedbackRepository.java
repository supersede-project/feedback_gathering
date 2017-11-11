
package ch.fhnw.cere.repository.repositories;


import ch.fhnw.cere.repository.models.ScreenshotFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ScreenshotFeedbackRepository extends JpaRepository<ScreenshotFeedback, Long> {
    List<ScreenshotFeedback> findByFeedbackId(@Param("feedbackId") long feedbackId);
}
