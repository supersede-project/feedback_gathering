package ch.fhnw.cere.repository.controllers;

import ch.fhnw.cere.repository.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.repository.models.CommentFeedback;
import ch.fhnw.cere.repository.models.CommentViewed;
import ch.fhnw.cere.repository.models.FeedbackViewed;
import ch.fhnw.cere.repository.services.CommentFeedbackService;
import ch.fhnw.cere.repository.services.CommentViewedService;
import ch.fhnw.cere.repository.services.EndUserService;
import ch.fhnw.cere.repository.services.FeedbackService;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Aydinli on 23.01.2018.
 */
@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/{language}/applications/{applicationId}/feedbacks")
public class CommentViewedController {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(FeedbackController.class);

    @Autowired
    private CommentViewedService commentViewedService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private CommentFeedbackService commentFeedbackService;

    @Autowired
    private EndUserService endUserService;

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/comment_views")
    public List<CommentViewed> getAllCommentViews(@PathVariable long applicationId) {
        return commentViewedService.findAll();
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/comment_views" +
            "/comment/{commentId}")
    public List<CommentViewed> getCommentViewsForFeedback(@PathVariable long applicationId,
                                                @PathVariable long commentId) {
        return commentViewedService.findByCommentId(commentId);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/comment_views/user/{userId}")
    public List<CommentViewed> getCommentViewsForUser(@PathVariable long applicationId,
                                            @PathVariable long userId) {
        return commentViewedService.findByEnduserId(userId);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/comment_views" +
            "/both/{userId}/{commentId}")
    public CommentViewed getCommentViewsForUserAndFeedback(@PathVariable long applicationId,
                                                 @PathVariable long userId,
                                                 @PathVariable long commentId) {
        return commentViewedService.findByEnduserIdAndCommentId(userId,commentId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "/comment_views")
    public CommentViewed createView(HttpEntity<String> viewJSON) {
        LOGGER.info("Create View: " + viewJSON.getBody());
        if(viewJSON.getBody() != null){
            JSONObject object = new JSONObject(viewJSON.getBody());
            long commentId = object.getLong("comment_id");
            long userId = object.getLong("user_id");

            CommentViewed commentViewed = new CommentViewed();
            commentViewed.setComment(commentFeedbackService.find(commentId));
            commentViewed.setEnduser(endUserService.find(userId));

            return commentViewedService.save(commentViewed);
        }
        return null;
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.DELETE, value = "/comment_views/{commentViewId}")
    public void deleteView(@PathVariable long applicationId, @PathVariable long commentViewId) {
        commentViewedService.delete(commentViewId);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/comment_views/{commentViewId}")
    public CommentViewed getCommentView(@PathVariable long applicationId,
                              @PathVariable long commentViewId) {
        CommentViewed commentView = commentViewedService.find(commentViewId);
        if(commentView == null){
            throw new NotFoundException();
        }
        return commentView;
    }
}
