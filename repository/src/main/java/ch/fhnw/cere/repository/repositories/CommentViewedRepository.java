package ch.fhnw.cere.repository.repositories;

import ch.fhnw.cere.repository.models.CommentViewed;
import ch.fhnw.cere.repository.models.FeedbackViewed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Aydinli on 23.01.2018.
 */
public interface CommentViewedRepository extends JpaRepository<CommentViewed, Long>{
    List<CommentViewed> findByCommentId(@Param("commentId") long commentId);
    List<CommentViewed> findByEnduserId(@Param("userId") long userId);
    CommentViewed findByEnduserIdAndCommentId(@Param("commentId") long commentId,
                                                @Param("userId") long userId);
}
