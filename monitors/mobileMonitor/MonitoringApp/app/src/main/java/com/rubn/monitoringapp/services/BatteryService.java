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
import android.os.BatteryManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.rubn.monitoringapp.activities.MainActivity;
import com.rubn.monitoringapp.R;

import java.util.Timer;
import java.util.TimerTask;


public class BatteryService extends Service {

    private static final String TAG = "BatteryService";
    private Timer timer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "StartCommand Battery Service");
        getBattery(intent.getIntExtra("time", -1));
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

    private void getBattery(int time) {
        int delay = 0;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                float batteryPercentage;
                Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
                int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                if (level == -1 || scale == -1) {
                    batteryPercentage = 50.0f;
                } else {
                    batteryPercentage = ((float) level / (float) scale) * 100.0f;
                }
                sentToBroadcast(batteryPercentage);
            }
        }, delay, time);

    }
    private void sentToBroadcast(float battery){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putFloat(ServicePreferences.BATTERY_SERVICE,battery).apply();
        Intent dataObtained = new Intent(ServicePreferences.BATTERY_SERVICE);
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
