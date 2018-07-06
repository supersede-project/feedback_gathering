package ch.uzh.supersede.feedbacklibrary.services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.activities.FeedbackHubActivity;
import ch.uzh.supersede.feedbacklibrary.beans.LocalConfigurationBean;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;

public class NotificationBaseService extends Service {
    private NotificationCompat.Builder notification;
    private LocalConfigurationBean configuration;
    private int notificationId = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        configuration = (LocalConfigurationBean) intent.getSerializableExtra(EXTRA_KEY_APPLICATION_CONFIGURATION);
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

        //TODO [jfo] create async task
        execCreateNotification();
        execSendNotification();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    private void execCreateNotification() {
        Intent intent = new Intent(getApplicationContext(), FeedbackHubActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(EXTRA_KEY_HOST_APPLICATION_NAME, configuration.getHostApplicationName());
        intent.putExtra(EXTRA_KEY_APPLICATION_CONFIGURATION, configuration);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon_smile)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
    }

    private void execSendNotification() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId++, notification.build());
    }
}
