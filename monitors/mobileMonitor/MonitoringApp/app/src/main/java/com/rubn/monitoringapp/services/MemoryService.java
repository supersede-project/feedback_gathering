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
import android.net.Uri;
import android.os.Debug;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.rubn.monitoringapp.activities.MainActivity;
import com.rubn.monitoringapp.R;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Rubén on 11/10/2016.
 */

public class MemoryService extends Service {

    private static final String TAG = "MemoryService";
    private Timer timer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG, "InsideStartCommand MemoryService");
        getMemory(intent.getIntExtra(getResources().getString(R.string.metrics_time), -1), intent.getStringExtra(getResources().getString(R.string.package_name)));
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

    private void getMemory(int time, final String  packageName){
        int delay = 0;
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask()
        {
            public void run()
            {
                float totalPss = 0f;
                float totalSharedDirty = 0f;
                float totalPrivDirty = 0f;
                ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
                ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                activityManager.getMemoryInfo(mi);

                PackageManager packageManager = getPackageManager();
                List<ApplicationInfo> packages = packageManager.getInstalledApplications(0);
                for (ApplicationInfo app: packages){
                    if (app.processName.equals(packageName)){
                        int[] uids = new int[1];
                        uids[0]=app.uid;
                        Debug.MemoryInfo[] procesMemInfo = activityManager.getProcessMemoryInfo(uids);
                        totalPss = procesMemInfo[0].getTotalPss();
                        totalSharedDirty = procesMemInfo[0].getTotalSharedDirty();
                        totalPrivDirty = procesMemInfo[0].getTotalPrivateDirty();
                        break;
                    }
                }
                float availableMegsPercentage = ((float)mi.availMem / (float)mi.totalMem)*100f;
                sentToBroadcast(availableMegsPercentage, totalPss, totalSharedDirty, totalPrivDirty);
            }
        }, delay, time);
    }

    private void sentToBroadcast(float memory, float memoryPss, float memorySharedDirty, float memoryPrivDirty){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.edit().putFloat(ServicePreferences.MEMORY_SERVICE,memory).apply();
        sharedPreferences.edit().putFloat(ServicePreferences.MEMORY_SERVICE_PSS, memoryPss).apply();
        sharedPreferences.edit().putFloat(ServicePreferences.MEMORY_SERVICE_SHARED_DIRTY, memorySharedDirty).apply();
        sharedPreferences.edit().putFloat(ServicePreferences.MEMORY_SERVICE_PRIVATE_DIRTY, memoryPrivDirty).apply();
        Intent dataObtained = new Intent(ServicePreferences.MEMORY_SERVICE);
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
