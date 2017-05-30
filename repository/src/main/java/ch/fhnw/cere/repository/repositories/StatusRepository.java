
package ch.fhnw.cere.repository.repositories;


import ch.fhnw.cere.repository.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
    List<Status> findByFeedbackId(@Param("feedbackId") long feedbackId);
    List<Status> findByApiUserId(@Param("apiUserId") long apiUserId);
}
