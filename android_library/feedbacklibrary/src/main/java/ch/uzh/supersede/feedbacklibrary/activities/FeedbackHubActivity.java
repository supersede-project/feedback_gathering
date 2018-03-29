package ch.uzh.supersede.feedbacklibrary.activities;


import android.os.Bundle;
import android.view.View;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.services.FeedbackService;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class FeedbackHubActivity extends AbstractBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_hub);
        onPostCreate();
    }

    @Override
    public void onButtonClicked(View view) {
        if (view != null) {
            int i = view.getId();
            if (i == R.id.hub_button_list) {
                startActivity(this, FeedbackListActivity.class);
            } else if (i == R.id.hub_button_create) {
                startActivity(this, FeedbackActivity.class);
            } else if (i == R.id.hub_button_settings) {
                startActivity(this, FeedbackSettingsActivity.class);
            } else {
                //NOP
            }
        }
    }
}
