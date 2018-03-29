package ch.uzh.supersede.feedbacklibrary.activities;


import android.os.Bundle;

import ch.uzh.supersede.feedbacklibrary.R;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class FeedbackSettingsActivity extends AbstractBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_settings);
        onPostCreate();
    }

}
