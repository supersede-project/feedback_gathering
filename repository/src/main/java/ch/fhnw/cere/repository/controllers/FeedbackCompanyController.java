package ch.fhnw.cere.repository.controllers;

import ch.fhnw.cere.repository.controllers.exceptions.NotFoundException;
import ch.fhnw.cere.repository.models.FeedbackCompany;
import ch.fhnw.cere.repository.services.FeedbackCompanyService;
import ch.fhnw.cere.repository.services.FeedbackService;
import org.hibernate.Criteria;
import org.json.JSONObject;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.jms.Session;
import java.util.List;

/**
 * Created by Aydinli on 25.12.2017.
 */

@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/{language}/applications/{applicationId}/feedbacks")
public class FeedbackCompanyController {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(FeedbackController.class);

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private FeedbackCompanyService feedbackCompanyService;

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/feedback_company")
    public List<FeedbackCompany> getAllCompanyFeedbacks(@PathVariable long applicationId) {
//        if(promote){
//            return feedbackCompanyService.findByPromote(true);
//        } else {
//            return feedbackCompanyService.findAll();
//        }
        return feedbackCompanyService.findAll();
    }

//    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
//    @RequestMapping(method = RequestMethod.GET, value = "/feedback_company/feedback/{feedbackId}")
//    public List<FeedbackCompany> getFeedbackCompanyForFeedback(@PathVariable long applicationId,
//                                                     @PathVariable long feedbackId) {
//        return feedbackCompanyService.findByFeedbackId(feedbackId);
//    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, value = "/feedback_company")
    public FeedbackCompany createFeedbackCompany(HttpEntity<String> feedbackCompanyJSON) {
        if(feedbackCompanyJSON.getBody() != null){
            JSONObject object = new JSONObject(feedbackCompanyJSON.getBody());
//            long feedbackId = object.getLong("feedback_id");
            String status = object.getString("status");
            String text = object.getString("text");

            FeedbackCompany feedbackCompany = new FeedbackCompany();

            if(object.has("promote") && object.get("promote") != null){
                boolean promote = object.getBoolean("promote");
                feedbackCompany.setPromote(promote);
            }

//            feedbackCompany.setFeedback(feedbackService.find(feedbackId));
            feedbackCompany.setStatus(status);
            feedbackCompany.setText(text);

            return feedbackCompanyService.save(feedbackCompany);
        }
        return null;
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.DELETE, value = "/feedback_company/{feedbackCompanyId}")
    public void deleteFeedbackCompany(@PathVariable long applicationId, @PathVariable long feedbackCompanyId) {
        feedbackCompanyService.delete(feedbackCompanyId);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/feedback_company/{feedbackCompanyId}")
    public FeedbackCompany getFeedbackCompany(@PathVariable long applicationId,
                                    @PathVariable long feedbackCompanyId) {
        FeedbackCompany feedbackCompany = feedbackCompanyService.find(feedbackCompanyId);
        if(feedbackCompany == null){
            throw new NotFoundException();
        }
        return feedbackCompany;
    }
}
