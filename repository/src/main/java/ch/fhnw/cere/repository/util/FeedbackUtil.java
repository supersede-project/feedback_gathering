package ch.fhnw.cere.repository.util;

import ch.fhnw.cere.repository.models.Feedback;
import ch.fhnw.cere.repository.services.FeedbackService;

import java.util.Comparator;
import java.util.List;

public class FeedbackUtil {


    private FeedbackUtil() {
    }

    public static List<Feedback> setMinMaxVotes(List<Feedback> feedbackList, long applicationId, FeedbackService feedbackService) {
        List<Feedback> applicationFeedbackList = feedbackService.findByApplicationId(applicationId);

        int minVotes = 0;
        int maxVotes = 0;
        Feedback minFeedback = applicationFeedbackList.stream().min(Comparator.comparingInt(Feedback::getVotes)).orElse(null);
        Feedback maxFeedback = applicationFeedbackList.stream().max(Comparator.comparingInt(Feedback::getVotes)).orElse(null);

        if (minFeedback != null) {
            minVotes = minFeedback.getVotes();
        }

        if (maxFeedback != null) {
            maxVotes = maxFeedback.getVotes();
        }

        int finalMinVotes = minVotes;
        int finalMaxVotes = maxVotes;
        feedbackList.forEach(feedback -> feedback.setMinMaxVotes(finalMinVotes, finalMaxVotes));

        return feedbackList;
    }

    public static Feedback setMinMaxVotes(Feedback feedback, long applicationId, FeedbackService feedbackService) {
        List<Feedback> applicationFeedbackList = feedbackService.findByApplicationId(applicationId);

        int minVotes = 0;
        int maxVotes = 0;
        Feedback minFeedback = applicationFeedbackList.stream().min(Comparator.comparingInt(Feedback::getVotes)).orElse(null);
        Feedback maxFeedback = applicationFeedbackList.stream().max(Comparator.comparingInt(Feedback::getVotes)).orElse(null);

        if (minFeedback != null) {
            minVotes = minFeedback.getVotes();
        }

        if (maxFeedback != null) {
            maxVotes = maxFeedback.getVotes();
        }

        feedback.setMinMaxVotes(minVotes, maxVotes);

        return feedback;
    }
}
