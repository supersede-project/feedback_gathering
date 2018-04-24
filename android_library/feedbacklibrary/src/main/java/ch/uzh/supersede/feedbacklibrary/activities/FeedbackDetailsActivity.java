package ch.uzh.supersede.feedbacklibrary.activities;


import android.os.Bundle;
import android.widget.*;

import java.util.*;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.beans.*;
import ch.uzh.supersede.feedbacklibrary.components.buttons.FeedbackResponseListItem;
import ch.uzh.supersede.feedbacklibrary.stubs.RepositoryStub;
import ch.uzh.supersede.feedbacklibrary.utils.CollectionUtility;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.EXTRA_KEY_FEEDBACK_BEAN;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class FeedbackDetailsActivity extends AbstractBaseActivity {
    private FeedbackDetailsBean feedbackDetailsBean;
    private LinearLayout responseLayout;
    private TextView votesText;
    private TextView userText;
    private TextView titleText;
    private TextView descriptionText;
    private Button imageButton;
    private Button audioButton;
    private Button labelButton;
    private Button subscribeButton;
    private Button responseButton;
    private ArrayList<FeedbackResponseListItem> responseList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_details);
        responseLayout = getView(R.id.details_layout_scroll, LinearLayout.class);
        votesText = getView(R.id.details_text_votes, TextView.class);
        userText = getView(R.id.details_text_user, TextView.class);
        titleText = getView(R.id.details_text_title, TextView.class);
        descriptionText = getView(R.id.details_text_description, TextView.class);
        FeedbackBean feedbackBean = (FeedbackBean) getIntent().getSerializableExtra(EXTRA_KEY_FEEDBACK_BEAN);
        if (feedbackBean != null) {
            feedbackDetailsBean = RepositoryStub.getFeedbackDetails(this, feedbackBean);
            if (feedbackDetailsBean != null) {
                for (FeedbackResponseBean bean : feedbackDetailsBean.getResponses()) {
                    FeedbackResponseListItem feedbackResponseListItem = new FeedbackResponseListItem(this, bean);
                    responseList.add(feedbackResponseListItem);
                }
                Collections.sort(responseList);
                for (FeedbackResponseListItem item : responseList) {
                    responseLayout.addView(item);
                }
                votesText.setText(feedbackDetailsBean.getUpVotesAsText());
                userText.setText(feedbackDetailsBean.getUserName());
                titleText.setText(feedbackDetailsBean.getTitle());
                descriptionText.setText(feedbackDetailsBean.getDescription());
            }
        }
        onPostCreate();
    }

    //TODO FEEDBACK VON STUB
    //SORTIERUNG DER RESPONSES
}
