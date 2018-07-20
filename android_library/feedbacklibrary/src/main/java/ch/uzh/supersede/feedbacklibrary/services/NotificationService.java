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
import ch.uzh.supersede.feedbacklibrary.models.AuthenticateResponse;
import ch.uzh.supersede.feedbacklibrary.utils.*;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.UserConstants.USER_NAME;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.ADVANCED;

public class NotificationService extends Service implements IFeedbackServiceEventListener {
    private static final int MAX_FAIL_COUNT = 10;

    private LocalConfigurationBean configuration;
    private String userName;

    private int failCount = 0;
    private int notificationId = 0;

    private AsyncPoll asyncPoll;
    private Thread pollThread;

    @Override
    public void onCreate() {
        if (!ADVANCED.check(this)) {
            stopSelf();
            return;
        }

        if (configuration == null) {
            configuration = FeedbackDatabase.getInstance(this).readConfiguration();
        }
        userName = FeedbackDatabase.getInstance(this).readString(USER_NAME, null);

        if (CompareUtility.notNull(configuration, userName)) {
            execStartAsyncPolling(configuration);
        } else {
            Log.e(getClass().getSimpleName(), "service stopped due to a configuration error.");
            stopSelf();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            configuration = (LocalConfigurationBean) intent.getSerializableExtra(EXTRA_KEY_APPLICATION_CONFIGURATION);
        }
        Log.i(getClass().getSimpleName(), "service started.");
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * Create a never dying service by re-instantiating itself through a brocastReciever when the actual host app gets destroyed.
     */
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        execStopAsyncPolling();
        Intent broadcastIntent = new Intent(getResources().getString(R.string.notification_service_shutdown));
        Log.d(getClass().getSimpleName(), "Starting new broadcast with configuration=" + (configuration != null));
        sendBroadcast(broadcastIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        execStopAsyncPolling();
    }

    private void execStopAsyncPolling() {
        if (asyncPoll != null) {
            resetFailCount();
            asyncPoll.shutdown();
            pollThread = null;
            Log.i(getClass().getSimpleName(), "polling stopped.");
        } else {
            Log.d(getClass().getSimpleName(), "polling already stopped.");
        }
    }

    protected void execSendNotification(Notification notification) {
        if (notification != null) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            Log.d(this.getClass().getSimpleName(), "Notification #" + notificationId + " sent..");
            notificationManager.notify(notificationId++, notification);
        }
    }

    private void execStartAsyncPolling(LocalConfigurationBean config) {
        asyncPoll = new AsyncPoll(config);
        pollThread = new Thread(asyncPoll);
        pollThread.start();
    }

    /**
     * Executes the polling mechanism. The override of member variable configuration via the parameter config is necessary since, if ON_BOOT_COMPLETED triggers the BootCompletedReceiver creates a
     * new instance of this class which will cause a discrepancy between Service and AsyncPoll such that the LocalConfigurationBean cannot be stored, and thus is indeed null.
     *
     * @param config configuration that passed by Service through AsyncPoll
     * @see BootCompletedReceiver
     */
    void execPoll(LocalConfigurationBean config) {
        if (configuration == null) {
            configuration = config;
        }
        if (failCount > MAX_FAIL_COUNT) {
            resetFailCount();
            stopSelf();
            return;
        }
        FeedbackService.getInstance(this).getFeedbackSubscriptions(this,this);
        FeedbackService.getInstance(this).getUser(this, new AndroidUser(userName));
    }

    @Override
    public void onEventCompleted(IFeedbackServiceEventListener.EventType eventType, Object response) {
        switch (eventType) {
            case AUTHENTICATE:
                if (response instanceof AuthenticateResponse) {
                    FeedbackService.getInstance(this).setToken(((AuthenticateResponse) response).getToken());
                }
                FeedbackService.getInstance(this).setApplicationId(configuration.getHostApplicationLongId());
                FeedbackService.getInstance(this).setLanguage(configuration.getHostApplicationLanguage());
                break;
            case GET_FEEDBACK_SUBSCRIPTIONS:
                List<FeedbackDetailsBean> feedbackDetailsBeans = FeedbackUtility.transformFeedbackResponse(response, this);
                handleFeedbackSubscriptionUpdate(feedbackDetailsBeans);
                break;
            case GET_USER:
                if (response instanceof AndroidUser) {
                    handleUserUpdate((AndroidUser) response);
                }
                break;
            case GET_FEEDBACK_SUBSCRIPTIONS_MOCK:
                execSendNotification(NotificationUtility.getInstance(this).createDummyFeedbackUpdateNotification(this, configuration));
                break;
            case GET_USER_MOCK:
                execSendNotification(NotificationUtility.getInstance(this).createDummyUserUpdateNotification(this, configuration));
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
        switch (eventType) {
            case AUTHENTICATE:
                //FIXME [jfo] remove block with F2FA-80
                FeedbackService.getInstance(this).setToken(LIFETIME_TOKEN);
                FeedbackService.getInstance(this).setApplicationId(configuration.getHostApplicationLongId());
                FeedbackService.getInstance(this).setLanguage(configuration.getHostApplicationLanguage());
                break;
            default:
        }
        updateFailCount();
        Log.w(getClass().getSimpleName(), getResources().getString(R.string.api_service_event_failed, eventType, response.toString()));
    }

    @Override
    public void onConnectionFailed(IFeedbackServiceEventListener.EventType eventType) {
        updateFailCount();
        Log.w(getClass().getSimpleName(), getResources().getString(R.string.api_service_connection_failed, eventType));
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
    @SuppressWarnings("squid:S2209")
    public class AsyncPoll implements Runnable {
        private boolean isShutdown = false;
        private LocalConfigurationBean config;

        AsyncPoll(LocalConfigurationBean config) {
            this.config = config;
        }

        @Override
        public void run() {
            Thread thisThread = Thread.currentThread();
            while (pollThread == thisThread) {
                execPoll(config);
                try {
                    thisThread.sleep(DateUtility.minutesToMillis(config.getPullIntervalMinutes()));
                    synchronized (this) {
                        while (isShutdown && pollThread == thisThread) {
                            wait();
                        }
                    }
                } catch (InterruptedException e) {
                    Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
                    thisThread.interrupt();
                }
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
