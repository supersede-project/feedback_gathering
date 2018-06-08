package ch.uzh.supersede.feedbacklibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.List;
import java.util.UUID;

import ch.uzh.supersede.feedbacklibrary.beans.FeedbackBean;
import ch.uzh.supersede.feedbacklibrary.beans.FeedbackDetailsBean;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.models.AbstractFeedbackPart;
import ch.uzh.supersede.feedbacklibrary.models.AudioFeedback;
import ch.uzh.supersede.feedbacklibrary.models.Feedback;
import ch.uzh.supersede.feedbacklibrary.models.LabelFeedback;
import ch.uzh.supersede.feedbacklibrary.models.RatingFeedback;
import ch.uzh.supersede.feedbacklibrary.models.ScreenshotFeedback;
import ch.uzh.supersede.feedbacklibrary.models.TextFeedback;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.USER_NAME;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.FEEDBACK_STATUS.OPEN;

public class FeedbackUtility {
    private FeedbackUtility() {
    }

    public static Feedback createFeedback(Context context, List<AbstractFeedbackPart> feedbackPart) {
        return new Feedback.Builder()
                //.withTitle() TODO not yet implemented
                .withUserIdentification(FeedbackDatabase.getInstance(context).readString(USER_NAME, null))
                .withContextInformation(context)
                .withAudioFeedback((AudioFeedback) getFeedbackPart(feedbackPart, AudioFeedback.class))
                .withLabelFeedback((LabelFeedback) getFeedbackPart(feedbackPart, LabelFeedback.class))
                .withRatingFeedback((RatingFeedback) getFeedbackPart(feedbackPart, RatingFeedback.class))
                .withScreenshotFeedback((ScreenshotFeedback) getFeedbackPart(feedbackPart, ScreenshotFeedback.class))
                .withTextFeedback((TextFeedback) getFeedbackPart(feedbackPart, TextFeedback.class))
                .build();
    }

    private static AbstractFeedbackPart getFeedbackPart(List<AbstractFeedbackPart> feedbackParts, Class<? extends AbstractFeedbackPart> feedbackPartType) {
        for (AbstractFeedbackPart feedbackPart : feedbackParts) {
            if (feedbackPart.getClass().equals(feedbackPartType)) {
                return feedbackPart;
            }
        }
        return null;
    }

    public static FeedbackDetailsBean feedbackToFeedbackDetailsBean(Context context, Feedback feedback) {
        int minUpVotes = 0; // TODO not yet implemented
        int maxUpVotes = 10; // TODO not yet implemented
        String title = ""; // TODO not yet implemented
        Enums.FEEDBACK_STATUS status = OPEN;  // TODO not yet implemented
        int upVotes = 0; // TODO not yet implemented
        int responses = 0; // TODO not yet implemented
        UUID feedbackId = UUID.randomUUID(); // TODO not yet implemented

        String description = null;
        String[] labels = new String[0];

        if (!feedback.getTextFeedbackList().isEmpty()) {
            description = feedback.getTextFeedbackList().get(0).getText();
        }
        if (!feedback.getLabelFeedbackList().isEmpty()) {
            labels = feedback.getLabelFeedbackList().get(0).getLabels().toArray(new String[0]);
        }

        String userName = feedback.getUserIdentification();
        long timeStamp = feedback.getCreatedAt() != null ? feedback.getCreatedAt().getTime() : 0;
        Bitmap bitmap = Utils.loadAnnotatedImageFromDatabase(context);
        bitmap = bitmap != null ? bitmap : Utils.loadImageFromDatabase(context);

        FeedbackBean feedbackBean = new FeedbackBean.Builder()
                .withFeedbackUid(feedbackId)
                .withTitle(title)
                .withUserName(userName)
                .withTimestamp(timeStamp)
                .withUpVotes(upVotes)
                .withMinUpVotes(minUpVotes)
                .withMaxUpVotes(maxUpVotes)
                .withResponses(responses)
                .withStatus(status)
                .build();
        return new FeedbackDetailsBean.Builder()
                .withFeedbackUid(feedbackBean.getFeedbackUid())
                .withFeedbackBean(feedbackBean)
                .withTitle(feedbackBean.getTitle())
                .withDescription(description)
                .withUserName(userName)
                .withLabels(labels)
                .withTimestamp(timeStamp)
                .withStatus(status)
                .withUpVotes(upVotes)
                .withBitmap(bitmap)
                .build();
    }
}
