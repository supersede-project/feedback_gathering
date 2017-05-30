package ch.fhnw.cere.repository.repositories;


import ch.fhnw.cere.repository.models.AudioFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AudioFeedbackRepository extends JpaRepository<AudioFeedback, Long> {
    List<AudioFeedback> findByFeedbackId(@Param("feedbackId") long feedbackId);
}
