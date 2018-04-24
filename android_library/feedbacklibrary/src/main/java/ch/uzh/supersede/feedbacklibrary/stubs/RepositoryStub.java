package ch.uzh.supersede.feedbacklibrary.stubs;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.*;

import ch.uzh.supersede.feedbacklibrary.beans.*;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.stubs.GeneratorStub.BagOfLabels;
import ch.uzh.supersede.feedbacklibrary.utils.*;
import ch.uzh.supersede.feedbacklibrary.utils.Enums.FEEDBACK_STATUS;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.TECHNICAL_USER_NAME;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.USER_NAME;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.FEEDBACK_STATUS.*;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.ACTIVE;

public class RepositoryStub {
    private RepositoryStub(){
    }

    public static List<FeedbackBean> getFeedback(Context context, int count, int minUpVotes,int maxUpVotes, float ownFeedbackPercent) {
        ArrayList<FeedbackBean> feedbackBeans = new ArrayList<>();
        for (int f = 0; f < count; f++) {
            feedbackBeans.add(getFeedback(context,minUpVotes,maxUpVotes,ownFeedbackPercent));
        }
        return feedbackBeans;
    }
    public static List<FeedbackResponseBean> getFeedbackResponses(Context context, int count, long feedbackCreationDate, float developerPercent, float ownerPercent, FeedbackBean feedbackBean) {
        ArrayList<FeedbackResponseBean> feedbackResponseBeans = new ArrayList<>();
        for (int f = 0; f < count; f++) {
            feedbackResponseBeans.add(getFeedbackResponse(context,feedbackCreationDate,developerPercent,ownerPercent,feedbackBean));
        }
        return feedbackResponseBeans;
    }

    public static FeedbackDetailsBean getFeedbackDetails(Context context, FeedbackBean feedbackBean) {
        String[] content = generateDescriptionAndTitle();
        String description = content[0];
        String title = content[1];
        String userName = feedbackBean.getUserName();
        String technicalUserName = feedbackBean.getTechnicalUserName();
        String[] labels = BagOfLabels.pickRandom(5);
        long timeStamp = generateTimestamp();
        FEEDBACK_STATUS status = generateFeedbackStatus();
        List<FeedbackResponseBean> feedbackResponses = getFeedbackResponses(context, NumberUtility.randomInt(0, 10), timeStamp, 0.1f, 0.1f, feedbackBean);
        return new FeedbackDetailsBean.Builder()
                .withFeedbackBean(feedbackBean)
                .withTitle(title)
                .withDescription(description)
                .withUserName(userName)
                .withTechnicalUserName(technicalUserName)
                .withLabels(labels)
                .withTimestamp(timeStamp)
                .withStatus(status)
                .withResponses(feedbackResponses)
                .build();
    }

    private static FeedbackResponseBean getFeedbackResponse(Context context, long feedbackCreationDate, float developerPercent, float ownerPercent, FeedbackBean feedbackBean) {
        int upperBound = NumberUtility.divide(1,developerPercent);
        boolean feedbackOwner = NumberUtility.randomInt(0,upperBound>0?upperBound-1:upperBound)==0;
        upperBound = NumberUtility.divide(1,ownerPercent);
        boolean developerFeedback = NumberUtility.randomInt(0,upperBound>0?upperBound-1:upperBound)==0;
        String content = generateContent();
        String userName = feedbackOwner?feedbackBean.getUserName():generateUserName(context, false);
        String technicalUserName = generateTechnicalUserName(context, false);
        long timeStamp = DateUtility.getPastDateAfter(feedbackCreationDate);
        return new FeedbackResponseBean.Builder()
                .withContent(content)
                .withUserName(userName)
                .withTechnicalUserName(technicalUserName)
                .withTimestamp(timeStamp)
                .isDeveloper(developerFeedback)
                .isFeedbackOwner(feedbackOwner)
                .build();
    }

    private static FeedbackBean getFeedback(Context context, int minUpVotes,int maxUpVotes, float ownFeedbackPercent) {
        int upperBound = NumberUtility.divide(1,ownFeedbackPercent);
        boolean ownFeedback = ACTIVE.check(context)&&NumberUtility.randomInt(0,upperBound>0?upperBound-1:upperBound)==0;
        FEEDBACK_STATUS feedbackStatus = generateFeedbackStatus();
        String title = generateTitle();
        String userName = generateUserName(context, ownFeedback);
        String technicalUserName = generateTechnicalUserName(context, ownFeedback);
        long timeStamp = generateTimestamp();
        int upVotes = generateUpVotes(minUpVotes,maxUpVotes,feedbackStatus);
        int responses = generateResponses();
        return new FeedbackBean.Builder()
                .withTitle(title)
                .withUserName(userName)
                .withTechnicalUserName(technicalUserName)
                .withTimestamp(timeStamp)
                .withUpVotes(upVotes)
                .withMinUpVotes(minUpVotes)
                .withMaxUpVotes(maxUpVotes)
                .withResponses(responses)
                .withStatus(feedbackStatus)
                .build();
    }

    private static FEEDBACK_STATUS generateFeedbackStatus() {
        FEEDBACK_STATUS[] status = new FEEDBACK_STATUS[]{OPEN,IN_PROGRESS,REJECTED,DUPLICATE,CLOSED};
        return status[NumberUtility.randomPosition(status)];
    }

    private static int generateResponses() {
        return NumberUtility.randomInt(0,50);
    }

    private static int generateUpVotes(int minUpVotes, int maxUpVotes, FEEDBACK_STATUS feedbackStatus) {
        if (CompareUtility.oneOf(feedbackStatus,REJECTED)){
            return NumberUtility.randomInt(minUpVotes,-1);
        }else if (CompareUtility.oneOf(feedbackStatus,DUPLICATE)){
            return 0;
        }
        return NumberUtility.randomInt(0,maxUpVotes);
    }

    private static long generateTimestamp() {
        return DateUtility.getPastDateLong(2);
    }

    @NonNull
    private static String generateTechnicalUserName(Context context,boolean own) {
        if (own){
            return FeedbackDatabase.getInstance(context).readString(TECHNICAL_USER_NAME, null);
        }
        return UUID.randomUUID().toString();
    }

    @NonNull
    private static String generateUserName(Context context,boolean own) {
        if (own){
            return FeedbackDatabase.getInstance(context).readString(USER_NAME, null);
        }
        return RepositoryStub.getUniqueName(GeneratorStub.BagOfNames.pickRandom());
    }

    @NonNull
    private static String generateTitle() {
        return BagOfLabels.pickRandom().concat("-Feedback");
    }

    @NonNull
    private static String generateContent() {
        return GeneratorStub.BagOfResponses.pickRandom();
    }

    @NonNull
    private static String[] generateDescriptionAndTitle() {
        return GeneratorStub.BagOfFeedback.pickRandomWithTitle();
    }

    //Should be generated on the Server
    //Return value is something like Jake --> Jake#12345678 (random 8 digits)
    public static String getUniqueName(String name) {
        return name.concat("#").concat(String.valueOf(NumberUtility.multiply(99999999, Math.random())));
    }
}
