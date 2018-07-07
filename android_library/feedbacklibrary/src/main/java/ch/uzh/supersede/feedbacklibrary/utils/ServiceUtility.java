package ch.uzh.supersede.feedbacklibrary.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import java.io.Serializable;

public class ServiceUtility {
    private ServiceUtility() {
    }

    private static boolean isServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static void startService(Class<?> serviceClass, Context context, Extra... extras) {
        if (!isServiceRunning(serviceClass, context)) {
            Intent service = new Intent(context, serviceClass);
            for (Extra extra : extras) {
                service.putExtra(extra.getName(), extra.getValue());
            }
            context.startService(service);
        }
    }

    public static void stopService(Class<?> serviceClass, Activity activity) {
        if (isServiceRunning(serviceClass, activity)) {
            activity.stopService(new Intent(activity.getBaseContext(), serviceClass));
        }
    }

    public static class Extra {
        String name;
        Serializable value;

        public Extra(String name, Serializable value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public Serializable getValue() {
            return value;
        }
    }
}
