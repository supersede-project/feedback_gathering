package ch.fhnw.cere.repository.repositories;


import ch.fhnw.cere.repository.models.AttachmentFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AttachmentFeedbackRepository extends JpaRepository<AttachmentFeedback, Long> {
    List<AttachmentFeedback> findByFeedbackId(@Param("feedbackId") long feedbackId);
}
