package ch.uzh.supersede.feedbacklibrary.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.List;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.activities.FeedbackHubActivity;
import ch.uzh.supersede.feedbacklibrary.beans.*;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.models.AndroidUser;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.FETCH_MODE.SUBSCRIBED;

public class NotificationUtility {
    private static NotificationUtility instance;

    private String userName;
    private boolean userIsDeveloper;
    private boolean userIsBlocked;
    private int userKarma;

    private NotificationUtility(Context context) {
        initDatabaseState(context);
    }

    public static NotificationUtility getInstance(Context context) {
        if (instance == null) {
            instance = new NotificationUtility(context);
        }
        return instance;
    }

    private void initDatabaseState(Context context) {
        userName = FeedbackDatabase.getInstance(context).readString(USER_NAME, null);
        userIsDeveloper = FeedbackDatabase.getInstance(context).readBoolean(USER_IS_DEVELOPER, false);
        userKarma = FeedbackDatabase.getInstance(context).readInteger(USER_KARMA, 0);
        userIsBlocked = FeedbackDatabase.getInstance(context).readBoolean(USER_IS_BLOCKED, false);
    }

    private Notification createNotification(String title, String message, Context context, LocalConfigurationBean configuration) {
        if (message.isEmpty()){
            return null;
        }

        Intent intent = new Intent(context, FeedbackHubActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(EXTRA_KEY_HOST_APPLICATION_NAME, configuration.getHostApplicationName());
        intent.putExtra(EXTRA_KEY_APPLICATION_CONFIGURATION, configuration);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_star_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
    }

    public Notification createUserUpdateNotification(AndroidUser androidUser, Context context, LocalConfigurationBean configuration) {
        StringBuilder message = new StringBuilder();
        if (androidUser.isBlocked() != userIsBlocked) {
            String partMessage = androidUser.isBlocked() ? "Your account got blocked!" : "Your account got unblocked!";
            message.append(partMessage);
            message.append('\n');
            userIsBlocked = androidUser.isBlocked();
            FeedbackDatabase.getInstance(context).writeBoolean(USER_IS_BLOCKED, userIsBlocked);
        }
        if (androidUser.getKarma() != userKarma) {
            int increase = userKarma - androidUser.getKarma();
            String partMessage = increase > 0 ? "Your karma has increased by " : "Your karma has decreased by ";
            message.append(partMessage);
            message.append(increase);
            message.append('\n');
            userKarma = androidUser.getKarma();
            FeedbackDatabase.getInstance(context).writeInteger(USER_KARMA, userKarma);
        }
        if (androidUser.isDeveloper() != userIsDeveloper) {
            String partMessage = androidUser.isDeveloper() ? "Your are now a developer!" : "Your no longer a developer!";
            message.append(partMessage);
            message.append('\n');
            userIsDeveloper = androidUser.isDeveloper();
            FeedbackDatabase.getInstance(context).writeBoolean(USER_IS_DEVELOPER, userIsDeveloper);
        }
        if (!androidUser.getName().equals(userName)) {
            userName = androidUser.getName();
            message.append("Your username has changed to ");
            message.append(userName);
            message.append('\n');
            FeedbackDatabase.getInstance(context).writeString(USER_NAME, userName);
        }

        return createNotification("Your user has received new updates", message.toString(), context, configuration);
    }

    public Notification createFeedbackUpdateNotification(List<FeedbackDetailsBean> newFeedbackDetailsBeans, Context context, LocalConfigurationBean configuration) {
        List<LocalFeedbackBean> oldFeedbackBeans = FeedbackDatabase.getInstance(context).getFeedbackBeans(SUBSCRIBED);
        int newResponses = 0;
        int newOwnResponses = 0;
        int newVotes = 0;
        int newOwnVotes = 0;
        int statusUpdates = 0;
        int ownStatusUpdates = 0;
        int visibilityUpdates = 0;

        for (FeedbackDetailsBean newFeedback : newFeedbackDetailsBeans) {
            for (LocalFeedbackBean oldFeedback : oldFeedbackBeans) {
                if (newFeedback.getFeedbackId() == oldFeedback.getFeedbackId()) {
                    int voteChange = newFeedback.getUpVotes() - oldFeedback.getVotes();
                    int responseChange = newFeedback.getResponses().size() - oldFeedback.getResponses();
                    int statusChange = newFeedback.getFeedbackStatus() != oldFeedback.getFeedbackStatus() ? 1 : 0;
                    newResponses += responseChange;
                    newVotes += voteChange;
                    statusUpdates += statusChange;
                    if (newFeedback.getUserName().equals(userName)){
                        newOwnResponses += responseChange;
                        newOwnVotes += voteChange;
                        ownStatusUpdates += statusUpdates;
                    }
                    //TODO [jfo] check for public/private feedback
                }
            }
        }

        StringBuilder message = new StringBuilder();
        if (newResponses > 0) {
            message.append("There are ");
            message.append(newResponses);
            message.append(" new responses on your subscribed feedback, thereof ");
            message.append(newOwnResponses);
            message.append(" off your own.");
            message.append('\n');
        }
        if (newVotes != 0) {
            message.append("There are ");
            message.append(newVotes);
            message.append(" new votes on your subscribed feedback, thereof ");
            message.append(newOwnVotes);
            message.append(" off your own.");
            message.append('\n');
        }
        if (statusUpdates > 0) {
            message.append("The status of ");
            message.append(statusUpdates);
            message.append(" off your subscribed feedback have changed, thereof ");
            message.append(ownStatusUpdates);
            message.append(" off your own.");
            message.append('\n');
        }
        if (visibilityUpdates > 0) {
            message.append("The visibility of ");
            message.append(visibilityUpdates);
            message.append(" off your feedback have changed.");
            message.append('\n');
        }

        return createNotification("Your subscribed feedback have received updates", message.toString(), context, configuration);
    }
}
