package ch.fhnw.cere.repository.controllers;

import ch.fhnw.cere.repository.models.EmailUnsubscribed;
import ch.fhnw.cere.repository.services.EmailUnsubscribedService;
import ch.fhnw.cere.repository.services.EmailUnsubscribedServiceImpl;
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
 * Created by Aydinli on 31.01.2018.
 */
@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/{language}/applications/{applicationId}/feedbacks")
public class EmailUnsubscribedController {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(FeedbackController.class);

    @Autowired
    private EmailUnsubscribedService emailUnsubscribedService;

    @Autowired
    private EndUserService endUserService;

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/unsubscribed")
    public List<EmailUnsubscribed> getAllUnsubscriptions(@PathVariable long applicationId) {
        return emailUnsubscribedService.findAll();
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/unsubscribed/user/{userId}")
    public EmailUnsubscribed getUnsubscriptionForUser(@PathVariable long applicationId,
                                                @PathVariable long userId) {
        return emailUnsubscribedService.findByEnduserId(userId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "/unsubscribed")
    public EmailUnsubscribed unsubscribeUser(HttpEntity<String> unsubscribedJSON) {
        if(unsubscribedJSON.getBody() != null){
            JSONObject object = new JSONObject(unsubscribedJSON.getBody());
            long userId = object.getLong("userId");
            String email = object.getString("email");

            if(emailUnsubscribedService.findByEnduserId(userId)!= null){
                return emailUnsubscribedService.findByEnduserId(userId);
            }

            EmailUnsubscribed emailUnsubscribed= new EmailUnsubscribed();
            emailUnsubscribed.setEnduser(endUserService.find(userId));
            emailUnsubscribed.setEmail(email);

            return emailUnsubscribedService.save(emailUnsubscribed);
        }
        return null;
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.DELETE, value = "/unsubscribed/{emailUnsubscribedId}")
    public void deleteUnsubscription(@PathVariable long applicationId, @PathVariable long emailUnsubscribedId) {
        emailUnsubscribedService.delete(emailUnsubscribedId);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.DELETE, value = "/unsubscribed/user/{userId}")
    public void deleteUnsubscriptionForUser(@PathVariable long applicationId, @PathVariable long userId) {
        emailUnsubscribedService.delete(emailUnsubscribedService.findByEnduserId(userId).getId());
    }
}
