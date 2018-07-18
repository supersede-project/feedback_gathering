package ch.fhnw.cere.repository.controllers;

import ch.fhnw.cere.repository.models.FeedbackTag;
import ch.fhnw.cere.repository.services.FeedbackTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/{language}/applications/{applicationId}/tags")
public class FeedbackTagController extends BaseController {

    @Autowired
    private FeedbackTagService feedbackTagService;

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<String> getFeedbackTags(@PathVariable long applicationId) {
        return feedbackTagService.findDistinctTag(applicationId);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public FeedbackTag getFeedbackTag(@PathVariable long applicationId, @PathVariable long id) {
        return feedbackTagService.find(id);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteFeedbackTag(@PathVariable long applicationId, @PathVariable long id) {
        feedbackTagService.delete(id);
    }
}