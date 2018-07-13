package ch.fhnw.cere.repository.controllers;

import ch.fhnw.cere.repository.models.AndroidUser;
import ch.fhnw.cere.repository.models.Feedback;
import ch.fhnw.cere.repository.models.FeedbackReport;
import ch.fhnw.cere.repository.services.AndroidUserService;
import ch.fhnw.cere.repository.services.FeedbackReportService;
import ch.fhnw.cere.repository.services.FeedbackService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/{language}/applications/{applicationId}/reports")
public class FeedbackReportController extends BaseController {

    @Autowired
    private FeedbackReportService feedbackReportService;
    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private AndroidUserService androidUserService;

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<FeedbackReport> getFeedbackReports(@PathVariable long applicationId) {
        return feedbackReportService.findByApplicationId(applicationId);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public FeedbackReport getFeedbackReport(@PathVariable long applicationId, @PathVariable long id) {
        return feedbackReportService.find(id);
    }

    //TODO: CHECK if this is the right way to to this...
    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.POST, value = "")
    public FeedbackReport createFeedbackReport(@PathVariable long applicationId, HttpEntity<String> feedbackReportRequest) {
        JSONObject obj = new JSONObject(feedbackReportRequest.getBody());
        long feedbackId = obj.getLong("feedbackId");
        String reporterUserName = obj.getString("reporterUserName");
        String reason = obj.getString("reason");

        AndroidUser androidUser = androidUserService.findByNameAndApplicationId(reporterUserName, applicationId);
        Feedback feedback = feedbackService.find(feedbackId);

        FeedbackReport feedbackReport = new FeedbackReport(feedback,androidUser,reason);

        return feedbackReportService.save(feedbackReport);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteFeedbackReport(@PathVariable long applicationId, @PathVariable long id) {
        feedbackReportService.delete(id);
    }
}