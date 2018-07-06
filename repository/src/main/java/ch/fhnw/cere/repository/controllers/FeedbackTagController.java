package ch.fhnw.cere.repository.controllers;

import ch.fhnw.cere.repository.models.FeedbackTag;
import ch.fhnw.cere.repository.services.FeedbackTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/{language}/applications/{applicationId}/feedbacks/tags")
public class FeedbackTagController extends BaseController {

    @Autowired
    private FeedbackTagService feedbackTagService;

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "")
    public Map<String, List<String>> getFeedbackTags(@PathVariable long applicationId) {
        List<String> result = feedbackTagService.findDistinctTag(applicationId);
        Map<String, List<String>> response = new HashMap<>();
        response.put("tags", result);
        return response;
    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public FeedbackTag getFeedbackTags(@PathVariable long applicationId, @PathVariable long id) {
        return feedbackTagService.find(id);
    }


    //TODO: Find out if this is even necessery
    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.POST, value = "")
    public List<FeedbackTag> createFeedbackTags(HttpEntity<String> labelRequest) {
        /*if (labelRequest.getBody() != null) {
            JSONObject obj = new JSONObject(labelRequest.getBody());
            JSONArray labels = obj.getJSONArray("labels");
            for (int i = 0; i < labels.length(); i++) {
                String currentLabel = labels.getString(i);

            }
        }*/

        return null;

    }

    @PreAuthorize("@securityService.hasAdminPermission(#applicationId)")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public void deleteFeedbackTag(@PathVariable long applicationId, @PathVariable long id) {
        feedbackTagService.delete(id);
    }
}