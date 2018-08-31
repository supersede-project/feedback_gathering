package ch.uzh.supersede.feedbacklibrary.services;

import android.content.*;
import android.util.Log;

import ch.uzh.supersede.feedbacklibrary.utils.ServiceUtility;

/**
 * Listens to broadcast events from the NotificationService and tries to restart i.e. re-instantiate it.
 */
public final class NotificationServiceShutdownReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(this.getClass().getSimpleName(), "Restarting " + NotificationService.class.getSimpleName());
        ServiceUtility.startService(NotificationService.class, context);
    }
}
