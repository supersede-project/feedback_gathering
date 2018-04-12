package ch.uzh.supersede.feedbacklibrary.entrypoint;


import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import java.util.HashMap;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.activities.FeedbackHubActivity;
import ch.uzh.supersede.feedbacklibrary.services.FeedbackService;
import ch.uzh.supersede.feedbacklibrary.utils.Utils;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.FeedbackActivityConstants.EXTRA_KEY_APPLICATION_ID;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.FeedbackActivityConstants.EXTRA_KEY_LANGUAGE;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.SUPERSEEDE_BASE_URL;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.ACTIVE;

public class FeedbackConnector {
    private HashMap<Integer,View> registeredViews;

    private static final FeedbackConnector instance = new FeedbackConnector();

    public static FeedbackConnector getInstance() {
        return instance;
    }

    private FeedbackConnector() {
        registeredViews = new HashMap<>();
    }

    public void connect(View view, Activity activity){
        if (!registeredViews.containsKey(view.getId())){
            registeredViews.put(view.getId(),view);
            view.setOnTouchListener(new FeedbackOnTouchListener(activity,view));
            onTouchConnector(activity, view, null);
        }
    }

    protected static void onTouchConnector(Activity activity, View view, MotionEvent event){
        if (event == null){ //On Listener attached
            onListenerConnected(activity,view);
        }
        onListenerTriggered(activity,view,event);
    }

    private static void onListenerConnected(Activity activity, View view) {
        //NOP
    }

    private static void onListenerTriggered(Activity activity, View view, MotionEvent event) {
        startFeedbackHubWithScreenshotCapture(SUPERSEEDE_BASE_URL, activity, 1337, "en");
    }

    private static class FeedbackOnTouchListener implements OnTouchListener{
        private View mView;
        private Activity mActivity;
        private FeedbackOnTouchListener(Activity activity, View view){
            this.mActivity = activity;
            this.mView = view;
        }
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            onTouchConnector(mActivity,mView,event);
            return false;
        }
    }

    /**
     * Takes a screenshot of the current screen automatically and opens the FeedbackActivity from the feedback library in case if a PUSH feedback is triggered.
     */
    public static void startFeedbackHubWithScreenshotCapture(@NonNull final String baseURL, @NonNull final Activity activity, final long applicationId, @NonNull final String language) {
        Intent intent = new Intent(activity, FeedbackHubActivity.class);
        if (ACTIVE.check(activity)) {
            Utils.wipeImages(activity.getApplicationContext());
            Utils.storeScreenshotToDatabase(activity);
        } else {
            Utils.storeScreenshotToIntent(activity, intent);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra(EXTRA_KEY_APPLICATION_ID, applicationId);
        intent.putExtra(EXTRA_KEY_LANGUAGE, language);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
