package ch.fhnw.cere.repository.controllers;

import ch.fhnw.cere.repository.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.repository.models.UserFBDislike;
import ch.fhnw.cere.repository.models.UserFBLike;
import ch.fhnw.cere.repository.services.*;
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
public class DislikeController {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(FeedbackController.class);

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private EndUserService endUserService;

    @Autowired
    private UserFeedbackDislikeService feedbackDislikeService;

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/dislikes")
    public List<UserFBDislike> getAllDislikes(@PathVariable long applicationId) {
        return feedbackDislikeService.findAll();
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/dislikes/feedback/{feedbackId}")
    public List<UserFBDislike> getDislikesForFeedback(@PathVariable long applicationId,
                                                @PathVariable long feedbackId) {
        return feedbackDislikeService.findByFeedbackId(feedbackId);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/dislikes/user/{userId}")
    public List<UserFBDislike> getDislikesForUser(@PathVariable long applicationId,
                                                @PathVariable long userId) {
        return feedbackDislikeService.findByEnduserId(userId);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.POST, value = "/dislikes")
    public UserFBDislike createDislike(HttpEntity<String> dislikeJSON) {
        LOGGER.info("Create Like: " + dislikeJSON.getBody());
        if(dislikeJSON.getBody() != null){
            JSONObject object = new JSONObject();
            long feedbackId = object.getLong("feedback_id");
            long userId = object.getLong("user_id");

            UserFBDislike userFBDislike = new UserFBDislike();
            userFBDislike.setFeedback(feedbackService.find(feedbackId));
            userFBDislike.setEnduser(endUserService.find(userId));

            return feedbackDislikeService.save(userFBDislike);
        }
        return null;
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.DELETE, value = "/dislike/{dislikeId}")
    public void deleteDislike(@PathVariable long applicationId, @PathVariable long likeId) {
        feedbackDislikeService.delete(likeId);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/dislike/{dislikeId}")
    public UserFBDislike getDislike(@PathVariable long applicationId,
                           @PathVariable long likeId) {
        UserFBDislike userFBDislike = feedbackDislikeService.find(likeId);
        if(userFBDislike == null){
            throw new NotFoundException();
        }
        return userFBDislike;
    }
}
