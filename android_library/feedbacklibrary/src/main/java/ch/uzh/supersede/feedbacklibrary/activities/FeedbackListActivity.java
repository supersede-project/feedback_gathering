package ch.uzh.supersede.feedbacklibrary.activities;


import android.os.Bundle;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.USER_NAME;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class FeedbackListActivity extends AbstractBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_list);
        onPostCreate();
    }
}
