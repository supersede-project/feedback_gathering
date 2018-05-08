package ch.fhnw.cere.repository.repositories;


import ch.fhnw.cere.repository.models.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByApplicationId(@Param("applicationId") long applicationId);
    List<Feedback> findByUserIdentification(@Param("userIdentification") String userIdentification);
    List<Feedback> findByIsPublic(@Param("isPublic") boolean isPublic);
}
