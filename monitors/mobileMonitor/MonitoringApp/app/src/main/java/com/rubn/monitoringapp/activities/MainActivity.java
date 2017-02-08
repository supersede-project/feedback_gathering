package com.rubn.monitoringapp.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.rubn.monitoringapp.R;
import com.rubn.monitoringapp.gcm.QuickstartPreferences;
import com.rubn.monitoringapp.gcm.RegistrationIntentService;
import com.rubn.monitoringapp.retrofit.ConfigApi;
import com.rubn.monitoringapp.services.ServicePreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements Callback<JSONObject> {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final int numMaxServices = 5;
    private static final String TAG = "MainActivity";

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private BroadcastReceiver mServiceBroadcastReceiver;
    private TextView mIntroductionTextView;
    private TextView mTokenTextView;
    private boolean isReceiverRegistered;

    private ArrayList<Boolean> mServicesConfig = new ArrayList<Boolean>(numMaxServices);
    private ArrayList<Boolean> mServicesConfigValues;

    private JSONObject jsonKafka;
    private JSONArray jsonKafkaMetrics;

    private String endPoint = "";
    private String topic = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                RegistrationBroadcast(context);
            }
        };
        mServiceBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ServiceBroadcast(context, intent);
            }
        };

        mIntroductionTextView = (TextView) findViewById(R.id.helloWorldText);
        mTokenTextView = (TextView) findViewById(R.id.tokenText);

        // Registering BroadcastReceiver
        registerReceiver();

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    private void ServiceBroadcast(Context context, Intent intent) {
        try {

            SharedPreferences sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(context);
            JSONObject jsonMetric;
            switch (intent.getAction()) {
                case ServicePreferences.SERVICE_MANAGER:
                    setServices(sharedPreferences.getString(ServicePreferences.SERVICE_MANAGER_CONFIG, ""));
                    endPoint = sharedPreferences.getString(ServicePreferences.SERVICE_MANAGER_ENDPOINT, "");
                    topic = sharedPreferences.getString(ServicePreferences.SERVICE_MANAGER_TOPIC, "");
                    clearServicePreferences(context);
                    mServicesConfigValues = new ArrayList<Boolean>(Collections.nCopies(numMaxServices, false));
                    jsonKafka = new JSONObject();
                    jsonKafkaMetrics = new JSONArray();
                    jsonKafka.put("idDevice", Settings.Secure.ANDROID_ID);
                    break;
                case ServicePreferences.BATTERY_SERVICE:
                    jsonMetric = new JSONObject();
                    jsonMetric.put("name", "battery");
                    jsonMetric.put("usage", sharedPreferences.getFloat(ServicePreferences.BATTERY_SERVICE,0.0f));
                    jsonKafkaMetrics.put(jsonMetric);
                    mServicesConfigValues.set(0,true);
                    if (checkServicesFinish())
                        sentToKafka();
                    break;
                case ServicePreferences.CPU_SERVICE:
                    jsonMetric = new JSONObject();
                    jsonMetric.put("name", "cpu");
                    jsonMetric.put("available", sharedPreferences.getFloat(ServicePreferences.CPU_SERVICE,0.0f));
                    jsonKafkaMetrics.put(jsonMetric);
                    mServicesConfigValues.set(1,true);
                    if (checkServicesFinish())
                        sentToKafka();
                    break;
                case ServicePreferences.MEMORY_SERVICE:
                    jsonMetric = new JSONObject();
                    jsonMetric.put("name", "memory");
                    jsonMetric.put("available", sharedPreferences.getFloat(ServicePreferences.MEMORY_SERVICE,0.0f));
                    jsonMetric.put("package Total PSS", sharedPreferences.getFloat(ServicePreferences.MEMORY_SERVICE_PSS, 0.0f));
                    jsonMetric.put("package Total Shared Dirty", sharedPreferences.getFloat(ServicePreferences.MEMORY_SERVICE_SHARED_DIRTY, 0.0f));
                    jsonMetric.put("package Total Private Dirty", sharedPreferences.getFloat(ServicePreferences.MEMORY_SERVICE_PRIVATE_DIRTY, 0.0f));
                    jsonKafkaMetrics.put(jsonMetric);
                    mServicesConfigValues.set(2,true);
                    if (checkServicesFinish())
                        sentToKafka();
                    break;
                case ServicePreferences.DISK_SERVICE:
                    jsonMetric = new JSONObject();
                    jsonMetric.put("name", "disk");
                    jsonMetric.put("available", sharedPreferences.getFloat(ServicePreferences.DISK_SERVICE,0.0f));
                    jsonKafkaMetrics.put(jsonMetric);
                    mServicesConfigValues.set(3,true);
                    if (checkServicesFinish())
                        sentToKafka();
                    break;
                case ServicePreferences.NETWORK_SERVICE:
                    jsonMetric = new JSONObject();
                    jsonMetric.put("name", "network");
                    jsonMetric.put("transmitted", sharedPreferences.getLong(ServicePreferences.NETWORK_SERVICE_SENT, 0));
                    jsonMetric.put("received", sharedPreferences.getLong(ServicePreferences.NETWORK_SERVICE_RECEIVED, 0));
                    jsonMetric.put("package transmitted", sharedPreferences.getLong(ServicePreferences.NETWORK_SERVICE_PACKAGE_SENT,0));
                    jsonMetric.put("package received", sharedPreferences.getLong(ServicePreferences.NETWORK_SERVICE_PACKAGE_RECEIVED,0));
                    jsonKafkaMetrics.put(jsonMetric);
                    mServicesConfigValues.set(4,true);
                    if (checkServicesFinish())
                        sentToKafka();
                    break;
                default:
                    Log.i(TAG, "Not a monitoring Service");
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean checkServicesFinish() {

        for(int i = 0; i < mServicesConfigValues.size(); i++){
            if(mServicesConfig.get(i) != mServicesConfigValues.get(i))
                return false;
        }
        return true;
    }

    private void sentToKafka() {
        JSONObject jsonSentKafka = new JSONObject();
        try {
            jsonSentKafka.put("endpoint",endPoint);
            jsonSentKafka.put("topic",topic);
            jsonSentKafka.put("timeStamp", new Date());
            int numDataItems = 0;
            for (int i = 0; i < mServicesConfig.size(); i++)
                numDataItems += (mServicesConfig.get(i)) ? 1 : 0;
            jsonSentKafka.put("numDataItems",numDataItems);
            jsonSentKafka.put("metrics",jsonKafkaMetrics);
            mServicesConfigValues = new ArrayList<Boolean>(Collections.nCopies(numMaxServices, false));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getResources().getString(R.string.sever_url)+"monitoring_kafka/rest/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ConfigApi configAPI = retrofit.create(ConfigApi.class);
        Call<JSONObject> call = configAPI.sendDataToKafka(jsonSentKafka);
        call.enqueue(this);
        jsonKafkaMetrics = new JSONArray();
    }
    @Override
    public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
    }

    @Override
    public void onFailure(Call<JSONObject> call, Throwable t) {
    }

    private void RegistrationBroadcast(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        boolean sentToken = sharedPreferences
                .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);

        if (sentToken) {
            mIntroductionTextView.setText(R.string.gcm_registered);
            /*mTokenTextView.setText(sharedPreferences
                    .getString(QuickstartPreferences.TOKEN_REGISTRATION, ""));*/
        } else {
            mIntroductionTextView.setText(R.string.gcm_error_register);
        }
    }

    private void clearServicePreferences(Context context) {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putFloat(ServicePreferences.BATTERY_SERVICE, 0.0f).apply();
        sharedPreferences.edit().putFloat(ServicePreferences.CPU_SERVICE, 0.0f).apply();
        sharedPreferences.edit().putFloat(ServicePreferences.DISK_SERVICE, 0.0f).apply();
        sharedPreferences.edit().putFloat(ServicePreferences.MEMORY_SERVICE, 0.0f).apply();
        sharedPreferences.edit().putBoolean(ServicePreferences.NETWORK_SERVICE, false).apply();
        sharedPreferences.edit().putLong(ServicePreferences.NETWORK_SERVICE_SENT, 0).apply();
        sharedPreferences.edit().putLong(ServicePreferences.NETWORK_SERVICE_RECEIVED, 0).apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            LocalBroadcastManager.getInstance(this).registerReceiver(mServiceBroadcastReceiver,
                    new IntentFilter(ServicePreferences.SERVICE_MANAGER));
            LocalBroadcastManager.getInstance(this).registerReceiver(mServiceBroadcastReceiver,
                    new IntentFilter(ServicePreferences.BATTERY_SERVICE));
            LocalBroadcastManager.getInstance(this).registerReceiver(mServiceBroadcastReceiver,
                    new IntentFilter(ServicePreferences.CPU_SERVICE));
            LocalBroadcastManager.getInstance(this).registerReceiver(mServiceBroadcastReceiver,
                    new IntentFilter(ServicePreferences.DISK_SERVICE));
            LocalBroadcastManager.getInstance(this).registerReceiver(mServiceBroadcastReceiver,
                    new IntentFilter(ServicePreferences.MEMORY_SERVICE));
            LocalBroadcastManager.getInstance(this).registerReceiver(mServiceBroadcastReceiver,
                    new IntentFilter(ServicePreferences.NETWORK_SERVICE));

            isReceiverRegistered = true;
        }
    }

    private void setServices(String services) {
        mServicesConfig.add(0, services.contains("battery"));
        mServicesConfig.add(1, services.contains("cpu"));
        mServicesConfig.add(2, services.contains("memory"));
        mServicesConfig.add(3, services.contains("disk"));
        mServicesConfig.add(4, services.contains("network"));
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
