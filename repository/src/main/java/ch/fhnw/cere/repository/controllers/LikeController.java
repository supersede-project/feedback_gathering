package ch.fhnw.cere.repository.controllers;

import ch.fhnw.cere.repository.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.repository.models.CommentFeedback;
import ch.fhnw.cere.repository.models.UserFBLike;
import ch.fhnw.cere.repository.services.EndUserServiceImpl;
import ch.fhnw.cere.repository.services.FeedbackServiceImpl;
import ch.fhnw.cere.repository.services.UserFeedbackLikeService;
import org.apache.catalina.User;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Aydinli on 23.12.2017.
 */

@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/{language}/applications/{applicationId}/feedbacks")
public class LikeController {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(FeedbackController.class);
    @Autowired
    private FeedbackServiceImpl feedbackService;

    @Autowired
    private EndUserServiceImpl endUserService;

    @Autowired
    private UserFeedbackLikeService feedbackLikeService;

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/likes")
    public List<UserFBLike> getAllLikes(@PathVariable long applicationId) {
        return feedbackLikeService.findAll();
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/likes/feedback/{feedbackId}")
    public List<UserFBLike> getLikesForFeedback(@PathVariable long applicationId,
                                                @PathVariable long feedbackId) {
        return feedbackLikeService.findByFeedbackId(feedbackId);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/likes/user/{userId}")
    public List<UserFBLike> getLikesForUser(@PathVariable long applicationId,
                                                @PathVariable long userId) {
        return feedbackLikeService.findByEnduserId(userId);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.POST, value = "/likes")
    public UserFBLike createLike(HttpEntity<String> likeJSON) {
        LOGGER.info("Create Like: " + likeJSON.getBody());
        if(likeJSON.getBody() != null){
            JSONObject object = new JSONObject();
            long feedbackId = object.getLong("feedback_id");
            long userId = object.getLong("user_id");

            UserFBLike userFBLike = new UserFBLike();
            userFBLike.setFeedback(feedbackService.find(feedbackId));
            userFBLike.setEnduser(endUserService.find(userId));

            return feedbackLikeService.save(userFBLike);
        }
        return null;
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.DELETE, value = "/likes/{likeId}")
    public void deleteLike(@PathVariable long applicationId, @PathVariable long likeId) {
        feedbackLikeService.delete(likeId);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/likes/{likeId}")
    public UserFBLike getLike(@PathVariable long applicationId,
                           @PathVariable long likeId) {
        UserFBLike userFBLike = feedbackLikeService.find(likeId);
        if(userFBLike == null){
            throw new NotFoundException();
        }
        return userFBLike;
    }
}
