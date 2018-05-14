package ch.fhnw.cere.repository.controllers;



import ch.fhnw.cere.repository.models.FeedbackVote;
import ch.fhnw.cere.repository.services.FeedbackVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/{language}/applications/{applicationId}/feedbacks/{id}/votes")
public class FeedbackVoteController extends BaseController {

    @Autowired
    private FeedbackVoteService feedbackVoteService;

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<FeedbackVote> getFeedback(@PathVariable long applicationId, @PathVariable long id) {
        return feedbackVoteService.findByFeedbackId(id);

    }
}