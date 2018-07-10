package ch.uzh.supersede.feedbacklibrary.services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.List;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.beans.FeedbackDetailsBean;
import ch.uzh.supersede.feedbacklibrary.beans.LocalConfigurationBean;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.models.AndroidUser;
import ch.uzh.supersede.feedbacklibrary.utils.*;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.EXTRA_KEY_APPLICATION_CONFIGURATION;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.USER_NAME;

public class NotificationService extends Service implements IFeedbackServiceEventListener {
    private static final long POLL_SLEEP_TIME = 3000; // milliseconds //TODO [jfo] set to reasonable interval
    private static final int MAX_FAIL_COUNT = 10;

    private LocalConfigurationBean configuration;
    private String userName;

    private int failCount = 0;
    private int notificationId = 0;

    private AsyncPoll asyncPoll;
    private Thread pollThread;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        configuration = (LocalConfigurationBean) intent.getSerializableExtra(EXTRA_KEY_APPLICATION_CONFIGURATION);
        userName = FeedbackDatabase.getInstance(this).readString(USER_NAME, null);

        Log.i(getClass().getSimpleName(), "service started.");
        execStartAsyncPolling();
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

    protected void execSendNotification(Notification notification) {
        if (notification != null) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            Log.d(this.getClass().getSimpleName(), "Notification #" + notificationId + " sent..");
            notificationManager.notify(notificationId++, notification);
        }
    }

    private void execStartAsyncPolling() {
        asyncPoll = new AsyncPoll();
        pollThread = new Thread(asyncPoll);
        pollThread.start();
    }

    void execPoll() {
        if (failCount > MAX_FAIL_COUNT) {
            resetFailCount();
            ServiceUtility.stopService(NotificationService.class, this);
            return;
        }
        FeedbackService.getInstance(this).getFeedbackList(this, null, configuration, 0);
        FeedbackService.getInstance(this).getUser(this, new AndroidUser(userName));
    }

    @Override
    public void onEventCompleted(IFeedbackServiceEventListener.EventType eventType, Object response) {
        switch (eventType) {
            case GET_FEEDBACK_SUBSCRIPTIONS:
                List<FeedbackDetailsBean> feedbackDetailsBeans = FeedbackUtility.transformFeedbackResponse(response, this);
                handleFeedbackSubscriptionUpdate(feedbackDetailsBeans);
                break;
            case GET_USER:
                if (response instanceof AndroidUser) {
                    handleUserUpdate((AndroidUser) response);
                }
                break;
            default:
                break;
        }
    }

    private void handleFeedbackSubscriptionUpdate(List<FeedbackDetailsBean> newFeedbackDetailsBeans) {
        List<Notification> notifications = NotificationUtility.getInstance(this).createFeedbackUpdateNotification(newFeedbackDetailsBeans, this, configuration, true);
        for (Notification notification : notifications) {
            execSendNotification(notification);
        }
    }

    private void handleUserUpdate(AndroidUser androidUser) {
        List<Notification> notifications = NotificationUtility.getInstance(this).createUserUpdateNotification(androidUser, this, configuration, true);
        for (Notification notification : notifications) {
            execSendNotification(notification);
        }
    }

    @Override
    public void onEventFailed(IFeedbackServiceEventListener.EventType eventType, Object response) {
        updateFailCount();
        Log.e(getClass().getSimpleName(), getResources().getString(R.string.api_service_event_failed, eventType, response.toString()));
    }

    @Override
    public void onConnectionFailed(IFeedbackServiceEventListener.EventType eventType) {
        updateFailCount();
        Log.e(getClass().getSimpleName(), getResources().getString(R.string.api_service_connection_failed, eventType));
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
                    pollThread.sleep(POLL_SLEEP_TIME); //NOSONAR
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
