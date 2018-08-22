package ch.uzh.supersede.feedbacklibrary.activities;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.view.*;
import android.widget.*;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.services.*;
import ch.uzh.supersede.feedbacklibrary.utils.*;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.UserConstants.USER_REPORTED_FEEDBACK;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.RESPONSE_MODE.READING;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.SAVE_MODE.*;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.ACTIVE;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public final class FeedbackDetailsActivity extends AbstractFeedbackDetailsActivity implements IFeedbackServiceEventListener {
    private Button upButton;
    private Button downButton;
    private Button reportButton;
    private Button makePublicButton;
    private TextView statusText;
    private boolean creationMode = false;
    private boolean tutorialFinished = false;
    private boolean tutorialInitialized = false;

    @Override
    protected void initViews() {
        setContentView(R.layout.activity_feedback_details);
        setMode(READING);
        setCallback(this);
        setResponseLayout(getView(R.id.details_layout_scroll_layout, LinearLayout.class));
        setScrollContainer(getView(R.id.details_layout_scroll_container, ScrollView.class));
        setVotesText(getView(R.id.details_text_votes, TextView.class));
        setUserText(getView(R.id.details_text_user, TextView.class));
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

        colorViews(0, upButton, downButton,makePublicButton);
        colorViews(1, getView(R.id.details_root, RelativeLayout.class));
        colorViews(0, getView(R.id.details_layout_scroll_layout, LinearLayout.class));
        colorViews(configuration.getLastColorIndex(), statusText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCallerClass(ObjectUtility.getCallerClass(getIntent()));

        updateReportStatus(null);
        updateOwnFeedbackCase();
        initStatusText();
        drawLayoutOutlines(R.id.details_layout_up,R.id.details_layout_mid,R.id.details_layout_low,R.id.details_layout_scroll_container,R.id.details_layout_button);
        tutorialFinished = getSharedPreferences(SHARED_PREFERENCES_ID, MODE_PRIVATE).getBoolean(SHARED_PREFERENCES_TUTORIAL_DETAILS, false);
        tutorialInitialized  = getSharedPreferences(SHARED_PREFERENCES_ID, MODE_PRIVATE).getBoolean(SHARED_PREFERENCES_TUTORIAL_DETAILS, false);
        onPostCreate();
    }

    private void initStatusText() {
        statusText.setText(getString(R.string.details_status,getFeedbackDetailsBean().getFeedbackStatus().getLabel()));
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

    @Override
    protected void updateFeedbackState() {
        super.updateFeedbackState();
        if (ACTIVE.check(this, true)) {
            if (getFeedbackState().isUpVoted()) {
                getVotesText().setTextColor(ColorUtility.adjustColorToBackground(getTopColor(1),ContextCompat.getColor(this, R.color.green_4),0.4));
                upButton.setEnabled(false);
                downButton.setEnabled(true);
            }
            if (getFeedbackState().isDownVoted()) {
                getVotesText().setTextColor(ColorUtility.adjustColorToBackground(getTopColor(1),ContextCompat.getColor(this, R.color.red_5),0.4));
                downButton.setEnabled(false);
                upButton.setEnabled(true);
            }
            if (getFeedbackState().isEqualVoted() && getFeedbackDetailsBean().getFeedbackBean().isPublic()) {
                getVotesText().setTextColor(ColorUtility.adjustColorToBackground(getTopColor(1),ContextCompat.getColor(this, R.color.black),0.4));
                upButton.setEnabled(true);
                downButton.setEnabled(true);
            }
        }
    }

    protected void handleUpVoteButtonClicked() {
        FeedbackDatabase.getInstance(this).writeFeedback(getFeedbackDetailsBean().getFeedbackBean(), UP_VOTED);
        getVotesText().setText(getFeedbackDetailsBean().getFeedbackBean().upVote());
    }

    protected void handleDownVoteButtonClicked() {
        FeedbackDatabase.getInstance(this).writeFeedback(getFeedbackDetailsBean().getFeedbackBean(), DOWN_VOTED);
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
            disableViews(reportButton,upButton,downButton);
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
            case EDIT_FEEDBACK_PUBLICATION_MOCK:
                Toast.makeText(FeedbackDetailsActivity.this, R.string.details_published, Toast.LENGTH_SHORT).show();
                break;
            default:
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void createInfoBubbles() {
        if (!tutorialFinished && !tutorialInitialized) {
            imageButton.setEnabled(false);
            audioButton.setEnabled(false);
            tagButton.setEnabled(false);
            upButton.setEnabled(false);
            downButton.setEnabled(false);
            makePublicButton.setEnabled(false);
            reportButton.setEnabled(false);
            responseButton.setEnabled(false);
            subscribeButton.setEnabled(false);

            RelativeLayout root = getView(R.id.details_root, RelativeLayout.class);

            String votesLabel = getString(R.string.detail_tutorial_title_votes);
            String publicLabel = getString(R.string.detail_tutorial_title_public);
            String multimediaLabel = getString(R.string.detail_tutorial_title_multimedia);
            String subscribeLabel = getString(R.string.detail_tutorial_title_subs);
            String replyLabel = getString(R.string.detail_tutorial_title_reply);
            float textSize = ScalingUtility
                    .getInstance()
                    .getMinTextSizeScaledForWidth(20, 75, 0.45, votesLabel,publicLabel,multimediaLabel, replyLabel, subscribeLabel);
            RelativeLayout repLayout = infoUtility.addInfoBox(root, replyLabel, getString(R.string.detail_tutorial_content_reply), textSize, this, responseButton);
            RelativeLayout subLayout = infoUtility.addInfoBox(root, subscribeLabel, getString(R.string.detail_tutorial_content_subs), textSize, this, subscribeButton, repLayout);
            RelativeLayout mulLayout = infoUtility.addInfoBox(root, multimediaLabel, getString(R.string.detail_tutorial_content_multimedia), textSize, this, audioButton, subLayout);
            RelativeLayout pubLayout = infoUtility.addInfoBox(root, publicLabel, getString(R.string.detail_tutorial_content_public), textSize, this, downButton, mulLayout);
            RelativeLayout votLayout = infoUtility.addInfoBox(root, votesLabel, getString(R.string.detail_tutorial_content_votes), textSize, this, votesText, pubLayout);
            repLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Toast.makeText(v.getContext(), R.string.tutorial_finished, Toast.LENGTH_SHORT).show();
                    getSharedPreferences(SHARED_PREFERENCES_ID, MODE_PRIVATE).edit().putBoolean(SHARED_PREFERENCES_TUTORIAL_DETAILS, true).apply();
                    imageButton.setEnabled(true);
                    audioButton.setEnabled(true);
                    tagButton.setEnabled(true);
                    upButton.setEnabled(true);
                    downButton.setEnabled(true);
                    makePublicButton.setEnabled(true);
                    reportButton.setEnabled(true);
                    responseButton.setEnabled(true);
                    subscribeButton.setEnabled(true);
                    tutorialFinished = true;
                    updateFeedbackState();
                    return false;
                }
            });
            colorShape(1, mulLayout, subLayout, repLayout, pubLayout, votLayout);
            tutorialInitialized = true;
        }
    }
}

