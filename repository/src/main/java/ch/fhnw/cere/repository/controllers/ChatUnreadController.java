package ch.fhnw.cere.repository.controllers;

import ch.fhnw.cere.repository.models.ChatUnread;
import ch.fhnw.cere.repository.services.ChatUnreadService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Aydinli on 20.02.2018.
 */
@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/{language}/applications/{applicationId}/feedbacks")
public class ChatUnreadController {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(FeedbackController.class);

    @Autowired
    private ChatUnreadService chatUnreadService;

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/chat_unread")
    public List<ChatUnread> getAllChatUnreads(@PathVariable long applicationId) {
        return chatUnreadService.findAll();
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/chat_unread" +
            "/feedback/{feedbackId}")
    public List<ChatUnread> getChatUnreadForFeedback(@PathVariable long applicationId,
                                                    @PathVariable long feedbackId) {
        return chatUnreadService.findByFeedbackId(feedbackId);
    }

//    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/chat_unread" +
            "/user/{userId}")
    public List<ChatUnread> getChatUnreadForUser(@PathVariable long applicationId,
                                                     @PathVariable long userId) {
        return chatUnreadService.findByEnduserId(userId);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/chat_unread" +
            "/feedback/{feedbackId}/user/{userId}")
    public List<ChatUnread> getChatUnreadForFeedbackAndUser(
            @PathVariable long applicationId,
            @PathVariable long feedbackId,
            @PathVariable long userId) {
        return chatUnreadService.findByFeedbackIdAndEnduserId(feedbackId,userId);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.POST, value = "/chat_unread" +
            "/feedback/{feedbackId}/user/{userId}")
    public void deleteChatUnreadForFeedbackAndUser(
            @PathVariable long applicationId,
            @PathVariable long feedbackId,
            @PathVariable long userId) {
        chatUnreadService.delete(chatUnreadService.
                findByFeedbackIdAndEnduserId(feedbackId,userId));
    }
}
