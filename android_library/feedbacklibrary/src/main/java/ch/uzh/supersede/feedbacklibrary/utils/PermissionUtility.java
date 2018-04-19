package ch.uzh.supersede.feedbacklibrary.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.ACTIVE;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.ADVANCED;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.LOCKED;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.PASSIVE;

public class PermissionUtility {

    public enum USER_LEVEL {
        LOCKED(0),
        PASSIVE(1,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE),
        ACTIVE(2,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE),
        ADVANCED(3,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO);
        private int level;
        private String[] permissions;

        USER_LEVEL(int level, String... permissions) {
            this.level = level;
            this.permissions = permissions;
        }

        public boolean check(Context context) {
            return check(context,false);
        }

        public boolean check(Context context, boolean ignoreDatabaseCheck) {
            boolean contributor = context.getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE).getBoolean(FEEDBACK_CONTRIBUTOR, false);
            boolean noMissingPermissions = getMissing(context).length == 0;
            return ignoreDatabaseCheck?contributor && noMissingPermissions:contributor && noMissingPermissions && checkForUsername(context);
        }

        private boolean checkForUsername(Context context) {
            if (this == ACTIVE || this == ADVANCED) {
                String username = FeedbackDatabase.getInstance(context).readString(USER_NAME, null);
                return StringUtility.hasText(username);
            }
            return true;
        }

        public String[] getMissing(Context context) {
            ArrayList<String> permissionsNotGranted = new ArrayList<>();
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsNotGranted.add(permission);
                }
            }
            return permissionsNotGranted.toArray(new String[permissionsNotGranted.size()]);
        }

        public int getLevel() {
            return level;
        }
    }

    public static USER_LEVEL getUserLevel(Context context) {
        return getUserLevel(context,false);
    }
    public static USER_LEVEL getUserLevel(Context context, boolean ignoreDatabaseCheck) {
        if (ADVANCED.check(context,ignoreDatabaseCheck)) {
            return ADVANCED;
        }
        if (ACTIVE.check(context,ignoreDatabaseCheck)) {
            return ACTIVE;
        }
        if (PASSIVE.check(context,ignoreDatabaseCheck)) {
            return PASSIVE;
        }
        return LOCKED;
    }
}
