package ch.fhnw.cere.repository.controllers;


import ch.fhnw.cere.repository.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.repository.models.Feedback;
import ch.fhnw.cere.repository.services.FeedbackService;
import ch.fhnw.cere.repository.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/{language}/applications/{applicationId}/feedbacks")
public class FeedbackController extends BaseController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private FileStorageService fileStorageService;

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
    public Feedback createFeedback(HttpServletRequest request, @RequestPart("json") Feedback feedback) throws IOException, ServletException {
        feedback.setApplicationId(applicationId());

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultiValueMap<String, MultipartFile> parts = multipartRequest.getMultiFileMap();
        parts.remove("json");
        feedback = fileStorageService.storeFiles(feedback, parts);

        return feedbackService.save(feedback);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteFeedback(@PathVariable long id) {
        feedbackService.delete(id);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.PUT, value = "")
    public Feedback updateFeedback(@RequestBody Feedback feedback) {
        return feedbackService.save(feedback);
    }
}
