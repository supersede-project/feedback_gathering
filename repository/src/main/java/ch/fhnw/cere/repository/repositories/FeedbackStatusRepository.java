package ch.fhnw.cere.repository.repositories;

import ch.fhnw.cere.repository.models.FeedbackStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackStatusRepository extends JpaRepository<FeedbackStatus, Long> {
    List<FeedbackStatus> findByStatusType(@Param("statusType") String statusType);
}
