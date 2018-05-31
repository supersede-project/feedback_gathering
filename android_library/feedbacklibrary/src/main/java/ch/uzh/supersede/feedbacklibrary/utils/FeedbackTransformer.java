package ch.uzh.supersede.feedbacklibrary.utils;

import java.util.List;

import ch.uzh.supersede.feedbacklibrary.beans.FeedbackDetailsBean;
import ch.uzh.supersede.feedbacklibrary.feedback.Feedback;
import ch.uzh.supersede.feedbacklibrary.models.AbstractFeedbackPart;
import ch.uzh.supersede.feedbacklibrary.models.AudioFeedback;
import ch.uzh.supersede.feedbacklibrary.models.LabelFeedback;
import ch.uzh.supersede.feedbacklibrary.models.RatingFeedback;
import ch.uzh.supersede.feedbacklibrary.models.ScreenshotFeedback;
import ch.uzh.supersede.feedbacklibrary.models.TextFeedback;

public class FeedbackTransformer {
    private FeedbackTransformer() {
    }

    public static Feedback FeedbackDetailsBeanToFeedback(FeedbackDetailsBean feedbackDetailsBean, long applicationId, List<AbstractFeedbackPart> mechanisms) {
        return new Feedback.Builder()
                .withApplicationId(applicationId)
                .withTitle(feedbackDetailsBean.getTitle())
                .withUserIdentification(feedbackDetailsBean.getUserName())
                .withContextInformation()
                .withAudioMechanism((AudioFeedback) getMechanism(mechanisms, AudioFeedback.class))
                .withCategoryMechanism((LabelFeedback) getMechanism(mechanisms, LabelFeedback.class))
                .withRatingMechanism((RatingFeedback) getMechanism(mechanisms, RatingFeedback.class))
                .withScreenshotMechanism((ScreenshotFeedback) getMechanism(mechanisms, ScreenshotFeedback.class))
                .withTextMechanism((TextFeedback) getMechanism(mechanisms, TextFeedback.class))
                .build();
    }

    private static AbstractFeedbackPart getMechanism(List<AbstractFeedbackPart> mechanisms, Class<? extends AbstractFeedbackPart> mechanismType) {
        for (AbstractFeedbackPart mechanism : mechanisms) {
            if (mechanism.getClass().equals(mechanismType)) {
                return mechanism;
            }
        }
        return null;
    }
}
