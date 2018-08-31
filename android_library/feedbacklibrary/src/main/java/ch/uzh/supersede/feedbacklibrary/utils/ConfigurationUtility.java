package ch.uzh.supersede.feedbacklibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import ch.uzh.supersede.feedbacklibrary.beans.LocalConfigurationBean;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.EXTRA_KEY_APPLICATION_CONFIGURATION;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.ACTIVE;

public final class ConfigurationUtility {
    private ConfigurationUtility() {
    }

    public static LocalConfigurationBean getConfigurationFromActivity(Activity activity) {
        return getConfigurationFromIntent(activity.getIntent());
    }

    public static LocalConfigurationBean getConfigurationFromIntent(Intent intent) {
        return (LocalConfigurationBean) intent.getSerializableExtra(EXTRA_KEY_APPLICATION_CONFIGURATION);
    }

    public static void execStoreStateToDatabase(Context context, LocalConfigurationBean configuration) {
        if (ACTIVE.check(context)) {
            FeedbackDatabase.getInstance(context).writeConfiguration(configuration);
        }
    }

    public static LocalConfigurationBean getConfigurationFromDatabase(Context context) {
        if (ACTIVE.check(context)) {
            return FeedbackDatabase.getInstance(context).readConfiguration();
        }
        return null;
    }
}
