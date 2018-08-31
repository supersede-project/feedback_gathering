package ch.uzh.supersede.feedbacklibrary.utils;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.supersede.feedbacklibrary.beans.*;
import ch.uzh.supersede.feedbacklibrary.components.buttons.*;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.models.*;
import ch.uzh.supersede.feedbacklibrary.stubs.GeneratorStub;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.UserConstants.USER_NAME;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.FEEDBACK_STATUS.OPEN;

public final class FeedbackUtility {
    private FeedbackUtility() {
    }

    public static String getUpvotesAsText(int votes) {
        return votes <= 0 ? String.valueOf(votes) : "+" + votes;
    }

    public static List<FeedbackListItem> createFeedbackListItems(List<Feedback> feedbackList, Context context, LocalConfigurationBean configuration, int topColor, Class<?> callerClass) {
        List<FeedbackListItem> feedbackDetailsBeans = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        for (Feedback feedback : feedbackList) {
            FeedbackDetailsBean feedbackDetailsBean = FeedbackUtility.feedbackToFeedbackDetailsBean(feedback);
            FeedbackListItem listItem = new FeedbackListItem(context, 8, feedbackDetailsBean, configuration, topColor, callerClass);
            listItem.addAllLabels(labels);
            feedbackDetailsBeans.add(listItem);
        }
        float textSize = ScalingUtility.getInstance().getMinTextSizeScaledForWidth(15, 0, 0.4, labels.toArray(new String[labels.size()]));
        for (FeedbackListItem listItem : feedbackDetailsBeans) {
            listItem.equalizeTextSize(textSize);
        }
        return feedbackDetailsBeans;
    }

    public static List<VoteListItem> createFeedbackVotesListItems(List<Feedback> feedbackList, Context context, LocalConfigurationBean configuration, int topColor) {
        List<VoteListItem> voteListItems = new ArrayList<>();
        List<LocalFeedbackBean> feedbackBeansVoted = FeedbackDatabase.getInstance(context).getFeedbackBeans(Enums.FETCH_MODE.VOTED);
        for (Feedback feedback : feedbackList) {
            for (LocalFeedbackBean localFeedbackBean : feedbackBeansVoted) {
                if (feedback.getId() == localFeedbackBean.getFeedbackId()) {
                    FeedbackDetailsBean feedbackDetailsBean = FeedbackUtility.feedbackToFeedbackDetailsBean(feedback);
                    voteListItems.add(new VoteListItem(context, 8, feedbackDetailsBean, localFeedbackBean, configuration, topColor));
                }
            }
        }
        return voteListItems;
    }

    public static List<SubscriptionListItem> createFeedbackSubscriptionListItems(List<Feedback> feedbackList, Context context, LocalConfigurationBean configuration, int topColor) {
        List<SubscriptionListItem> subscriptionListItems = new ArrayList<>();
        for (Feedback feedback : feedbackList) {
            FeedbackDetailsBean feedbackDetailsBean = FeedbackUtility.feedbackToFeedbackDetailsBean(feedback);
            subscriptionListItems.add(new SubscriptionListItem(context, 8, feedbackDetailsBean, configuration, topColor));
        }
        return subscriptionListItems;
    }

    public static List<FeedbackDetailsBean> localFeedbackBeanToFeedbackDetailsBean(List<LocalFeedbackBean> localFeedbackBeans, Context context) {
        List<FeedbackDetailsBean> feedbackDetailsBeans = new ArrayList<>();
        String userName = FeedbackDatabase.getInstance(context).readString(USER_NAME, null);

        for (LocalFeedbackBean bean : localFeedbackBeans) {

            FeedbackBean feedbackBean = new FeedbackBean.Builder()
                    .withFeedbackId(bean.getFeedbackId())
                    .withTitle(bean.getTitle())
                    .withUserName(bean.getOwner() > 0 ? userName : null)
                    .withTimestamp(bean.getCreationDate())
                    .withUpVotes(bean.getVotes())
                    .withResponses(bean.getResponses())
                    .withStatus(bean.getFeedbackStatus())
                    .build();

            FeedbackDetailsBean feedbackDetailsBean = new FeedbackDetailsBean.Builder()
                    .withFeedbackId(feedbackBean.getFeedbackId())
                    .withFeedbackBean(feedbackBean)
                    .withUserName(bean.getOwner() > 0 ? userName : null)
                    .withTimestamp(bean.getCreationDate())
                    .withStatus(bean.getFeedbackStatus())
                    .withUpVotes(bean.getVotes())
                    .withSubscription(bean.getSubscribed() > 0)
                    .build();

            feedbackDetailsBeans.add(feedbackDetailsBean);
        }
        return feedbackDetailsBeans;
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
                FeedbackDetailsBean feedbackDetailsBean = FeedbackUtility.feedbackToFeedbackDetailsBean(feedback);
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
            ids[i] = feedbackBeans.get(i).getFeedbackId();
        }
        return StringUtility.join(ids, ",");
    }

    public static FeedbackDetailsBean feedbackToFeedbackDetailsBean(Feedback feedback) {
        int minUpVotes = feedback.getMinVotes();
        int maxUpVotes = feedback.getMaxVotes();
        Enums.FEEDBACK_STATUS status = (feedback.getFeedbackStatus() != null) ? feedback.getFeedbackStatus() : OPEN;
        int upVotes = feedback.getVotes();
        int responses = (feedback.getFeedbackResponses() != null) ? feedback.getFeedbackResponses().size() : 0;
        boolean isPublic = feedback.isPublic();

        String contextData = buildContextData(feedback.getContextInformationFeedback());

        String description = null;
        if (!feedback.getTextFeedbackList().isEmpty()) {
            description = feedback.getTextFeedbackList().get(0).getText();
        }
        String imageName = null;
        if (feedback.getScreenshotFeedbackList() != null && !feedback.getScreenshotFeedbackList().isEmpty()) {
            imageName = feedback.getScreenshotFeedbackList().get(0).getPath();
        }

        String userName = feedback.getUserIdentification();
        long timeStamp = feedback.getCreatedAt() != null ? feedback.getCreatedAt().getTime() : System.currentTimeMillis();

        String bitmapName = null;
        if (feedback.getScreenshotFeedbackList() != null && !feedback.getScreenshotFeedbackList().isEmpty()) {
            bitmapName = feedback.getScreenshotFeedbackList().get(0).getPath();
        }

        String audioFileName = null;
        if (feedback.getAudioFeedbackList() != null && !feedback.getAudioFeedbackList().isEmpty()) {
            audioFileName = feedback.getAudioFeedbackList().get(0).getPath();
        }

        String title = feedback.getTitle();
        if (title == null || title.length() == 0) {
            title = "#Dummy-Title#" + GeneratorStub.BagOfFeedbackTitles.pickRandom();
        }
        String[] tags = feedback.getTags();

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
                .isPublic(isPublic)
                .build();
        List<FeedbackResponseBean> feedbackResponses = feedbackResponseListToFeedbackResponseBeans(feedbackBean, feedback.getId(), feedback.getFeedbackResponses());
        return new FeedbackDetailsBean.Builder()
                .withFeedbackId(feedback.getId())
                .withFeedbackBean(feedbackBean)
                .withDescription(description)
                .withUserName(userName)
                .withTimestamp(timeStamp)
                .withStatus(status)
                .withUpVotes(upVotes)
                .withBitmapName(bitmapName)
                .withAudioFileName(audioFileName)
                .withResponses(feedbackResponses)
                .withContext(contextData)
                .isPublic(isPublic)
                .build();
    }

    private static String buildContextData(ContextInformationFeedback contextInformation) {
        StringBuilder sb = new StringBuilder();
        String newLine = "\n";
        if (contextInformation != null) {
            sb.append("Android Version: ").append(StringUtility.nulLSafe(contextInformation.getAndroidVersion())).append(newLine);
            sb.append("Resolution: ").append(StringUtility.nulLSafe(contextInformation.getResolution())).append(newLine);
            sb.append(parseMetaData(contextInformation.getMetaData()));
            sb.append("Country: ").append(StringUtility.nulLSafe(contextInformation.getCountry())).append(newLine);
            sb.append("Local Time: ").append(StringUtility.nulLSafe(contextInformation.getLocalTime())).append(newLine);
            sb.append("Timezone: ").append(StringUtility.nulLSafe(contextInformation.getTimeZone())).append(newLine);
        }
        return sb.toString();
    }

    private static String parseMetaData(String metaData) {
        String manufacturer = "MANUFACTURER";
        String hardware = "HARDWARE";
        String model = "MODEL";
        String device = "DEVICE";
        String brand = "BRAND";
        String type = "TYPE";
        if (StringUtility.contains(metaData, manufacturer, hardware, model, device, brand, type)) {
            try {
                StringBuilder sb = new StringBuilder();
                for (String data : metaData.split(";")) {
                    if (data.contains(":")) {
                        sb.append(formatData(data));
                    }
                }
                return sb.toString();
            } catch (Exception e) {
                //NOP Fall-Through
            }
        }
        return "Metadata: " + StringUtility.nulLSafe(metaData) + "\n";
    }

    private static String formatData(String data) {
        String[] tokens = data.split(":");
        String formatted = tokens[0].substring(0, 1).concat(tokens[0].substring(1, tokens[0].length()).toLowerCase());
        return formatted.concat(": ").concat(tokens[1]).concat("\n");
    }

    private static List<FeedbackResponseBean> feedbackResponseListToFeedbackResponseBeans(FeedbackBean feedbackBean, long feedbackId, List<FeedbackResponse> feedbackResponses) {
        List<FeedbackResponseBean> feedbackResponseBeans = new ArrayList<>();

        if (feedbackResponses != null) {
            for (FeedbackResponse feedbackResponse : feedbackResponses) {
                String responseUserName = feedbackResponse.getUser().getName();
                feedbackResponseBeans.add(new FeedbackResponseBean.Builder()
                        .withFeedbackId(feedbackId)
                        .withResponseId(feedbackResponse.getId())
                        .withContent(feedbackResponse.getContent())
                        .withUserName(responseUserName)
                        .withTimestamp((feedbackResponse.getUpdatedAt() != null ? feedbackResponse.getUpdatedAt() : feedbackResponse.getCreatedAt()).getTime())
                        .isDeveloper(feedbackResponse.getUser().isDeveloper())
                        .isFeedbackOwner(feedbackBean.getUserName().equals(responseUserName))
                        .build()
                );
            }
        }
        return feedbackResponseBeans;
    }
}
