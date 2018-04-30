package ch.fhnw.cere.repository.controllers;

import ch.fhnw.cere.repository.models.*;
import ch.fhnw.cere.repository.services.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/applications/{androidApplicationId}/feedback")
public class AndroidFeedbackController extends BaseController {

    @Autowired
    private FeedbackService feedbackService;

    @RequestMapping(method = RequestMethod.GET, value = "")
    public List<Feedback> getApplicationFeedback(@PathVariable String androidApplicationId) {
        return feedbackService.findByAndroidApplicationId(androidApplicationId);
    }

}
