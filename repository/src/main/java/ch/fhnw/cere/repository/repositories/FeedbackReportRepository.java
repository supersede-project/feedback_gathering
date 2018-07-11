package ch.fhnw.cere.repository.repositories;

import ch.fhnw.cere.repository.models.FeedbackReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackReportRepository extends JpaRepository<FeedbackReport, Long> {
    @Query( "select fr from FeedbackReport fr join fr.feedback where fr.feedback.applicationId = :applicationId" )
    List<FeedbackReport> findByApplicationIdQuery(@Param("applicationId") long applicationId);
}
