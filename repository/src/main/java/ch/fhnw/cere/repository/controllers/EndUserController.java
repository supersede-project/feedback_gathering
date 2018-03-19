package ch.fhnw.cere.repository.controllers;

import ch.fhnw.cere.repository.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.repository.models.EndUser;
import ch.fhnw.cere.repository.services.EndUserService;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Aydinli on 25.12.2017.
 */

@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/{language}/applications/{applicationId}/feedbacks")
public class EndUserController {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(FeedbackController.class);

    @Autowired
    private EndUserService endUserService;

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/end_user")
    public List<EndUser> getUserForApplication(@PathVariable long applicationId) {
        return endUserService.findByApplicationId(applicationId);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/end_user/username/{username}")
    public EndUser getUserForUsername(@PathVariable long applicationId,
                                      @PathVariable String username) {
        return endUserService.findByUsername(username);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "/end_user")
    public EndUser createEndUser(HttpEntity<String> feedbackChatJSON) {
        if(feedbackChatJSON.getBody() != null){
            JSONObject object = new JSONObject(feedbackChatJSON.getBody());
            String username = object.getString("username");
            long applicationId = object.getLong("application_id");

            EndUser endUser = new EndUser();
            endUser.setUsername(username);
            endUser.setApplicationId(applicationId);

            return endUserService.save(endUser);
        }
        return null;
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.DELETE, value = "/end_user/{userId}")
    public void deleteEndUser(@PathVariable long applicationId, @PathVariable long userId) {
        endUserService.delete(userId);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/end_user/{userId}")
    public EndUser getUser(@PathVariable long applicationId,
                                                   @PathVariable long userId) {
        EndUser endUser = endUserService.find(userId);
        if(endUser == null){
            throw new NotFoundException();
        }
        return endUser;
    }
}
