package ch.uzh.supersede.feedbacklibrary.activities;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.ContentFrameLayout;
import android.text.InputFilter;
import android.view.*;
import android.widget.*;

import java.util.ArrayList;
import java.util.Collections;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.beans.*;
import ch.uzh.supersede.feedbacklibrary.components.buttons.FeedbackResponseListItem;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.services.FeedbackService;
import ch.uzh.supersede.feedbacklibrary.stubs.RepositoryStub;
import ch.uzh.supersede.feedbacklibrary.utils.Enums.RESPONSE_MODE;
import ch.uzh.supersede.feedbacklibrary.utils.PopUp;
import ch.uzh.supersede.feedbacklibrary.utils.StringUtility;

import static ch.uzh.supersede.feedbacklibrary.components.buttons.FeedbackResponseListItem.RESPONSE_MODE.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.UserConstants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.RESPONSE_MODE.READING;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.ACTIVE;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class FeedbackDetailsActivity extends AbstractBaseActivity {
    public static RESPONSE_MODE mode = READING;
    private FeedbackDetailsBean feedbackDetailsBean;
    private LocalFeedbackState feedbackState;
    private static LinearLayout responseLayout;
    private static ScrollView scrollContainer;
    private TextView votesText;
    private TextView userText;
    private TextView statusText;
    private TextView titleText;
    private TextView descriptionText;
    private Button upButton;
    private Button downButton;
    private Button imageButton;
    private Button reportButton;
    private Button audioButton;
    private Button tagButton;
    private Button subscribeButton;
    private Button responseButton;
    private Button makePublicButton;
    private boolean creationMode = false;
    private ArrayList<FeedbackResponseListItem> responseList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mode = READING;
        setContentView(R.layout.activity_feedback_details);
        responseLayout = getView(R.id.details_layout_scroll_layout, LinearLayout.class);
        scrollContainer = getView(R.id.details_layout_scroll_container, ScrollView.class);
        votesText = getView(R.id.details_text_votes, TextView.class);
        userText = getView(R.id.details_text_user, TextView.class);
        statusText = getView(R.id.details_text_status, TextView.class);
        titleText = getView(R.id.details_text_title, TextView.class);
        descriptionText = getView(R.id.details_text_description, TextView.class);
        upButton = getView(R.id.details_button_up, Button.class);
        downButton = getView(R.id.details_button_down, Button.class);
        imageButton = getView(R.id.details_button_images, Button.class);
        audioButton = getView(R.id.details_button_audio, Button.class);
        reportButton = getView(R.id.details_button_report, Button.class);
        tagButton = getView(R.id.details_button_tags, Button.class);
        subscribeButton = getView(R.id.details_button_subscribe, Button.class);
        responseButton = getView(R.id.details_button_response, Button.class);
        makePublicButton = getView(R.id.details_button_make_public, Button.class);
        FeedbackBean feedbackBean = (FeedbackBean) getIntent().getSerializableExtra(EXTRA_KEY_FEEDBACK_BEAN);
        FeedbackDetailsBean cachedFeedbackDetailsBean = (FeedbackDetailsBean) getIntent().getSerializableExtra(EXTRA_KEY_FEEDBACK_DETAIL_BEAN);
        creationMode = getIntent().getBooleanExtra(EXTRA_FROM_CREATION, false);
        if (!configuration.isReportEnabled()) {
            reportButton.setVisibility(View.GONE);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) subscribeButton.getLayoutParams();
            layoutParams.weight = 3;
            subscribeButton.setLayoutParams(layoutParams);
            layoutParams = (LinearLayout.LayoutParams) responseButton.getLayoutParams();
            layoutParams.weight = 3;
            responseButton.setLayoutParams(layoutParams);
        }
        if (cachedFeedbackDetailsBean != null) {
            feedbackDetailsBean = cachedFeedbackDetailsBean;
            feedbackBean = feedbackDetailsBean.getFeedbackBean();
        } else if (feedbackBean != null) {
            feedbackDetailsBean = RepositoryStub.getFeedbackDetails(this, feedbackBean);
        }
        if (feedbackDetailsBean != null) {
            updateFeedbackState();
            for (FeedbackResponseBean bean : feedbackDetailsBean.getResponses()) {
                FeedbackResponseListItem feedbackResponseListItem = new FeedbackResponseListItem(this, feedbackBean, bean, configuration, FIXED);
                responseList.add(feedbackResponseListItem);
            }
            Collections.sort(responseList);
            for (FeedbackResponseListItem item : responseList) {
                responseLayout.addView(item);
            }
            votesText.setText(feedbackDetailsBean.getUpVotesAsText());
            userText.setText(getString(R.string.details_user, feedbackDetailsBean.getUserName()));
            statusText.setText(getString(R.string.details_status, feedbackDetailsBean.getFeedbackStatus().getLabel()));
            titleText.setText(getString(R.string.details_title, feedbackDetailsBean.getTitle()));
            descriptionText.setText(feedbackDetailsBean.getDescription());

            if (feedbackDetailsBean.getFeedbackBean() != null && feedbackDetailsBean.getFeedbackBean().isOwnFeedback(getApplicationContext())) {
                upButton.setEnabled(false);
                downButton.setEnabled(false);
                if (!feedbackDetailsBean.getFeedbackBean().isPublic()) {
                    makePublicButton.setVisibility(View.VISIBLE);
                }
            }
        } else {
            this.onBackPressed();
        }
        //Disable all Database-related content, read only
        if (!ACTIVE.check(this, true)) {
            upButton.setEnabled(false);
            downButton.setEnabled(false);
            subscribeButton.setEnabled(false);
            responseButton.setEnabled(false);
        }
        colorViews(0, upButton, downButton, imageButton, audioButton, tagButton, subscribeButton, responseButton);
        colorViews(1, getView(R.id.details_root, ContentFrameLayout.class));
        colorViews(2, userText, titleText, statusText, descriptionText);
        updateReportStatus(null);
        updateOwnFeedbackCase();
        invokeVersionControl(5, audioButton.getId());
        onPostCreate();
    }

    private void updateFeedbackState() {
        if (ACTIVE.check(this, true)) {
            feedbackState = FeedbackDatabase.getInstance(this).getFeedbackState(feedbackDetailsBean.getFeedbackBean());
            if (feedbackState.isSubscribed() && subscribeButton.isEnabled()) {
                subscribeButton.setText(getString(R.string.details_unsubscribe));
                subscribeButton.setTextColor(ContextCompat.getColor(this, R.color.red_3));
            } else if (subscribeButton.isEnabled()) {
                subscribeButton.setText(getString(R.string.details_subscribe));
                colorViews(0, subscribeButton);
            }
            if (feedbackState.isUpVoted()) {
                votesText.setTextColor(ContextCompat.getColor(this, R.color.green_4));
                upButton.setEnabled(false);
            }
            if (feedbackState.isDownVoted()) { //TODO> Downvoting doesnt enable upvoting.
                votesText.setTextColor(ContextCompat.getColor(this, R.color.red_5));
                downButton.setEnabled(false);
            }
            if (feedbackState.isEqualVoted() && feedbackDetailsBean.getFeedbackBean().isPublic()) {
                colorViews(1, votesText);
                upButton.setEnabled(true);
                downButton.setEnabled(true);
            }
        }
    }

    @Override
    public void onButtonClicked(View view) {
        if (view.getId() == tagButton.getId()) {
            new PopUp(this)
                    .withTitle(getString(R.string.details_tags))
                    .withoutCancel()
                    .withMessage(StringUtility.concatWithDelimiter(", ", feedbackDetailsBean.getTags())).buildAndShow();
        } else if (view.getId() == imageButton.getId()) {
            final Dialog builder = new Dialog(FeedbackDetailsActivity.this);
            builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
            builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            ImageView imageView = new ImageView(FeedbackDetailsActivity.this);
            imageView.setImageBitmap(feedbackDetailsBean.getBitmap());
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder.dismiss();
                }
            });
            builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            builder.show();
        } else if (view.getId() == upButton.getId()) {
            RepositoryStub.sendUpVote(this, feedbackDetailsBean.getFeedbackBean());
            votesText.setText(feedbackDetailsBean.getFeedbackBean().upVote());
        } else if (view.getId() == downButton.getId()) {
            RepositoryStub.sendDownVote(this, feedbackDetailsBean.getFeedbackBean());
            votesText.setText(feedbackDetailsBean.getFeedbackBean().downVote());
        } else if (view.getId() == subscribeButton.getId()) {
            RepositoryStub.sendSubscriptionChange(this, feedbackDetailsBean.getFeedbackBean(), !feedbackState.isSubscribed());
        } else if (view.getId() == responseButton.getId() && mode == READING) {
            FeedbackResponseListItem item = new FeedbackResponseListItem(this, feedbackDetailsBean.getFeedbackBean(), null, configuration, EDITABLE);
            //Get to the Bottom
            scrollContainer.fullScroll(View.FOCUS_DOWN);
            responseLayout.addView(item);
            //Show new Entry
            scrollContainer.fullScroll(View.FOCUS_DOWN);
            item.requestInputFocus();
        } else if (view.getId() == makePublicButton.getId()) {
            DialogInterface.OnClickListener okClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    makePublicButton.setVisibility(View.INVISIBLE);
                    FeedbackService.getInstance(getApplicationContext()).makeFeedbackPublic(feedbackDetailsBean);
                    Toast.makeText(FeedbackDetailsActivity.this, R.string.details_published, Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }
            };
            new PopUp(this)
                    .withTitle(getString(R.string.details_make_public_title))
                    .withCustomOk("Confirm", okClickListener)
                    .withMessage(getString(R.string.details_make_public_content)).buildAndShow();
        } else if (view.getId() == reportButton.getId()) {
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
                        FeedbackService.getInstance(getApplicationContext()).reportFeedback(feedbackDetailsBean, report);
                        Toast.makeText(FeedbackDetailsActivity.this, R.string.details_report_sent, Toast.LENGTH_SHORT).show();
                        updateReportStatus(report);
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
        updateFeedbackState();
    }

    @Override
    public void onBackPressed() {
        if (creationMode) {
            startActivity(this, FeedbackHubActivity.class, true);
        } else {
            super.onBackPressed();
        }
    }

    public static void persistFeedbackResponseLocally(Context context, FeedbackBean bean, LocalConfigurationBean configuration, String feedbackResponse) {
        String userName = FeedbackDatabase.getInstance(context).readString(USER_NAME, USER_NAME_ANONYMOUS);
        boolean isDeveloper = FeedbackDatabase.getInstance(context).readBoolean(USER_IS_DEVELOPER, false);
        boolean isOwner = bean.getUserName() != null && bean.getUserName().equals(userName);
        FeedbackResponseBean responseBean = RepositoryStub.persist(bean, feedbackResponse, userName, isDeveloper, isOwner);
        FeedbackResponseListItem item = new FeedbackResponseListItem(context, bean, responseBean, configuration, FIXED);
        //Get to the Bottom
        scrollContainer.fullScroll(View.FOCUS_DOWN);
        responseLayout.addView(item);
        //Show new Entry
        scrollContainer.fullScroll(View.FOCUS_DOWN);
    }

    public void updateReportStatus(String report) {
        if (ACTIVE.check(getApplicationContext())) {
            if (report != null) {
                FeedbackDatabase.getInstance(getApplicationContext()).writeString(USER_REPORTED_FEEDBACK + feedbackDetailsBean.getFeedbackBean().getFeedbackId(), report);
            }
            if (FeedbackDatabase.getInstance(getApplicationContext()).readString(USER_REPORTED_FEEDBACK + feedbackDetailsBean.getFeedbackBean().getFeedbackId(), null) != null) {
                disableViews(reportButton);
            }
        }
    }

    /**
     * disable certain buttons on own feedback
     */
    public void updateOwnFeedbackCase() {
        if (ACTIVE.check(getApplicationContext())) {
            if (feedbackDetailsBean.getUserName().equals(FeedbackDatabase.getInstance(getApplicationContext()).readString(USER_NAME, null))) {
                disableViews(reportButton, subscribeButton);
            }
        }
    }
}

