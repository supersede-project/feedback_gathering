package ch.uzh.supersede.feedbacklibrary.activities;


import android.os.Bundle;
import android.widget.Toast;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.utils.DateUtility;
import ch.uzh.supersede.feedbacklibrary.wrapper.FeedbackBean;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.FeedbackActivityConstants.EXTRA_KEY_FEEDBACK_BEAN;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class FeedbackDetailsActivity extends AbstractBaseActivity {
    private FeedbackBean feedbackBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_details);
        feedbackBean = (FeedbackBean) getIntent().getSerializableExtra(EXTRA_KEY_FEEDBACK_BEAN);
        if (feedbackBean != null) {
            Toast.makeText(getApplicationContext(), getString(R.string.details_feedback_toast,
                            feedbackBean.getTitle(), feedbackBean.getUserName(),
                            DateUtility.getDateFromLong(feedbackBean.getTimeStamp()),
                            feedbackBean.getUpVotes(),
                            feedbackBean.getResponses()), Toast.LENGTH_LONG)
                    .show();
        }
        onPostCreate();
    }
}
