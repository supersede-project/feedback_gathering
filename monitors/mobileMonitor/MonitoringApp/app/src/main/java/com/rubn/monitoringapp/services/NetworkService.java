package com.rubn.monitoringapp.services;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.rubn.monitoringapp.R;
import com.rubn.monitoringapp.activities.MainActivity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Rubén on 12/10/2016.
 */

public class NetworkService extends Service {

    private static final String TAG = "NetworkService";
    private Timer timer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "StartCommand Network Service");
        getNetwork(intent.getIntExtra(getResources().getString(R.string.metrics_time), -1), intent.getStringExtra(getResources().getString(R.string.package_name)));
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

    private void getNetwork(int time, final String packageName) {
        int delay = 0;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                long mBReceived = TrafficStats.getTotalRxBytes() / 1048576L;
                long mBTransmitted = TrafficStats.getTotalTxBytes() / 1048576L;
                long mBPackageReceived = 0L;
                long mBPackageTransmitted = 0L;

                PackageManager packageManager = getPackageManager();
                List<ApplicationInfo> packages = packageManager.getInstalledApplications(0);
                for (ApplicationInfo app: packages){
                    if (app.processName.equals(packageName)){
                        int uid = app.uid;
                        mBPackageReceived = TrafficStats.getUidRxBytes(uid)/ 1048576L;
                        mBPackageTransmitted = TrafficStats.getUidTxBytes(uid)/ 1048576L;
                        break;
                    }
                }
                sentToBroadcast(mBTransmitted,mBReceived, mBPackageTransmitted,mBPackageReceived);
            }
        }, delay, time);
    }

    private void sentToBroadcast(long netSent, long netReceiv, long netPackSent, long netPackReceiv){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putBoolean(ServicePreferences.NETWORK_SERVICE,true).apply();
        sharedPreferences.edit().putLong(ServicePreferences.NETWORK_SERVICE_SENT,netSent).apply();
        sharedPreferences.edit().putLong(ServicePreferences.NETWORK_SERVICE_RECEIVED, netReceiv).apply();
        sharedPreferences.edit().putLong(ServicePreferences.NETWORK_SERVICE_PACKAGE_SENT, netPackSent).apply();
        sharedPreferences.edit().putLong(ServicePreferences.NETWORK_SERVICE_PACKAGE_RECEIVED, netPackReceiv).apply();
        Intent dataObtained = new Intent(ServicePreferences.NETWORK_SERVICE);
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
