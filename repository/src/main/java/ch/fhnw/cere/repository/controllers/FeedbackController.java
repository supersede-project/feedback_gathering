package ch.fhnw.cere.repository.controllers;


import ch.fhnw.cere.repository.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.repository.integration.DataProviderIntegrator;
import ch.fhnw.cere.repository.models.*;
import ch.fhnw.cere.repository.services.FeedbackEmailService;
import ch.fhnw.cere.repository.services.FeedbackEmailServiceImpl;
import ch.fhnw.cere.repository.services.FeedbackService;
import ch.fhnw.cere.repository.services.FileStorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.stage.Screen;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;


@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/{language}/applications/{applicationId}/feedbacks")
public class FeedbackController extends BaseController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private DataProviderIntegrator dataProviderIntegrator;

    @Autowired
    private FeedbackEmailService feedbackEmailService;


    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<Feedback> getApplicationFeedbacks(@PathVariable long applicationId) {
        return feedbackService.findByApplicationId(applicationId());
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
    public List<Feedback> getFeedbacksByUserIdentification(@PathVariable long applicationId, @PathVariable String userIdentification) {
        List<Feedback> feedbacks = feedbackService.findByUserIdentification(userIdentification);
        return feedbacks;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "", consumes = "multipart/form-data")
    public Feedback createFeedback(HttpServletRequest request) throws IOException, ServletException {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultiValueMap<String, MultipartFile> parts = multipartRequest.getMultiFileMap();

        MultipartFile jsonPart = parts.getFirst("json");
        ByteArrayInputStream stream = new ByteArrayInputStream(jsonPart.getBytes());
        String jsonString = IOUtils.toString(stream, "UTF-8");

        ObjectMapper mapper = new ObjectMapper();
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
        dataProviderIntegrator.ingestJsonData(feedback);
        feedbackEmailService.sendFeedbackNotification(createdFeedback);

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
}
