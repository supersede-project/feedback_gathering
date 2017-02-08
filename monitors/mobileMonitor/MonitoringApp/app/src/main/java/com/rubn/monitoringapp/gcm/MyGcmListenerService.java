package com.rubn.monitoringapp.gcm;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.rubn.monitoringapp.activities.MainActivity;
import com.rubn.monitoringapp.R;
import com.rubn.monitoringapp.services.BatteryService;
import com.rubn.monitoringapp.services.CpuService;
import com.rubn.monitoringapp.services.DiskService;
import com.rubn.monitoringapp.services.MemoryService;
import com.rubn.monitoringapp.services.NetworkService;
import com.rubn.monitoringapp.services.ServiceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.locks.Lock;

/**
 * Created by Rubén on 20/09/2016.
 */
public class MyGcmListenerService extends GcmListenerService {
    private static final String TAG = "MyGcmListenerService";
    private static boolean allow = true;
    //private int iprova = 0;

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {

        if(allow) {
            allow = false;
            String message = data.getString("message");
            Log.i(TAG, "From: " + from);
            Log.i(TAG, "Message: " + message);
            Log.i(TAG, "El valor del permiso es:" + allow);

            ServiceManager mServiceManager = ServiceManager.getInstance(this);
            mServiceManager.callServices(data);

            sendNotification(message);
        }
        else
            allow = true;
    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle("GCM Message")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
