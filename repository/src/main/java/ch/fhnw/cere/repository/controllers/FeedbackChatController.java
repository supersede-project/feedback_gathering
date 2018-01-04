package ch.fhnw.cere.repository.controllers;

import ch.fhnw.cere.repository.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.repository.models.FeedbackChatInformation;
import ch.fhnw.cere.repository.services.EndUserService;
import ch.fhnw.cere.repository.services.FeedbackChatInformationService;
import ch.fhnw.cere.repository.services.FeedbackService;
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
 * Created by Aydinli on 25.12.2017.
 */

@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/{language}/applications/{applicationId}/feedbacks")
public class FeedbackChatController {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(FeedbackController.class);

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private EndUserService endUserService;

    @Autowired
    private FeedbackChatInformationService feedbackChatInformationService;

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/feedback_chat")
    public List<FeedbackChatInformation> getAllFeedbackChats(@PathVariable long applicationId) {
        return feedbackChatInformationService.findAll();
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/feedback_chat/feedback/{feedbackId}")
    public List<FeedbackChatInformation> getFeedbackChatForFeedback(@PathVariable long applicationId,
                                                               @PathVariable long feedbackId) {
        return feedbackChatInformationService.findByFeedbackId(feedbackId);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/feedback_chat/user/{userId}")
    public List<FeedbackChatInformation> getFeedbackChatForUser(@PathVariable long applicationId,
                                                                    @PathVariable long userId) {
        return feedbackChatInformationService.findByUserId(userId);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.POST, value = "/feedback_chat")
    public FeedbackChatInformation createFeedbackChat(HttpEntity<String> feedbackChatJSON) {
        if(feedbackChatJSON.getBody() != null){
            JSONObject object = new JSONObject();
            long feedbackId = object.getLong("feedback_id");
            long userId = object.getLong("user_id");
            String chat_text = object.getString("chat_text");
            Boolean initiatedByUser = object.getBoolean("initiated_by_user");

            FeedbackChatInformation feedbackChatInformation = new FeedbackChatInformation();
            feedbackChatInformation.setFeedback(feedbackService.find(feedbackId));
            feedbackChatInformation.setUser(endUserService.find(userId));
            feedbackChatInformation.setChatText(chat_text);
            feedbackChatInformation.setInitatedByUser(initiatedByUser);

            return feedbackChatInformationService.save(feedbackChatInformation);
        }
        return null;
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.DELETE, value = "/feedback_chat/{feedbackChatId}")
    public void deleteFeedbackChat(@PathVariable long applicationId, @PathVariable long feedbackChatId) {
        feedbackChatInformationService.delete(feedbackChatId);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/feedback_chat/{feedbackChatId}")
    public FeedbackChatInformation getFeedbackChat(@PathVariable long applicationId,
                                              @PathVariable long feedbackChatId) {
        FeedbackChatInformation feedbackChatInformation = feedbackChatInformationService.find(feedbackChatId);
        if(feedbackChatInformation == null){
            throw new NotFoundException();
        }
        return feedbackChatInformation;
    }
}
