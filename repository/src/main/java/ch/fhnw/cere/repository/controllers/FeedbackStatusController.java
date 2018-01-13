package ch.fhnw.cere.repository.controllers;

import ch.fhnw.cere.repository.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.repository.models.FeedbackStatus;
import ch.fhnw.cere.repository.models.UserFBDislike;
import ch.fhnw.cere.repository.services.FeedbackService;
import ch.fhnw.cere.repository.services.FeedbackStatusService;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Path;
import java.util.List;

/**
 * Created by Aydinli on 23.12.2017.
 */
@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/{language}/applications/{applicationId}/feedbacks")
public class FeedbackStatusController {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(FeedbackController.class);

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private FeedbackStatusService feedbackStatusService;

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/status")
    public List<FeedbackStatus> getAllStates(@PathVariable long applicationId) {
        return feedbackStatusService.findAll();
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/status/feedback/{feedbackId}")
    public List<FeedbackStatus> getStatusForFeedback(@PathVariable long applicationId,
                                                     @PathVariable long feedbackId) {
        return feedbackStatusService.findByFeedbackId(feedbackId);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/status/{status}")
    public List<FeedbackStatus> getStatusForStatus(@PathVariable long applicationId,
                                                     @PathVariable String status) {
        return feedbackStatusService.findByStatus(status);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.POST, value = "/status")
    public FeedbackStatus createStatus(HttpEntity<String> statusJSON) {
        LOGGER.info("Create Like: " + statusJSON.getBody());
        if(statusJSON.getBody() != null){
            JSONObject object = new JSONObject();
            long feedbackId = object.getLong("feedback_id");
            String status = object.getString("status");

            FeedbackStatus feedbackStatus = new FeedbackStatus();
            feedbackStatus.setFeedback(feedbackService.find(feedbackId));
            feedbackStatus.setStatus(status);

            return feedbackStatusService.save(feedbackStatus);
        }
        return null;
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.DELETE, value = "/status/{statusId}")
    public void deleteStatus(@PathVariable long applicationId, @PathVariable long statusId) {
        feedbackStatusService.delete(statusId);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/status/{statusId}")
    public FeedbackStatus getStatus(@PathVariable long applicationId,
                                    @PathVariable long statusId) {
        FeedbackStatus feedbackStatus = feedbackStatusService.find(statusId);
        if(feedbackStatus == null){
            throw new NotFoundException();
        }
        return feedbackStatus;
    }
}
