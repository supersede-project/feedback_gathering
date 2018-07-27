package ch.fhnw.cere.repository.controllers;

import ch.fhnw.cere.repository.controllers.exceptions.BadRequestException;
import ch.fhnw.cere.repository.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.repository.models.AndroidUser;
import ch.fhnw.cere.repository.models.Feedback;
import ch.fhnw.cere.repository.models.FeedbackResponse;
import ch.fhnw.cere.repository.services.AndroidUserService;
import ch.fhnw.cere.repository.services.FeedbackResponseService;
import ch.fhnw.cere.repository.services.FeedbackService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/{language}/applications/{applicationId}/feedbacks/{feedbackId}/responses")
public class FeedbackResponseController extends BaseController {

    @Autowired
    private FeedbackResponseService feedbackResponseService;
    @Autowired
    private AndroidUserService androidUserService;
    @Autowired
    private FeedbackService feedbackService;

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<FeedbackResponse> getFeedbackResponses(@PathVariable long applicationId, @PathVariable long feedbackId) {
        return feedbackResponseService.findByFeedbackId(feedbackId);
    }


    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.POST, value = "")
    public FeedbackResponse createFeedbackResponse(@PathVariable long applicationId, @PathVariable long feedbackId, HttpEntity<String> createdResponse) {
        if (createdResponse.getBody() != null) {
            JSONObject obj = new JSONObject(createdResponse.getBody());
            String username = obj.getString("username");
            String content = obj.getString("content");
            AndroidUser respondingAndroidUser = androidUserService.findByName(username);
            Feedback respondedFeedback = feedbackService.find(feedbackId);
            if(respondingAndroidUser != null && respondedFeedback != null) {
                FeedbackResponse feedbackResponse = new FeedbackResponse(respondingAndroidUser, respondedFeedback, content);
                return feedbackResponseService.save(feedbackResponse);
            }
            throw new NotFoundException();

        }

        throw new BadRequestException();

    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteFeedbackResponse(@PathVariable long applicationId, @PathVariable long id) {
        feedbackResponseService.delete(id);
    }
}