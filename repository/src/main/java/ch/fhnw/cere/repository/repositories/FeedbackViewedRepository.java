package ch.fhnw.cere.repository.repositories;

import ch.fhnw.cere.repository.models.FeedbackViewed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Aydinli on 23.01.2018.
 */
@Repository
public interface FeedbackViewedRepository extends JpaRepository<FeedbackViewed, Long>{
    List<FeedbackViewed> findByFeedbackId(@Param("feedbackId") long feedbackId);
    List<FeedbackViewed> findByEnduserId(@Param("userId") long userId);
    FeedbackViewed findByEnduserIdAndFeedbackId(@Param("feedbackId") long feedbackId,
                                            @Param("userId") long userId);
}
