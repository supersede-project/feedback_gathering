package ch.uzh.supersede.feedbacklibrary.services;

import android.content.*;
import android.util.Log;

import ch.uzh.supersede.feedbacklibrary.beans.LocalConfigurationBean;
import ch.uzh.supersede.feedbacklibrary.utils.ServiceUtility;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.EXTRA_KEY_APPLICATION_CONFIGURATION;

/**
 * Listens to broadcast events from the NotificationService and tries to restart i.e. re-instantiate it.
 */
public class NotificationServiceShutdownReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(this.getClass().getSimpleName(), "Restarting " + NotificationService.class.getSimpleName());
        ServiceUtility.startService(NotificationService.class, context);
    }
}
