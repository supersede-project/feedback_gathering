package ch.fhnw.cere.repository.controllers;


import ch.fhnw.cere.repository.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.repository.integration.DataProviderIntegrator;
import ch.fhnw.cere.repository.integration.FeedbackCentralIntegrator;
import ch.fhnw.cere.repository.models.*;
import ch.fhnw.cere.repository.models.orchestrator.Application;
import ch.fhnw.cere.repository.services.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/{language}/applications/{applicationId}/feedbacks")
public class FeedbackSettingsController extends BaseController {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(FeedbackSettingsController.class);

    @Autowired
    private FeedbackSettingsService feedbackSettingsService;

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/feedbacksettings")
    public List<FeedbackSettings> getAllFeedbackSettings(@PathVariable long applicationId) {
        return feedbackSettingsService.findAll();
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/{feedbackId}/feedbacksettings")
    public FeedbackSettings getSettingsForFeedback(@PathVariable long applicationId, @PathVariable long feedbackId) {
        FeedbackSettings feedbackSettings = feedbackSettingsService.find(feedbackId);
        if (feedbackSettings == null) {
            throw new NotFoundException();
        }
        return feedbackSettings;
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/feedbacksettings/user_identification/{userIdentification}")
    public List<FeedbackSettings> getFeedbackSettingsByUserIdentification(@PathVariable long applicationId, @PathVariable long userIdentification) {
        List<FeedbackSettings> listFeedbackSettings = feedbackSettingsService.findByUserId(userIdentification);
        return listFeedbackSettings;
    }

//    @ResponseStatus(HttpStatus.CREATED)
//    @RequestMapping(method = RequestMethod.POST, value = "/feedbacksettings")
//    public FeedbackSettings createFeedbackSetting(@RequestBody FeedbackSettings feedbackSettings) throws IOException, ServletException {
//        System.out.println("Some Stats");
//        Feedback mockFeedback = new Feedback();
//        EndUser mockUser = new EndUser();
//        mockUser.setId(1);
//        mockFeedback.setId(1);
//        feedbackSettings.setFeedback(mockFeedback);
//        feedbackSettings.setUser(mockUser);
//        System.out.println(feedbackSettings.getFeedback());
//        System.out.println(feedbackSettings.getFeedbackQuery());
//        FeedbackSettings createdSettings = feedbackSettingsService.save(feedbackSettings);
//        return createdSettings;
//    }
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "/feedbacksettings")
    public FeedbackSettings createFeedbackSetting(@RequestBody FeedbackSettings feedbackSettings) throws IOException, ServletException {
        if(feedbackSettings != null){
            System.out.println("Request is not null hihi");
            Feedback mockFeedback = new Feedback();
            EndUser mockUser = new EndUser();
            mockUser.setId(1000);
            mockFeedback.setId(1000);
            feedbackSettings.setFeedback(mockFeedback);
            feedbackSettings.setUser(mockUser);
//            if(request.getPart("statusUpdates").toString().toLowerCase().equals("yes")){
//                feedbackSettings.setStatusUpdates(true);
//            } else {
//                feedbackSettings.setStatusUpdates(false);
//            }
//            if(request.getPart("feedbackQuery").toString().toLowerCase().equals("yes")){
//                feedbackSettings.setFeedbackQuery(true);
//            } else {
//                feedbackSettings.setFeedbackQuery(false);
//            }
//            feedbackSettings.setStatusUpdatesContactChannel(request.getPart("statusUpdatesContactChannel").toString());
//            feedbackSettings.setFeedbackQueryChannel(request.getPart("feedbackQueryChannel").toString());
//            if(request.getPart("globalFeedbackSetting").toString().toLowerCase().equals("true")){
//                feedbackSettings.setGlobalFeedbackSetting(true);
//            } else {
//                feedbackSettings.setGlobalFeedbackSetting(false);
//            }
            return feedbackSettingsService.save(feedbackSettings);
        }
        return null;
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.DELETE, value = "/feedbacksettings/{feedbacksettingsId}")
    public void deleteFeedbackSettings(@PathVariable long applicationId, @PathVariable long feedbacksettingsId) {
        feedbackSettingsService.delete(feedbacksettingsId);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.PUT, value = "/feedbacksettings")
    public FeedbackSettings updateFeedbackSettings(@PathVariable long applicationId, @RequestBody FeedbackSettings feedbackSettings) {
        return feedbackSettingsService.save(feedbackSettings);
    }
}
