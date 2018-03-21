package ch.uzh.supersede.hostapplication;

import android.Manifest;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import ch.uzh.supersede.feedbacklibrary.activities.AbstractBaseActivity;
import ch.uzh.supersede.feedbacklibrary.activities.FeedbackActivity;
import ch.uzh.supersede.feedbacklibrary.entrypoint.FeedbackConnector;
import ch.uzh.supersede.feedbacklibrary.services.FeedbackService;
import ch.uzh.supersede.feedbacklibrary.services.FeedbackService.ConfigurationRequestWrapper;
import ch.uzh.supersede.feedbacklibrary.services.FeedbackService.ConfigurationRequestWrapper.ConfigurationRequestWrapperBuilder;
import ch.uzh.supersede.feedbacklibrary.utils.Utils;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.SUPERSEEDE_BASE_URL;

public class CleanStartActivity extends AbstractBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void onConnect(View view){
        FeedbackConnector.getInstance().connect(view,this);
    }
}