package com.rubn.monitoringapp.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.rubn.monitoringapp.R;
import com.rubn.monitoringapp.activities.MainActivity;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Timer;
import java.util.TimerTask;


public class CpuService extends Service {

    private static final String TAG = "CPUService";
    private Timer timer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "StartCommand CPU Service");
        getCpu(intent.getIntExtra("time", -1));
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

    private void getCpu(int time) {
        int delay = 0;

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int i = 0;

            public void run() {
                float cpuPercentatge;
                try {
                    RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
                    String load = reader.readLine();

                    String[] toks = load.split(" +");  // Split on one or more spaces

                    long idle1 = Long.parseLong(toks[4]);
                    long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[5])
                            + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

                    try {
                        Thread.sleep(360);
                    } catch (Exception e) {
                    }

                    reader.seek(0);
                    load = reader.readLine();
                    reader.close();
                    toks = load.split(" +");
                    long idle2 = Long.parseLong(toks[4]);
                    long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[5])
                            + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);
                    cpuPercentatge = ((float) (cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1))) * 100.0f;
                    sentToBroadcast(cpuPercentatge);
                    i++;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, delay, time);
    }
    private void sentToBroadcast(float cpu){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putFloat(ServicePreferences.CPU_SERVICE,cpu).apply();
        Intent dataObtained = new Intent(ServicePreferences.CPU_SERVICE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(dataObtained);
    }
    private void sendNotification(String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
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

        notificationManager.notify(0 , notificationBuilder.build());
    }
}
