package ch.fhnw.cere.repository.controllers;



import ch.fhnw.cere.repository.controllers.exceptions.BadRequestException;
import ch.fhnw.cere.repository.models.AndroidUser;
import ch.fhnw.cere.repository.models.Feedback;
import ch.fhnw.cere.repository.models.FeedbackVote;
import ch.fhnw.cere.repository.services.AndroidUserService;
import ch.fhnw.cere.repository.services.FeedbackService;
import ch.fhnw.cere.repository.services.FeedbackVoteService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/{language}/applications/{applicationId}/feedbacks/{feedbackId}/votes")
public class FeedbackVoteController extends BaseController {

    @Autowired
    private FeedbackVoteService feedbackVoteService;

    @Autowired
    private AndroidUserService androidUserService;

    @Autowired
    private FeedbackService feedbackService;

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<FeedbackVote> getFeedbackVotes(@PathVariable long applicationId, @PathVariable long feedbackId) {
        return feedbackVoteService.findByFeedbackId(feedbackId);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public FeedbackVote getFeedbackVote(@PathVariable long applicationId, @PathVariable long id) {
           return feedbackVoteService.find(id);
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.PUT, value = "")
    public FeedbackVote createFeedbackVote(@PathVariable long applicationId, @PathVariable long feedbackId, HttpEntity<String> createdVote) {
        if (createdVote.getBody() != null) {
            JSONObject obj = new JSONObject(createdVote.getBody());
            int vote = obj.getInt("vote");
            String voterUserName = obj.getString("voterUsername");
            AndroidUser voterAndroidUser = androidUserService.findByName(voterUserName);
            Feedback votedFeedback = feedbackService.find(feedbackId);
            FeedbackVote feedbackVote = feedbackVoteService.findByFeedbackIdAndVoterUserId(votedFeedback.getId(),voterAndroidUser.getId());
            if(feedbackVote != null) {
                feedbackVote.setVote(vote);
            } else {
                feedbackVote = new FeedbackVote(votedFeedback, voterAndroidUser, vote);
            }
            return feedbackVoteService.save(feedbackVote);

        }

        throw new BadRequestException();

    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteFeedbackVote(@PathVariable long applicationId, @PathVariable long id) {
         feedbackVoteService.delete(id);
    }
}