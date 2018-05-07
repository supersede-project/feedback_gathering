package ch.uzh.supersede.feedbacklibrary.utils;

import java.util.List;

import ch.uzh.supersede.feedbacklibrary.beans.FeedbackDetailsBean;
import ch.uzh.supersede.feedbacklibrary.feedback.Feedback;
import ch.uzh.supersede.feedbacklibrary.models.AbstractMechanism;
import ch.uzh.supersede.feedbacklibrary.models.AttachmentMechanism;
import ch.uzh.supersede.feedbacklibrary.models.AudioMechanism;
import ch.uzh.supersede.feedbacklibrary.models.CategoryMechanism;
import ch.uzh.supersede.feedbacklibrary.models.RatingMechanism;
import ch.uzh.supersede.feedbacklibrary.models.ScreenshotMechanism;
import ch.uzh.supersede.feedbacklibrary.models.TextMechanism;

public class FeedbackTransformer {
    private FeedbackTransformer() {
    }

    public static Feedback FeedbackDetailsBeanToFeedback(FeedbackDetailsBean feedbackDetailsBean, long applicationId, List<AbstractMechanism> mechanisms) {
        return new Feedback.Builder()
                .withApplicationId(applicationId)
                .withTitle(feedbackDetailsBean.getTitle())
                .withUserIdentification(feedbackDetailsBean.getUserName())
                .withAttachmentMechanism((AttachmentMechanism) getMechanism(mechanisms, AttachmentMechanism.class))
                .withAudioMechanism((AudioMechanism) getMechanism(mechanisms, AudioMechanism.class))
                .withCategoryMechanism((CategoryMechanism) getMechanism(mechanisms, CategoryMechanism.class))
                .withRatingMechanism((RatingMechanism) getMechanism(mechanisms, RatingMechanism.class))
                .withScreenshotMechanism((ScreenshotMechanism) getMechanism(mechanisms, ScreenshotMechanism.class))
                .withTextMechanism((TextMechanism) getMechanism(mechanisms, TextMechanism.class))
                .build();
    }

    public static AbstractMechanism getMechanism(List<AbstractMechanism> mechanisms, Class<? extends AbstractMechanism> mechanismType) {
        for (AbstractMechanism mechanism : mechanisms) {
            if (mechanism.getClass().equals(mechanismType)) {
                return mechanism;
            }
        }
        return null;
    }
}
