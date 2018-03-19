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
    List<Feedback> findByUserIdentification(@Param("userIdentification") long userIdentification);
//    Page<Feedback> firstFeedback(Pageable limit);
    List<Feedback> findAllByOrderByIdDesc();
    List<Feedback> findByPublishedAndVisibility(@Param("published") boolean published,
                                                @Param("visibility") boolean visibility);
    List<Feedback> findByPublished(@Param("published") boolean published);
    List<Feedback> findByVisibility(@Param("visibility") boolean visibility);
    List<Feedback> findByBlocked(@Param("isBlocked") boolean is_blocked);
//    List<Feedback> findByBlocked(@Param("is_blocked") boolean is_blocked);
}
