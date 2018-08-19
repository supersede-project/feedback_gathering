package ch.uzh.supersede.feedbacklibrary.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import java.io.Serializable;

import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.ENABLE_NOTIFICATIONS;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.ADVANCED;

public final class ServiceUtility {
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
        if (!isServiceRunning(serviceClass, context) && isServiceEnabled(context)) {
            Intent service = new Intent(context, serviceClass);
            for (Extra extra : extras) {
                service.putExtra(extra.getName(), extra.getValue());
            }
            context.startService(service);
        }
    }

    public static void stopService(Class<?> serviceClass, Context context) {
        if (isServiceRunning(serviceClass, context)) {
            context.stopService(new Intent(context, serviceClass));
        }
    }

    public static boolean isServiceEnabled(Context context) {
        if (ADVANCED.check(context)) {
            return FeedbackDatabase.getInstance(context).readBoolean(ENABLE_NOTIFICATIONS, false);
        }
        return false;
    }

    public static void setServiceEnabled(Context context, boolean isEnabled) {
        if (ADVANCED.check(context)) {
            FeedbackDatabase.getInstance(context).writeBoolean(ENABLE_NOTIFICATIONS, isEnabled);
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
