package ch.fhnw.cere.repository.controllers;

import ch.fhnw.cere.repository.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.repository.models.CommentFeedback;
import ch.fhnw.cere.repository.services.*;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Aydinli on 23.12.2017.
 */
@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/{language}/applications/{applicationId}/feedbacks")
public class CommentController extends BaseController{
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(FeedbackController.class);

    @Autowired
    private CommentFeedbackService commentFeedbackService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private EndUserService endUserService;

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/comments")
    public List<CommentFeedback> getAllComments(@PathVariable long applicationId) {
        return commentFeedbackService.findAll();
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/comments/feedback/{feedbackId}")
    public List<CommentFeedback> getCommentsForFeedback(@PathVariable long applicationId,
                                                        @PathVariable long feedbackId) {
        return commentFeedbackService.findByFeedbackId(feedbackId);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/comments/user/{userId}")
    public List<CommentFeedback> getCommentsForUser(@PathVariable long applicationId,
                                                        @PathVariable long userId) {
        return commentFeedbackService.findByUserId(userId);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/comments/{commentId}")
    public CommentFeedback getComment(@PathVariable long applicationId,
                                            @PathVariable long commentId) {
        CommentFeedback commentFeedback = commentFeedbackService.find(commentId);
        if(commentFeedback == null){
            throw new NotFoundException();
        }
        return commentFeedback;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "/comments")
    public CommentFeedback createComment(HttpEntity<String> commentFeedbackJSON){
        LOGGER.info("Create Comment: " + commentFeedbackJSON.getBody());
        if(commentFeedbackJSON.getBody() != null){
            LOGGER.info("Create Comment: Body not null");

            CommentFeedback commentFeedback = new CommentFeedback();

            JSONObject object = new JSONObject(commentFeedbackJSON.getBody());
            long feedbackId = object.getLong("feedback_id");
            long userId = object.getLong("user_id");
            if(object.has("fk_parent_comment") && object.get("fk_parent_comment") != null){
                long parentId = object.getLong("fk_parent_comment");
                commentFeedback.setParentComment(commentFeedbackService.find(parentId));
            }
            if(object.has("anonymous") && object.get("anonymous") != null){
                boolean anonymous = object.getBoolean("anonymous");
                commentFeedback.setAnonymous(anonymous);
            } else {
                commentFeedback.setAnonymous(false);
            }
            String commentText = object.getString("commentText");

            if(object.has("bool_is_developer") && object.get("bool_is_developer") != null){
                boolean bool_is_developer = object.getBoolean("bool_is_developer");
                commentFeedback.setBool_is_developer(bool_is_developer);
            } else {
                commentFeedback.setBool_is_developer(false);
            }

            if(object.has("activeStatus") && object.get("activeStatus") != null){
                boolean activeStatus = object.getBoolean("activeStatus");
                commentFeedback.setActiveStatus(activeStatus);
            } else {
                commentFeedback.setActiveStatus(false);
            }

            commentFeedback.setFeedback(feedbackService.find(feedbackId));
            commentFeedback.setUser(endUserService.find(userId));
            commentFeedback.setCommentText(commentText);
            return commentFeedbackService.save(commentFeedback);
        }
        return null;
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.DELETE, value = "/comments/{id}")
    public void deleteFeedback(@PathVariable long applicationId, @PathVariable long id,
                               @RequestHeader(value = "Authentication") String admin) {
        if(admin.equals("admin")){
            commentFeedbackService.delete(id);
        }
    }

//    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
//    @RequestMapping(method = RequestMethod.DELETE, value = "/comments/{id}")
//    public void deleteComment(@PathVariable long applicationId, @PathVariable long id) {
//        commentFeedbackService.delete(id);
//    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.PUT, value = "/comments")
    public CommentFeedback updateComment(@PathVariable long applicationId, @RequestBody CommentFeedback comment) {
        return commentFeedbackService.save(comment);
    }
}