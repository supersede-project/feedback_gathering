package ch.uzh.supersede.feedbacklibrary.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.ArrayList;
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
        if (message.isEmpty()) {
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

    public List<Notification> createUserUpdateNotification(AndroidUser androidUser, Context context, LocalConfigurationBean configuration, boolean isSummary) {
        if (isSummary) {
            List<Notification> notifications = new ArrayList<>();
            notifications.add(createUserUpdateNotificationSummary(androidUser, context, configuration));
            return notifications;
        }
        return createUserUpdateNotifications(androidUser, context, configuration);
    }

    private List<Notification> createUserUpdateNotifications(AndroidUser androidUser, Context context, LocalConfigurationBean configuration) {
        throw new UnsupportedOperationException("not yet implemented.");
    }

    private Notification createUserUpdateNotificationSummary(AndroidUser androidUser, Context context, LocalConfigurationBean configuration) {
        StringBuilder message = new StringBuilder();
        if (androidUser.isBlocked() != userIsBlocked) {
            int resource = androidUser.isBlocked() ? R.string.notification_user_blocked : R.string.notification_user_unblocked;
            message.append(context.getResources().getString(resource));
            message.append('\n');
            userIsBlocked = androidUser.isBlocked();
            FeedbackDatabase.getInstance(context).writeBoolean(USER_IS_BLOCKED, userIsBlocked);
        }
        if (androidUser.getKarma() != userKarma) {
            int increase = userKarma - androidUser.getKarma();
            int resource = increase > 0 ? R.string.notification_user_karma_increased : R.string.notification_user_karma_decreased;
            message.append(context.getResources().getString(resource, increase));
            message.append('\n');
            userKarma = androidUser.getKarma();
            FeedbackDatabase.getInstance(context).writeInteger(USER_KARMA, userKarma);
        }
        if (androidUser.isDeveloper() != userIsDeveloper) {
            int resource = androidUser.isDeveloper() ? R.string.notification_user_developer : R.string.notification_user_not_developer;
            message.append(context.getResources().getString(resource));
            message.append('\n');
            userIsDeveloper = androidUser.isDeveloper();
            FeedbackDatabase.getInstance(context).writeBoolean(USER_IS_DEVELOPER, userIsDeveloper);
        }
        if (!androidUser.getName().equals(userName)) {
            userName = androidUser.getName();
            message.append(context.getResources().getString(R.string.notification_user_name_changed, userName));
            message.append('\n');
            FeedbackDatabase.getInstance(context).writeString(USER_NAME, userName);
        }
        return createNotification(context.getResources().getString(R.string.notification_user_title), message.toString(), context, configuration);
    }

    public List<Notification> createFeedbackUpdateNotification(List<FeedbackDetailsBean> newFeedbackDetailsBeans, Context context, LocalConfigurationBean configuration, boolean isSummary) {
        if (isSummary) {
            List<Notification> notifications = new ArrayList<>();
            notifications.add(createFeedbackUpdateNotificationSummary(newFeedbackDetailsBeans, context, configuration));
            return notifications;
        }
        return createFeedbackUpdateNotifications(newFeedbackDetailsBeans, context, configuration);
    }

    private List<Notification> createFeedbackUpdateNotifications(List<FeedbackDetailsBean> newFeedbackDetailsBeans, Context context, LocalConfigurationBean configuration) {
        throw new UnsupportedOperationException("not yet implemented.");
    }

    private Notification createFeedbackUpdateNotificationSummary(List<FeedbackDetailsBean> newFeedbackDetailsBeans, Context context, LocalConfigurationBean configuration) {
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
                    if (newFeedback.getUserName().equals(userName)) {
                        newOwnResponses += responseChange;
                        newOwnVotes += voteChange;
                        ownStatusUpdates += statusUpdates;
                    }
                    //TODO [jfo] check for public/private feedback
                    FeedbackDatabase.getInstance(context).writeFeedback(newFeedback.getFeedbackBean(), Enums.SAVE_MODE.SUBSCRIBED);
                }
            }
        }

        StringBuilder message = new StringBuilder();
        append(message, context, R.string.notification_feedback_responses, newResponses, R.string.notification_feedback_own, newOwnResponses);
        append(message, context, R.string.notification_feedback_votes, newVotes, R.string.notification_feedback_own, newOwnVotes);
        append(message, context, R.string.notification_feedback_status, statusUpdates, R.string.notification_feedback_own, ownStatusUpdates);
        append(message, context, R.string.notification_feedback_visibility, visibilityUpdates);

        return createNotification(context.getResources().getString(R.string.notification_feedback_title), message.toString(), context, configuration);
    }

    private void append(StringBuilder message, Context context, Integer resource, Integer value) {
        append(message, context, resource, value, null, null);
    }

    private void append(StringBuilder message, Context context, Integer resource, Integer value, Integer resourceOwn, Integer valueOwn) {
        if (resource == null) {
            return;
        }
        if (value != 0) {
            message.append(context.getResources().getString(resource, value));
            if (valueOwn != null && resourceOwn != null && valueOwn != 0) {
                message.append(context.getResources().getString(resourceOwn, valueOwn));
            }
            message.append('\n');
        }
    }
}
