package com.rubn.monitoringapp.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.rubn.monitoringapp.R;
import com.rubn.monitoringapp.activities.MainActivity;
import com.rubn.monitoringapp.gcm.QuickstartPreferences;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Rubén on 12/10/2016.
 */

public class DiskService extends Service {

    private static final String TAG = "DiskService";
    private Timer timer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        getDisk(intent.getIntExtra("time", -1));
        return Service.START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    private void getDisk(int time) {
        int delay = 0;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                long diskFree, diskTotal;
                float diskPercentatge = 0.0f;
                StatFs statfs = new StatFs(Environment.getRootDirectory().getAbsolutePath());
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    diskFree = statfs.getFreeBytes();
                    diskTotal = statfs.getTotalBytes();
                    diskPercentatge = ((float) diskFree / (float) diskTotal) * 100.0f;
                }
                sentToBroadcast(diskPercentatge);
            }
        }, delay, time);
    }

    private void sentToBroadcast(float disk){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putFloat(ServicePreferences.DISK_SERVICE,disk).apply();
        Intent dataObtained = new Intent(ServicePreferences.DISK_SERVICE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(dataObtained);
    }

    private void sendNotification(String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle("Memory Service")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}
