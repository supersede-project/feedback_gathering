package ch.uzh.supersede.feedbacklibrary.entrypoint;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import java.util.HashMap;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.activities.FeedbackHubActivity;
import ch.uzh.supersede.feedbacklibrary.beans.LocalConfigurationBean;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.utils.ConfigurationUtility;
import ch.uzh.supersede.feedbacklibrary.utils.ImageUtility;

import static android.content.Context.MODE_PRIVATE;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.UserConstants.USER_KARMA;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.ACTIVE;

public class FeedbackConnector {
    private static final FeedbackConnector instance = new FeedbackConnector();
    private HashMap<Integer, View> registeredViews;

    private FeedbackConnector() {
        registeredViews = new HashMap<>();
    }

    public static FeedbackConnector getInstance() {
        return instance;
    }

    private static void onTouchConnector(Activity activity, View view, MotionEvent event) {
        if (event == null) { //On Listener attached
            onListenerConnected(activity, view);
        }
        onListenerTriggered(activity, view, event);
    }

    private static void onListenerConnected(Activity activity, View view) {
        //NOP
    }

    private static void onListenerTriggered(Activity activity, View view, MotionEvent event) {
        startFeedbackHubWithScreenshotCapture(activity);
    }

    /**
     * Takes a screenshot of the current screen automatically and opens the FeedbackActivity from the feedback library in case if a PUSH feedback is triggered.
     */
    private static void startFeedbackHubWithScreenshotCapture(@NonNull final Activity activity) {
        Intent intent = new Intent(activity, FeedbackHubActivity.class);
        Bitmap screenshot;
        if (ACTIVE.check(activity)) {
            ImageUtility.wipeImages(activity.getApplicationContext());
            screenshot = ImageUtility.storeScreenshotToDatabase(activity);
        } else {
            screenshot = ImageUtility.storeScreenshotToIntent(activity, intent);
        }
        getActivityConfiguration(activity, intent, screenshot);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private static void getActivityConfiguration(Activity activity, Intent intent, Bitmap screenshot) {
        Integer[] topColors = ImageUtility.calculateTopNColors(screenshot, 3, 20);

        LocalConfigurationBean configurationBean = new LocalConfigurationBean(activity, topColors);
        ConfigurationUtility.execStoreStateToDatabase(activity, configurationBean);

        //Host Name for Database
        activity.getSharedPreferences(SHARED_PREFERENCES_ID, MODE_PRIVATE).edit().putString(SHARED_PREFERENCES_HOST_APPLICATION_NAME, configurationBean.getHostApplicationName()).apply();
        intent.putExtra(EXTRA_KEY_HOST_APPLICATION_NAME, configurationBean.getHostApplicationName());
        intent.putExtra(EXTRA_KEY_APPLICATION_CONFIGURATION, configurationBean);
    }

    public void connect(View view, Activity activity) {
        if (!registeredViews.containsKey(view.getId())) {
            registeredViews.put(view.getId(), view);
        }
        view.setOnTouchListener(new FeedbackOnTouchListener(activity, view));
        onTouchConnector(activity, view, null);
    }

    public Integer getCurrentUserKarma(Activity activity) {
        if (ACTIVE.check(activity)) {
            return FeedbackDatabase.getInstance(activity).readInteger(USER_KARMA, null);
        }
        return null;
    }

    private static class FeedbackOnTouchListener implements OnTouchListener {
        private View mView;
        private Activity mActivity;

        private FeedbackOnTouchListener(Activity activity, View view) {
            this.mActivity = activity;
            this.mView = view;
        }

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            onTouchConnector(mActivity, mView, event);
            return false;
        }
    }
}