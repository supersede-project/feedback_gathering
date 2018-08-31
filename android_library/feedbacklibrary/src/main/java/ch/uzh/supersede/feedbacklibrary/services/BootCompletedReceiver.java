package ch.uzh.supersede.feedbacklibrary.services;

import android.content.*;
import android.util.Log;

import ch.uzh.supersede.feedbacklibrary.utils.ServiceUtility;

/**
 * Listens to broadcast events from phone boot and tries to restart i.e. re-instantiate the NotificationService.
 */
public final class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Log.i(this.getClass().getSimpleName(), "Starting " + NotificationService.class.getSimpleName() + " on boot.");
            ServiceUtility.startService(NotificationService.class, context);
        }
    }
}
