package ch.uzh.supersede.feedbacklibrary.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ch.uzh.supersede.feedbacklibrary.beans.LocalConfigurationBean;
import ch.uzh.supersede.feedbacklibrary.utils.ServiceUtility;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.EXTRA_KEY_APPLICATION_CONFIGURATION;

/**
 * Listens to broadcast events from the NotificationService and tries to restart i.e. re-instanciate it.
 */
public class NotificationServiceBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(this.getClass().getSimpleName(), "Restarting " + NotificationService.class.getSimpleName());
        LocalConfigurationBean configuration = (LocalConfigurationBean) intent.getSerializableExtra(EXTRA_KEY_APPLICATION_CONFIGURATION);
        ServiceUtility.startService(NotificationService.class, context, new ServiceUtility.Extra(EXTRA_KEY_APPLICATION_CONFIGURATION, configuration));
    }
}
