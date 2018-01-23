package ch.fhnw.cere.repository.services;

import ch.fhnw.cere.repository.models.CommentViewed;
import ch.fhnw.cere.repository.models.FeedbackViewed;
import ch.fhnw.cere.repository.repositories.CommentViewedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Aydinli on 23.01.2018.
 */
@Service
public class CommentViewedServiceImpl implements CommentViewedService{
    @Autowired
    private CommentViewedRepository commentViewedRepository;

    @Override
    public List<CommentViewed> findAll() {
        return commentViewedRepository.findAll();
    }

    @Override
    public CommentViewed save(CommentViewed commentViewed) {
        return commentViewedRepository.save(commentViewed);
    }

    @Override
    public CommentViewed find(long id) {
        return commentViewedRepository.findOne(id);
    }

    @Override
    public void delete(long id) {
        commentViewedRepository.delete(id);
    }

    @Override
    public List<CommentViewed> findByCommentId(long commentId) {
        return commentViewedRepository.findByCommentId(commentId);
    }

    @Override
    public List<CommentViewed> findByEnduserId(long userId) {
        return commentViewedRepository.findByEnduserId(userId);
    }

    @Override
    public CommentViewed findByEnduserIdAndCommentId(long userId, long commentId) {
        return commentViewedRepository.findByEnduserIdAndCommentId(userId,commentId);
    }
}
