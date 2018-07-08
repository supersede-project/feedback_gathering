package ch.uzh.supersede.feedbacklibrary.services;

import android.content.*;
import android.util.Log;

public class BootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(this.getClass().getSimpleName(), "Starting " + NotificationService.class.getSimpleName() +  " on boot.");
        //TODO [jfo] read configuration from database and start service, iff enabled
    }
}
