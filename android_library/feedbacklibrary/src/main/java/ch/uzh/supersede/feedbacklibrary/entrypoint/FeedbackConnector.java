package ch.uzh.supersede.feedbacklibrary.entrypoint;


import android.Manifest;
import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import java.util.HashMap;

import ch.uzh.supersede.feedbacklibrary.services.FeedbackService;
import ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility;
import ch.uzh.supersede.feedbacklibrary.utils.Utils;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.ACTIVE;

public class FeedbackConnector {
    HashMap<Integer,View> registeredViews;

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
        FeedbackService.getInstance().startFeedbackHubWithScreenshotCapture(SUPERSEEDE_BASE_URL, activity, 1337, "en");
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
}
