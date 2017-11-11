
package ch.fhnw.cere.repository.repositories;


import ch.fhnw.cere.repository.models.TextAnnotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TextAnnotationRepository extends JpaRepository<TextAnnotation, Long> {
    List<TextAnnotation> findByScreenshotFeedbackId(@Param("screenshotFeedbackId") long screenshotFeedbackId);
}
