package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.CommentViewed;
import ch.fhnw.cere.repository.models.FeedbackViewed;

import javax.xml.stream.events.Comment;
import java.util.List;

/**
 * Created by Aydinli on 23.01.2018.
 */
public interface CommentViewedService {
    public List<CommentViewed> findAll();
    public CommentViewed save(CommentViewed commentViewed);
    public CommentViewed find(long id);
    public void delete(long id);
    List<CommentViewed> findByCommentId(long commentId);
    List<CommentViewed> findByEnduserId(long userId);
    CommentViewed findByEnduserIdAndCommentId(long userId, long commentId);
}
