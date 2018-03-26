package ch.fhnw.cere.repository.controllers;


import ch.fhnw.cere.repository.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.repository.integration.DataProviderIntegrator;
import ch.fhnw.cere.repository.integration.FeedbackCentralIntegrator;
import ch.fhnw.cere.repository.models.*;
import ch.fhnw.cere.repository.models.orchestrator.Application;
import ch.fhnw.cere.repository.services.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.stage.Screen;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/{language}/applications/{applicationId}/feedbacks")
public class FeedbackController extends BaseController {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(FeedbackController.class);

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private FeedbackViewedService feedbackViewedService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private FeedbackStatusService feedbackStatusService;

    @Autowired
    private DataProviderIntegrator dataProviderIntegrator;

    @Autowired
    private FeedbackCentralIntegrator feedbackCentralIntegrator;

    @Autowired
    private FeedbackEmailService feedbackEmailService;

    @Autowired
    private OrchestratorApplicationService orchestratorApplicationService;

    @Value("${integration.feedback_central}")
    private boolean feedbackCentralIntegrationEnabled;

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<Feedback> getApplicationFeedbacks(@PathVariable long applicationId) {
        return feedbackService.findByApplicationId(applicationId());
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/full")
    public List<Feedback> getDetailedApplicationFeedbacks(@PathVariable long applicationId) {
        List<Feedback> feedbacks = feedbackService.findByApplicationId(applicationId());

        Application orchestratorApplication = null;
        try {
            orchestratorApplication = this.orchestratorApplicationService.loadApplication(feedbacks.get(0).getLanguage(), feedbacks.get(0).getApplicationId());
            for(Feedback feedback : feedbacks) {
                Feedback.appendMechanismsToFeedback(orchestratorApplication, feedback);
                feedback.setApplication(orchestratorApplication);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return feedbacks;
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public Feedback getFeedback(@PathVariable long applicationId, @PathVariable long id) {
        Feedback feedback = feedbackService.find(id);
        if (feedback == null) {
            throw new NotFoundException();
        }
        return feedback;
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/user_identification/{userIdentification}")
    public List<Feedback> getFeedbacksByUserIdentification(@PathVariable long applicationId, @PathVariable long userIdentification) {
        List<Feedback> feedbacks = feedbackService.findByUserIdentification(userIdentification);
        return feedbacks;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "", consumes = "multipart/form-data")
    public Feedback createFeedback(HttpServletRequest request) throws IOException, ServletException {
        LOGGER.info("request feedback: " + request.toString());
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultiValueMap<String, MultipartFile> parts = multipartRequest.getMultiFileMap();

        MultipartFile jsonPart = parts.getFirst("json");
        ByteArrayInputStream stream = new ByteArrayInputStream(jsonPart.getBytes());
        String jsonString = IOUtils.toString(stream, "UTF-8");

        LOGGER.info("Feedback Json");
        LOGGER.info(jsonString);

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        Feedback feedback = mapper.readValue(jsonString, Feedback.class);
        feedback.setApplicationId(applicationId());
        if(feedback.getLanguage() == null) {
            feedback.setLanguage("en");
        }

        // TODO rewrite
        if(feedback.getContextInformation() != null) {
            feedback.getContextInformation().setFeedback(feedback);
        }
        if(feedback.getTextFeedbacks() != null) {
            for(TextFeedback textFeedback : feedback.getTextFeedbacks()) {
                textFeedback.setFeedback(feedback);
            }
        }
        if(feedback.getRatingFeedbacks() != null) {
            for(RatingFeedback ratingFeedback : feedback.getRatingFeedbacks()) {
                ratingFeedback.setFeedback(feedback);
            }
        }
        if(feedback.getCategoryFeedbacks() != null) {
            for(CategoryFeedback categoryFeedback : feedback.getCategoryFeedbacks()) {
                categoryFeedback.setFeedback(feedback);
            }
        }
        if(feedback.getScreenshotFeedbacks() != null) {
            for(ScreenshotFeedback screenshotFeedback : feedback.getScreenshotFeedbacks()) {
                screenshotFeedback.setFeedback(feedback);
            }
        }
        if(feedback.getAttachmentFeedbacks() != null) {
            for(AttachmentFeedback attachmentFeedback : feedback.getAttachmentFeedbacks()) {
                attachmentFeedback.setFeedback(feedback);
            }
        }
        if(feedback.getAudioFeedbacks() != null) {
            for(AudioFeedback audioFeedback : feedback.getAudioFeedbacks()) {
                audioFeedback.setFeedback(feedback);
            }
        }
        if(feedback.getStatuses() != null) {
            for(Status status : feedback.getStatuses()) {
                status.setFeedback(feedback);
            }
        }

        parts.remove("json");
        feedback = fileStorageService.storeFiles(feedback, parts);

        Feedback createdFeedback = feedbackService.save(feedback);

//        String feedbackState = randomState().name().toLowerCase();
        String feedbackState = "received";
        FeedbackStatus feedbackStatus = new FeedbackStatus(createdFeedback,feedbackState);
        feedbackStatusService.save(feedbackStatus);

        dataProviderIntegrator.ingestJsonData(feedback);
        // feedbackEmailService.sendFeedbackNotification(createdFeedback);
        if(feedbackCentralIntegrationEnabled) {
            feedbackCentralIntegrator.ingestJsonData(feedback);
        }

        return createdFeedback;
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(value = "/attachments/{path:.+}", method = RequestMethod.GET)
    public void downloadFeedbackAttachmentFile(@PathVariable long applicationId, @PathVariable("path") String fileName, HttpServletResponse response) {
        try {
            InputStream inputStream = new FileInputStream(fileStorageService.getFeedbackAttachmentFileByPath(fileName, applicationId));
            org.apache.commons.io.IOUtils.copy(inputStream, response.getOutputStream());
            response.addHeader("Content-disposition", "attachment;filename=" + fileName);
            response.flushBuffer();
        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file to output stream");
        }
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(value = "/audios/{path:.+}", method = RequestMethod.GET)
    public void downloadFeedbackAudioFile(@PathVariable long applicationId, @PathVariable("path") String fileName, HttpServletResponse response) {
        try {
            InputStream inputStream = new FileInputStream(fileStorageService.getFeedbackAudioFileByPath(fileName, applicationId));
            org.apache.commons.io.IOUtils.copy(inputStream, response.getOutputStream());
            response.addHeader("Content-disposition", "attachment;filename=" + fileName);
            response.flushBuffer();
        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file to output stream");
        }
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(value = "/screenshots/{path:.+}", method = RequestMethod.GET)
    public void downloadFeedbackScreenshotFile(@PathVariable long applicationId, @PathVariable("path") String fileName, HttpServletResponse response) {
        try {
            InputStream inputStream = new FileInputStream(fileStorageService.getFeedbackScreenshotFileByPath(fileName, applicationId));
            org.apache.commons.io.IOUtils.copy(inputStream, response.getOutputStream());
            response.addHeader("Content-disposition", "attachment;filename=" + fileName);
            response.flushBuffer();
        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file to output stream");
        }
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteFeedback(@PathVariable long applicationId, @PathVariable long id) {
        feedbackService.delete(id);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.PUT, value = "")
    public Feedback updateFeedback(@PathVariable long applicationId, @RequestBody Feedback feedback) {
        return feedbackService.save(feedback);
    }


    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.PUT, value = "/blocked/{feedbackId}")
    public String feedbackBlock(@PathVariable long applicationId,
                                @PathVariable long feedbackId,
                                HttpEntity<String> blockJSON){
        if(blockJSON.getBody() != null){
            JSONObject obj = new JSONObject(blockJSON.getBody());
            Boolean blocked = obj.getBoolean("blocked");
            Feedback updateFeedback = feedbackService.find(feedbackId);
            if(updateFeedback != null) {
                updateFeedback.setBlocked(blocked);
                feedbackService.save(updateFeedback);
                return "Feedback blocked status changed!";
            } else {
                return "Requested Feedback does not exist";
            }
        }
        return "JSON Body is NULL";
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.PUT, value = "/visibility/{feedbackId}")
    public String feedbackVisibility(@PathVariable long applicationId,
                                @PathVariable long feedbackId,
                                HttpEntity<String> visibleJSON){
        if(visibleJSON.getBody() != null){
            JSONObject obj = new JSONObject(visibleJSON.getBody());
            Boolean visible = obj.getBoolean("visible");
            Feedback updateFeedback = feedbackService.find(feedbackId);
            if(updateFeedback != null) {
                updateFeedback.setVisibility(visible);
                feedbackService.save(updateFeedback);
                return "Feedback visibility changed!";
            } else {
                return "Requested Feedback does not exist";
            }
        }
        return "JSON Body is NULL";
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.PUT, value = "/published/{feedbackId}")
    public String feedbackPublished(@PathVariable long applicationId,
                                     @PathVariable long feedbackId,
                                     HttpEntity<String> publishedJSON){
        if(publishedJSON.getBody() != null){
            JSONObject obj = new JSONObject(publishedJSON.getBody());
            Boolean published = obj.getBoolean("published");
            Feedback updateFeedback = feedbackService.find(feedbackId);
            if(updateFeedback != null) {
                updateFeedback.setPublished(published);
                feedbackService.save(updateFeedback);
                return "Feedback published state changed!";
            } else {
                return "Requested Feedback does not exist";
            }
        }
        return "JSON Body is NULL";
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/get_blocked/{value}")
    public List<Feedback> blockedFeedbacks(@PathVariable long applicationId,
                                    @PathVariable boolean value){
        return feedbackService.findByBlocked(value);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/get_visible/{value}")
    public List<Feedback> visibleFeedbacks(@PathVariable long applicationId,
                                            @PathVariable boolean value){
        return feedbackService.findByVisibility(value);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/get_published/{value}")
    public List<Feedback> publishedFeedbacks(@PathVariable long applicationId,
                                           @PathVariable boolean value){
        return feedbackService.findByPublished(value);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/get_published/unread" +
            "/user/{userId}")
    public List<Feedback> publishedUnreadFeedbacks(@PathVariable long applicationId,
                                                   @PathVariable long userId){
        List<FeedbackViewed> viewedFeedbacks = feedbackViewedService.findByEnduserId(userId);
        List<Feedback> publishedFeedbacks = feedbackService.findByPublished(true);
        List<Feedback> ownFeedbacks = feedbackService.findByUserIdentification(userId);
        publishedFeedbacks.removeAll(ownFeedbacks);

        for(FeedbackViewed feedbackViewed:viewedFeedbacks){
            publishedFeedbacks.removeIf(feedback -> feedback.getId()==feedbackViewed.getFeedback().getId());
        }
        return publishedFeedbacks;
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/pending_publication")
    public List<Feedback> getPendingFeedbacks(@PathVariable long applicationId){
        return feedbackService.findByPublishedAndVisibility(false,true);
    }

    private EnumStatus randomState(){
        int pick = new Random().nextInt(EnumStatus.values().length);
        return EnumStatus.values()[pick];
    }
}
