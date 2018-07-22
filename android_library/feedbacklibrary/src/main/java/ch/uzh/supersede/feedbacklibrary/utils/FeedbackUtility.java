package ch.uzh.supersede.feedbacklibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.*;

import ch.uzh.supersede.feedbacklibrary.beans.*;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.models.*;
import ch.uzh.supersede.feedbacklibrary.stubs.GeneratorStub;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.UserConstants.USER_NAME;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.FEEDBACK_STATUS.OPEN;

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

    public static String getIds(List<LocalFeedbackBean> feedbackBeans) {
        if (feedbackBeans.isEmpty()) {
            return null;
        }

        long[] ids = new long[feedbackBeans.size()];
        for (int i = 0; i < feedbackBeans.size(); i++) {
            ids[1] = feedbackBeans.get(i).getFeedbackId();
        }
        return StringUtility.join(ids, ",");
    }

    public static FeedbackDetailsBean feedbackToFeedbackDetailsBean(Context context, Feedback feedback) {
        int minUpVotes = 0; // TODO not yet implemented
        int maxUpVotes = 10; // TODO not yet implemented
        Enums.FEEDBACK_STATUS status = OPEN;  // TODO not yet implemented
        int upVotes = 0; // TODO not yet implemented
        int responses = 0; // TODO not yet implemented

        String description = null;
        String imageName = null;
        if (feedback.getScreenshotFeedbackList() != null && !feedback.getScreenshotFeedbackList().isEmpty()){
            imageName = feedback.getScreenshotFeedbackList().get(0).getPath();
        }

        if (!feedback.getTextFeedbackList().isEmpty()) {
            description = feedback.getTextFeedbackList().get(0).getText();
        }
        String userName = feedback.getUserIdentification();
        long timeStamp = feedback.getCreatedAt() != null ? feedback.getCreatedAt().getTime() : System.currentTimeMillis();
        Bitmap bitmap = null; //TODO bitmap
        String bitmapName = null;

        if (feedback.getScreenshotFeedbackList() != null && !feedback.getScreenshotFeedbackList().isEmpty()) {
            bitmapName = feedback.getScreenshotFeedbackList().get(0).getPath();
        }

        String title = feedback.getTitle();
        String[] tags = feedback.getTags();
        //TODO: Dani, we need title & tags, workaround for release 4
        if (title == null || title.length() == 0){
            title = "#Dummy-Title#"+(imageName!=null?"* ":" ")+ GeneratorStub.BagOfFeedbackTitles.pickRandom();
        }else{
            title = title +(imageName!=null?"* ":" ");
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
        if (feedbackBean == null){
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
                .withBitmapName(bitmapName)
                .build();
    }
}
