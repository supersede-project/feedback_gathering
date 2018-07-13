package ch.uzh.supersede.feedbacklibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.supersede.feedbacklibrary.beans.FeedbackBean;
import ch.uzh.supersede.feedbacklibrary.beans.FeedbackDetailsBean;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.models.*;
import ch.uzh.supersede.feedbacklibrary.stubs.GeneratorStub;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.UserConstants.USER_NAME;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.FEEDBACK_STATUS.OPEN;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.ACTIVE;

public class FeedbackUtility {
    private FeedbackUtility() {
    }

    public static Feedback createFeedback(Context context, List<AbstractFeedbackPart> feedbackPart, String feedbackTitle, String[] feedbackTags) {
        return new Feedback.Builder()
                .withTitle(feedbackTitle)
                .withUserIdentification(FeedbackDatabase.getInstance(context).readString(USER_NAME, null))
                .withContextInformation(context)
                .withAudioFeedback((AudioFeedback) getFeedbackPart(feedbackPart, AudioFeedback.class))
                .withLabelFeedback((LabelFeedback) getFeedbackPart(feedbackPart, LabelFeedback.class))
                .withRatingFeedback((RatingFeedback) getFeedbackPart(feedbackPart, RatingFeedback.class))
                .withScreenshotFeedback((ScreenshotFeedback) getFeedbackPart(feedbackPart, ScreenshotFeedback.class))
                .withTextFeedback((TextFeedback) getFeedbackPart(feedbackPart, TextFeedback.class))
                .withTags(feedbackTags)
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

    /**
     * Transforms List<Feedback> to List<FeedbackDetailsBean>.
     *
     * @param response Object of a Retrofit2 response, presumably a List<Feedback>
     * @param context  Context or activity
     * @return transformed List<Feedback> to List<FeedbackDetailsBean>
     */
    @SuppressWarnings("unchecked")
    public static List<FeedbackDetailsBean> transformFeedbackResponse(Object response, Context context) {
        List<FeedbackDetailsBean> feedbackDetailsBeans = new ArrayList<>();
        if (response instanceof List) {
            for (Feedback feedback : (List<Feedback>) response) {
                FeedbackDetailsBean feedbackDetailsBean = FeedbackUtility.feedbackToFeedbackDetailsBean(context, feedback);
                if (feedbackDetailsBean != null) { //Avoid NP caused by old Repository Feedback
                    feedbackDetailsBeans.add(feedbackDetailsBean);
                }
            }
        }
        return feedbackDetailsBeans;
    }

    public static FeedbackDetailsBean feedbackToFeedbackDetailsBean(Context context, Feedback feedback) {
        int minUpVotes = 0; // TODO not yet implemented
        int maxUpVotes = 10; // TODO not yet implemented
        Enums.FEEDBACK_STATUS status = OPEN;  // TODO not yet implemented
        int upVotes = 0; // TODO not yet implemented
        int responses = 0; // TODO not yet implemented

        String description = null;

        if (!feedback.getTextFeedbackList().isEmpty()) {
            description = feedback.getTextFeedbackList().get(0).getText();
        }
        String userName = feedback.getUserIdentification();
        long timeStamp = feedback.getCreatedAt() != null ? feedback.getCreatedAt().getTime() : System.currentTimeMillis();
        Bitmap bitmap = null;
        if (ACTIVE.check(context)) {
            bitmap = Utils.loadAnnotatedImageFromDatabase(context);
            bitmap = bitmap != null ? bitmap : Utils.loadImageFromDatabase(context);
        }

        String title = feedback.getTitle();
        String[] tags = feedback.getTags();
        //TODO: Dani, we need title & tags, workaround for release 2
        if (title == null || title.length() == 0) {
            title = "#Dummy-Title# " + GeneratorStub.BagOfFeedbackTitles.pickRandom();
        }
        FeedbackBean feedbackBean = new FeedbackBean.Builder()
                .withFeedbackId(feedback.getId())
                .withTitle(title)
                .withTags(tags)
                .withUserName(userName)
                .withTimestamp(timeStamp)
                .withUpVotes(upVotes)
                .withMinUpVotes(minUpVotes)
                .withMaxUpVotes(maxUpVotes)
                .withResponses(responses)
                .withStatus(status)
                .build();
        if (feedbackBean == null) {
            return null; //Avoid NP caused by old Feedback on the Repository
        }
        return new FeedbackDetailsBean.Builder()
                .withFeedbackId(feedbackBean.getFeedbackId())
                .withFeedbackBean(feedbackBean)
                .withDescription(description)
                .withUserName(userName)
                .withTimestamp(timeStamp)
                .withStatus(status)
                .withUpVotes(upVotes)
                .withBitmap(bitmap)
                .build();
    }
}
