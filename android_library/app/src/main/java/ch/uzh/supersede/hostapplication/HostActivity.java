package ch.uzh.supersede.hostapplication;

import android.os.Bundle;
import android.view.View;

import ch.uzh.supersede.feedbacklibrary.activities.AbstractBaseActivity;
import ch.uzh.supersede.feedbacklibrary.entrypoint.FeedbackConnector;

public class HostActivity extends AbstractBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
    }

    public void onFeedbackClicked(View view){
        FeedbackConnector.getInstance().connect(view,this);
    }
}