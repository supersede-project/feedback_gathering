package ch.fhnw.cere.repository.repositories;

import ch.fhnw.cere.repository.models.FeedbackLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackLabelRepository  extends JpaRepository<FeedbackLabel, Long> {
    @Query( "select distinct label from FeedbackLabel" )
    List<String> findDistinctLabel();
}
