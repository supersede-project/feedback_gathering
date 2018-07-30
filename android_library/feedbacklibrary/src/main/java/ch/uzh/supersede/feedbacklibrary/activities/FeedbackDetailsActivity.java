package ch.uzh.supersede.feedbacklibrary.activities;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.ContentFrameLayout;
import android.text.InputFilter;
import android.view.View;
import android.widget.*;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.services.FeedbackService;
import ch.uzh.supersede.feedbacklibrary.services.IFeedbackServiceEventListener;
import ch.uzh.supersede.feedbacklibrary.stubs.RepositoryStub;
import ch.uzh.supersede.feedbacklibrary.utils.PopUp;
import ch.uzh.supersede.feedbacklibrary.utils.StringUtility;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.EXTRA_FROM_CREATION;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.UserConstants.USER_REPORTED_FEEDBACK;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.RESPONSE_MODE.READING;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.ACTIVE;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class FeedbackDetailsActivity extends AbstractFeedbackDetailsActivity implements IFeedbackServiceEventListener {
    private Button upButton;
    private Button downButton;
    private Button reportButton;
    private Button makePublicButton;
    private boolean creationMode = false;
    private TextView statusText;

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_feedback_details);

        setMode(READING);
        setCallback(this);
        setResponseLayout(getView(R.id.details_layout_scroll_layout, LinearLayout.class));
        setScrollContainer(getView(R.id.details_layout_scroll_container, ScrollView.class));
        setVotesText(getView(R.id.details_text_votes, TextView.class));
        setUserText(getView(R.id.details_text_user, TextView.class));
        setStatus(getView(R.id.details_text_status, Spinner.class));
        setTitleText(getView(R.id.details_text_title, TextView.class));
        setDescriptionText(getView(R.id.details_text_description, TextView.class));
        setImageButton(getView(R.id.details_button_images, Button.class));
        setAudioButton(getView(R.id.details_button_audio, Button.class));
        setTagButton(getView(R.id.details_button_tags, Button.class));
        setSubscribeButton(getView(R.id.details_button_subscribe, Button.class));
        setResponseButton(getView(R.id.details_button_response, Button.class));

        upButton = getView(R.id.details_button_up, Button.class);
        downButton = getView(R.id.details_button_down, Button.class);
        reportButton = getView(R.id.details_button_report, Button.class);
        makePublicButton = getView(R.id.details_button_make_public, Button.class);
        creationMode = getIntent().getBooleanExtra(EXTRA_FROM_CREATION, false);
        statusText = getView(R.id.details_text_status, TextView.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        colorViews(0, upButton, downButton);
        colorViews(1, getView(R.id.details_root, ContentFrameLayout.class));
        updateReportStatus(null);
        updateOwnFeedbackCase();
        invokeVersionControl(5, getAudioButton().getId(), getTagButton().getId());

        onPostCreate();
    }

    @Override
    protected void initFeedbackDetailView() {
        super.initFeedbackDetailView();
        if (!configuration.isReportEnabled()) {
            reportButton.setVisibility(View.GONE);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getSubscribeButton().getLayoutParams();
            layoutParams.weight = 3;
            getSubscribeButton().setLayoutParams(layoutParams);
            layoutParams = (LinearLayout.LayoutParams) getResponseButton().getLayoutParams();
            layoutParams.weight = 3;
            getResponseButton().setLayoutParams(layoutParams);
        }
        if (getFeedbackDetailsBean() != null) {
            statusText.setText(getString(R.string.details_status, getFeedbackDetailsBean().getFeedbackStatus().getLabel()));

            if (getFeedbackDetailsBean().getFeedbackBean() != null && getFeedbackDetailsBean().getFeedbackBean().isOwnFeedback(getApplicationContext())) {
                upButton.setEnabled(false);
                downButton.setEnabled(false);
                if (!getFeedbackDetailsBean().getFeedbackBean().isPublic()) {
                    makePublicButton.setVisibility(View.VISIBLE);
                }
            }
        } else {
            this.onBackPressed();
        }
    }

    protected void updateFeedbackState() {
        super.updateFeedbackState();
        if (ACTIVE.check(this, true)) {
            if (getFeedbackState().isUpVoted()) {
                getVotesText().setTextColor(ContextCompat.getColor(this, R.color.green_4));
                upButton.setEnabled(false);
            }
            if (getFeedbackState().isDownVoted()) {
                getVotesText().setTextColor(ContextCompat.getColor(this, R.color.red_5));
                downButton.setEnabled(false);
            }
            if (getFeedbackState().isEqualVoted() && getFeedbackDetailsBean().getFeedbackBean().isPublic()) {
                colorViews(1, getVotesText());
                upButton.setEnabled(true);
                downButton.setEnabled(true);
            }
        }
    }

    @Override
    public void onButtonClicked(View view) {
        super.onButtonClicked(view);
        if (view.getId() == upButton.getId()) {
            handleUpVoteButtonClicked();
        } else if (view.getId() == downButton.getId()) {
            handleDownVoteButtonClicked();
        } else if (view.getId() == makePublicButton.getId()) {
            handlePublicationButtonClicked();
        } else if (view.getId() == reportButton.getId()) {
            handleReportButtonClicked();
        }
        updateFeedbackState();
    }

    protected void handleUpVoteButtonClicked() {
        RepositoryStub.sendUpVote(this, getFeedbackDetailsBean().getFeedbackBean());
        getVotesText().setText(getFeedbackDetailsBean().getFeedbackBean().upVote());
    }

    protected void handleDownVoteButtonClicked() {
        RepositoryStub.sendDownVote(this, getFeedbackDetailsBean().getFeedbackBean());
        getVotesText().setText(getFeedbackDetailsBean().getFeedbackBean().downVote());
    }

    protected void handlePublicationButtonClicked() {
        DialogInterface.OnClickListener okClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                makePublicButton.setVisibility(View.INVISIBLE);
                FeedbackService.getInstance(getApplicationContext()).editFeedbackPublication(FeedbackDetailsActivity.this, getFeedbackDetailsBean().getFeedbackBean(), true);
                dialog.cancel();
            }
        };
        new PopUp(this)
                .withTitle(getString(R.string.details_make_public_title))
                .withCustomOk("Confirm", okClickListener)
                .withMessage(getString(R.string.details_make_public_content)).buildAndShow();
    }

    protected void handleReportButtonClicked() {
        final EditText reportReason = new EditText(this);
        reportReason.setFilters(new InputFilter[]{new InputFilter.LengthFilter(configuration.getMaxReportLength())});
        DialogInterface.OnClickListener okClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String report = reportReason.getText().toString();
                if (!StringUtility.hasText(report)) {
                    Toast.makeText(FeedbackDetailsActivity.this, R.string.details_report_error_empty, Toast.LENGTH_SHORT).show();
                } else if (report.length() < configuration.getMinReportLength()) {
                    Toast.makeText(FeedbackDetailsActivity.this, R.string.details_report_error_short, Toast.LENGTH_SHORT).show();
                } else if (report.length() > configuration.getMaxReportLength()) {
                    Toast.makeText(FeedbackDetailsActivity.this, R.string.details_report_error_long, Toast.LENGTH_SHORT).show();
                } else {
                    FeedbackService.getInstance(getApplicationContext()).createFeedbackReport(FeedbackDetailsActivity.this, getFeedbackDetailsBean(), null);
                    dialog.dismiss();
                }
            }
        };
        new PopUp(this)
                .withTitle(getString(R.string.details_report_title))
                .withInput(reportReason)
                .withCustomOk("Confirm", okClickListener)
                .withMessage(getString(R.string.details_report_content, configuration.getMinReportLength(), configuration.getMaxReportLength())).buildAndShow();

    }

    @Override
    public void onBackPressed() {
        if (creationMode) {
            startActivity(this, FeedbackHubActivity.class, true);
        } else {
            super.onBackPressed();
        }
    }

    public void updateReportStatus(String report) {
        if (ACTIVE.check(getApplicationContext())) {
            if (report != null) {
                FeedbackDatabase.getInstance(getApplicationContext()).writeString(USER_REPORTED_FEEDBACK + getFeedbackDetailsBean().getFeedbackBean().getFeedbackId(), report);
            }
            if (FeedbackDatabase.getInstance(getApplicationContext()).readString(USER_REPORTED_FEEDBACK + getFeedbackDetailsBean().getFeedbackBean().getFeedbackId(), null) != null) {
                disableViews(reportButton);
            }
        }
    }

    /**
     * disable certain buttons on own feedback
     */
    public void updateOwnFeedbackCase() {
        if (ACTIVE.check(getApplicationContext()) && getFeedbackDetailsBean().getUserName().equals(getUserName())) {
            disableViews(reportButton, getSubscribeButton());
        }
    }

    @Override
    public void onEventCompleted(EventType eventType, Object response) {
        super.onEventCompleted(eventType, response);
        switch (eventType) {
            case CREATE_FEEDBACK_REPORT:
            case CREATE_FEEDBACK_REPORT_MOCK:
                if (response instanceof String) {
                    Toast.makeText(FeedbackDetailsActivity.this, R.string.details_report_sent, Toast.LENGTH_SHORT).show();
                    updateReportStatus((String) response);
                }
                break;
            case EDIT_FEEDBACK_PUBLICATION:
            case CREATE_FEEDBACK_PUBLICATION_MOCK:
                Toast.makeText(FeedbackDetailsActivity.this, R.string.details_published, Toast.LENGTH_SHORT).show();
                break;
            default:
        }
    }
}

