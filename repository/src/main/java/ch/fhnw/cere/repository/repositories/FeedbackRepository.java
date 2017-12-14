package ch.fhnw.cere.repository.repositories;


import ch.fhnw.cere.repository.models.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import scala.collection.immutable.Page;

import java.awt.print.Pageable;
import java.util.List;


@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByApplicationId(@Param("applicationId") long applicationId);
    List<Feedback> findByUserIdentification(@Param("userIdentification") String userIdentification);
//    Page<Feedback> firstFeedback(Pageable limit);
    List<Feedback> findAllByOrderByIdDesc();
}
