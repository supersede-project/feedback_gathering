package ch.fhnw.cere.repository.repositories;

import ch.fhnw.cere.repository.models.FeedbackTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackTagRepository extends JpaRepository<FeedbackTag, Long> {
    @Query( "select distinct ft.tag from FeedbackTag ft join ft.feedback where ft.feedback.applicationId = :applicationId" )
    List<String> findDistinctTag(@Param("applicationId")long applicationId);
}
