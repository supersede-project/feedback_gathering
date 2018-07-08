package ch.uzh.supersede.feedbacklibrary.services;

import android.app.*;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.activities.FeedbackHubActivity;
import ch.uzh.supersede.feedbacklibrary.beans.LocalConfigurationBean;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.models.AndroidUser;
import ch.uzh.supersede.feedbacklibrary.utils.ServiceUtility;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;

public class NotificationService extends Service implements IFeedbackServiceEventListener {
    private static final long POLL_SLEEP_TIME = 3000; // milliseconds //TODO [jfo] set to reasonable interval
    private static final int MAX_FAIL_COUNT = 10;
    private LocalConfigurationBean configuration;
    private static int notificationId = 0;
    private static int failCount = 0;
    private AsyncPoll asyncPoll;
    private Thread pollThread;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        configuration = (LocalConfigurationBean) intent.getSerializableExtra(EXTRA_KEY_APPLICATION_CONFIGURATION);
        Log.i(getClass().getSimpleName(), "service started.");
        execAsyncPolling();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Create a never dyeing service by re-instantiating itself through a brocastReciever when the actual host app gets destroyed.
     */
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Intent broadcastIntent = new Intent(getResources().getString(R.string.notification_service_shutdown));
        broadcastIntent.putExtra(EXTRA_KEY_APPLICATION_CONFIGURATION, configuration);
        sendBroadcast(broadcastIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        resetFailCount();
        if (asyncPoll != null) {
            asyncPoll.shutdown();
            pollThread = null;
        }
        Log.i(getClass().getSimpleName(), "service stopped.");
    }

    protected Notification createNotification(String title, String message) {
        Intent intent = new Intent(getApplicationContext(), FeedbackHubActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(EXTRA_KEY_HOST_APPLICATION_NAME, configuration.getHostApplicationName());
        intent.putExtra(EXTRA_KEY_APPLICATION_CONFIGURATION, configuration);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_star_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
    }

    protected void execSendNotification(Notification notification) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        Log.d(this.getClass().getSimpleName(), "Notification #" + notificationId + " sent..");
        notificationManager.notify(notificationId++, notification);
    }

    private void execAsyncPolling() {
        asyncPoll = new AsyncPoll();
        pollThread = new Thread(asyncPoll);
        pollThread.start();
    }

    enum NotificationEvent {
        FEEDBACK_STATUS_NOTIFICATION,
        ACHIEVEMENT_NOTIFICATION,
        FEEDBACK_RESPONSE_NOTIFICATION,
        FEEDBACK_VOTE_NOTIFICATION,
        FEEDBACK_VISIBILITY_NOTIFICATION
    }

    void execPoll() {
        if (failCount > MAX_FAIL_COUNT) {
            resetFailCount();
            ServiceUtility.stopService(NotificationService.class, getApplicationContext());
            return;
        }
        switch (NotificationEvent.values()[notificationId % NotificationEvent.values().length]) {
            case FEEDBACK_STATUS_NOTIFICATION:
                // TODO [jfo] getSubscribedFeedback and check for status changes
                FeedbackService.getInstance(this).getFeedbackList(this, null, configuration, 0);
                break;
            case ACHIEVEMENT_NOTIFICATION:
                // TODO [jfo] getUser and check for karma changes
                String userName = FeedbackDatabase.getInstance(this).readString(USER_NAME, null);
                boolean isDeveloper = FeedbackDatabase.getInstance(this).readBoolean(IS_DEVELOPER, false);
                FeedbackService.getInstance(this).getUser(this, new AndroidUser(userName, isDeveloper));
                break;
            case FEEDBACK_RESPONSE_NOTIFICATION:
                // TODO [jfo] getSubscribedFeedback and check for status changes
                break;
            case FEEDBACK_VOTE_NOTIFICATION:
                // TODO [jfo] getSubscribedFeedback and check for vote changes
                break;
            case FEEDBACK_VISIBILITY_NOTIFICATION:
                // TODO [jfo] getMine & SubscribedFeedback and check for visinility changes
                break;
            default:
                break;
        }
    }

    @Override
    public void onEventCompleted(IFeedbackServiceEventListener.EventType eventType, Object response) {
        switch (eventType) {
            case GET_MINE_FEEDBACK_VOTES:
                handleMineFeedbackVotesUpdate();
                break;
            case GET_FEEDBACK_SUBSCRIPTIONS:
                handleFeedbackSubscriptionUpdate();
                break;
            case GET_USER:
                handleUserUpdate();
                break;
            default:
                break;
        }
    }

    private void handleMineFeedbackVotesUpdate() {
        //TODO [jfo] implement update notification
        Notification notification = createNotification("New Votes have arrived for your Feedback", "dummy votes message..");
        execSendNotification(notification);
    }

    private void handleFeedbackSubscriptionUpdate() {
        //TODO [jfo] implement update notification
        Notification notification = createNotification("Your subscribed Feedback have received updates", "dummy subscription message..");
        execSendNotification(notification);
    }

    private void handleUserUpdate() {
        //TODO [jfo] implement update notification
        Notification notification = createNotification("Your user has received new updates", "dummy user message..");
        execSendNotification(notification);
    }

    @Override
    public void onEventFailed(IFeedbackServiceEventListener.EventType eventType, Object response) {
        updateFailCount();
        Log.e(getClass().getSimpleName(), "Event " + eventType + " failed: " + response.toString());
    }

    @Override
    public void onConnectionFailed(IFeedbackServiceEventListener.EventType eventType) {
        updateFailCount();
        Log.e(getClass().getSimpleName(), "Event " + eventType + " failed: server is not available.");
    }

    private synchronized void updateFailCount() {
        failCount++;
    }

    private synchronized void resetFailCount() {
        failCount = 0;
    }

    /**
     * Async task that creates a new thread which periodically calls ansync functions of enclosing class.
     * Runs forever until destory of enclosing class or calling task.shutdown().
     */
    public class AsyncPoll implements Runnable {
        private boolean isShutdown = false;

        @Override
        public void run() {
            Thread thisThread = Thread.currentThread();
            while (pollThread == thisThread) {
                try {
                    thisThread.sleep(POLL_SLEEP_TIME);
                    synchronized (this) {
                        while (isShutdown && pollThread == thisThread) {
                            wait();
                        }
                    }
                } catch (InterruptedException e) {
                    Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
                    thisThread.interrupt();
                }
                execPoll();
            }
        }

        synchronized void shutdown() {
            resetFailCount();
            asyncPoll = null;
            isShutdown = true;
            synchronized (this) {
                notifyAll();
            }
        }
    }
}
