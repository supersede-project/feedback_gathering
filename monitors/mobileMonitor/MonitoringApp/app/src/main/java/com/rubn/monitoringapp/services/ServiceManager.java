package com.rubn.monitoringapp.services;

import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.rubn.monitoringapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rubén on 13/10/2016.
 */

public class ServiceManager {

    private static final String TAG = "ServiceManager";
    private static ServiceManager mServiceManager;
    private static Intent intentMemory;
    private static Intent intentBattery;
    private static Intent intentNetwork;
    private static Intent intentDisk;
    private static Intent intentCpu;

    private static Context mContext;

    public static ServiceManager getInstance(Context context) {
        if (mServiceManager == null) {
            mServiceManager = new ServiceManager();
        }
        mContext = context;

        Log.i(TAG, "Intent Memory Check null: " + (intentMemory == null));

        return mServiceManager;
    }

    public ServiceManager() {
    }

    public void callServices(Bundle data) {
        stopServices();
        try {
            JSONObject jsonObj = new JSONObject(data.getString("message"));


            String endpoint = jsonObj.getString("endpoint");
            String topic = jsonObj.getString("topic");
            String packageName = jsonObj.getString("packageName");
            //***********************************************//
            // packageName example = com.rubn.monitoringapp  //
            //***********************************************//

            int time = jsonObj.getInt("time");
            JSONArray jsonMetrics = jsonObj.getJSONArray("metrics");
            String metricsConfigs = "";
            for (int i = 0; i < jsonMetrics.length(); i++) {
                JSONObject jsonIndObj = jsonMetrics.getJSONObject(i);
                metricsConfigs += jsonIndObj.getString("metricName");
            }
            if (metricsConfigs.length()> 0)
                sendToBroadcast(metricsConfigs, endpoint, topic);

            for (int i = 0; i < jsonMetrics.length(); i++) {
                JSONObject jsonIndObj = jsonMetrics.getJSONObject(i);
                String metricName = jsonIndObj.getString("metricName");

                switch (metricName) {
                    case "memory":
                        intentMemory = new Intent(mContext, MemoryService.class);
                        intentMemory.putExtra(mContext.getResources().getString(R.string.metrics_time), time);
                        intentMemory.putExtra(mContext.getResources().getString(R.string.package_name), packageName);
                        mContext.startService(intentMemory);
                        break;
                    case "battery":
                        intentBattery = new Intent(mContext, BatteryService.class);
                        intentBattery.putExtra(mContext.getResources().getString(R.string.metrics_time), time);
                        mContext.startService(intentBattery);
                        break;
                    case "cpu":
                        intentCpu = new Intent(mContext, CpuService.class);
                        intentCpu.putExtra(mContext.getResources().getString(R.string.metrics_time), time);
                        mContext.startService(intentCpu);
                        break;
                    case "network":
                        intentNetwork = new Intent(mContext, NetworkService.class);
                        intentNetwork.putExtra(mContext.getResources().getString(R.string.metrics_time), time);
                        intentNetwork.putExtra(mContext.getResources().getString(R.string.package_name), packageName);
                        mContext.startService(intentNetwork);
                        break;
                    case "disk":
                        intentDisk = new Intent(mContext, DiskService.class);
                        intentDisk.putExtra(mContext.getResources().getString(R.string.metrics_time), time);
                        mContext.startService(intentDisk);
                        break;
                    default:
                        Log.i(TAG, "It isn't in the metric list!");
                        break;
                }
            }
        } catch (JSONException e) {
            System.out.println("No metrics format");
            e.printStackTrace();
        }
    }

    private void sendToBroadcast(String metricsConfig, String endpoint, String topic) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        sharedPreferences.edit().putString(ServicePreferences.SERVICE_MANAGER_CONFIG, metricsConfig).apply();
        sharedPreferences.edit().putString(ServicePreferences.SERVICE_MANAGER_ENDPOINT, endpoint).apply();
        sharedPreferences.edit().putString(ServicePreferences.SERVICE_MANAGER_TOPIC, topic).apply();
        Intent dataObtained = new Intent(ServicePreferences.SERVICE_MANAGER);

        LocalBroadcastManager.getInstance(mContext).sendBroadcast(dataObtained);
    }

    private void stopServices() {
        if (intentBattery != null)
            mContext.stopService(intentBattery);
        if (intentMemory != null)
            mContext.stopService(intentMemory);
        if (intentDisk != null)
            mContext.stopService(intentDisk);
        if (intentNetwork != null)
            mContext.stopService(intentNetwork);
        if (intentCpu != null)
            mContext.stopService(intentCpu);
    }
}
