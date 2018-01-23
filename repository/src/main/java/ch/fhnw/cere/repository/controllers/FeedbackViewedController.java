package ch.fhnw.cere.repository.controllers;

import ch.fhnw.cere.repository.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.repository.models.FeedbackViewed;
import ch.fhnw.cere.repository.services.*;
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
public class FeedbackViewedController {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(FeedbackController.class);

    @Autowired
    private FeedbackViewedService feedbackViewedService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private EndUserService endUserService;

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/feedback_views")
    public List<FeedbackViewed> getAllFeedbackViews(@PathVariable long applicationId) {
        return feedbackViewedService.findAll();
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/feedback_views" +
            "/feedback/{feedbackId}")
    public List<FeedbackViewed> getViewsForFeedback(@PathVariable long applicationId,
                                                @PathVariable long feedbackId) {
        return feedbackViewedService.findByFeedbackId(feedbackId);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/feedback_views/user/{userId}")
    public List<FeedbackViewed> getViewsForUser(@PathVariable long applicationId,
                                            @PathVariable long userId) {
        return feedbackViewedService.findByEnduserId(userId);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/feedback_views" +
            "/both/{userId}/{feedbackId}")
    public FeedbackViewed getViewsForUserAndFeedback(@PathVariable long applicationId,
                                                 @PathVariable long userId,
                                                 @PathVariable long feedbackId) {
        return feedbackViewedService.findByEnduserIdAndFeedbackId(userId,feedbackId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "/feedback_views")
    public FeedbackViewed createView(HttpEntity<String> viewJSON) {
        LOGGER.info("Create View: " + viewJSON.getBody());
        if(viewJSON.getBody() != null){
            JSONObject object = new JSONObject(viewJSON.getBody());
            long feedbackId = object.getLong("feedback_id");
            long userId = object.getLong("user_id");

            FeedbackViewed feedbackViewed = new FeedbackViewed();
            feedbackViewed.setFeedback(feedbackService.find(feedbackId));
            feedbackViewed.setEnduser(endUserService.find(userId));

            return feedbackViewedService.save(feedbackViewed);
        }
        return null;
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.DELETE, value = "/feedback_views/{feedbackViewId}")
    public void deleteView(@PathVariable long applicationId, @PathVariable long feedbackViewId) {
        feedbackViewedService.delete(feedbackViewId);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/feedback_views/{feedbackViewId}")
    public FeedbackViewed getLike(@PathVariable long applicationId,
                              @PathVariable long feedbackViewId) {
        FeedbackViewed feedbackView = feedbackViewedService.find(feedbackViewId);
        if(feedbackView == null){
            throw new NotFoundException();
        }
        return feedbackView;
    }
}
