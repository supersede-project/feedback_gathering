package ch.uzh.supersede.feedbacklibrary.activities;


import android.app.Dialog;
import android.content.*;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.*;
import android.widget.*;

import java.util.*;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.beans.*;
import ch.uzh.supersede.feedbacklibrary.components.buttons.FeedbackResponseListItem;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.stubs.RepositoryStub;
import ch.uzh.supersede.feedbacklibrary.utils.*;
import ch.uzh.supersede.feedbacklibrary.utils.Enums.RESPONSE_MODE;

import static ch.uzh.supersede.feedbacklibrary.components.buttons.FeedbackResponseListItem.RESPONSE_MODE.EDITABLE;
import static ch.uzh.supersede.feedbacklibrary.components.buttons.FeedbackResponseListItem.RESPONSE_MODE.FIXED;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;
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
    private Button audioButton;
    private Button labelButton;
    private Button subscribeButton;
    private Button responseButton;
    private Button makePublicButton;
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
        labelButton = getView(R.id.details_button_labels, Button.class);
        subscribeButton = getView(R.id.details_button_subscribe, Button.class);
        responseButton = getView(R.id.details_button_response, Button.class);
        makePublicButton = getView(R.id.details_button_make_public, Button.class);
        FeedbackBean feedbackBean = (FeedbackBean) getIntent().getSerializableExtra(EXTRA_KEY_FEEDBACK_BEAN);
        if (feedbackBean != null) {
            feedbackDetailsBean = RepositoryStub.getFeedbackDetails(this, feedbackBean);
            if (feedbackDetailsBean != null) {
                updateFeedbackState();
                for (FeedbackResponseBean bean : feedbackDetailsBean.getResponses()) {
                    FeedbackResponseListItem feedbackResponseListItem = new FeedbackResponseListItem(this,feedbackBean, bean, configuration, FIXED);
                    responseList.add(feedbackResponseListItem);
                }
                Collections.sort(responseList);
                for (FeedbackResponseListItem item : responseList) {
                    responseLayout.addView(item);
                }
                votesText.setText(feedbackDetailsBean.getUpVotesAsText());
                userText.setText(getString(R.string.details_user,feedbackDetailsBean.getUserName()));
                statusText.setText(getString(R.string.details_status,feedbackDetailsBean.getFeedbackStatus().getLabel()));
                titleText.setText(getString(R.string.details_title,feedbackDetailsBean.getTitle()));
                descriptionText.setText(feedbackDetailsBean.getDescription());
            }
            if (feedbackBean.isOwnFeedback(getApplicationContext())){
                upButton.setEnabled(false);
                downButton.setEnabled(false);
                if (!feedbackBean.isPublic()){
                    makePublicButton.setVisibility(View.VISIBLE);
                }
            }
        }
        onPostCreate();
    }

    private void updateFeedbackState() {
        feedbackState = FeedbackDatabase.getInstance(this).getFeedbackState(feedbackDetailsBean.getFeedbackBean());
        if (feedbackState.isSubscribed()){
            subscribeButton.setText(getString(R.string.details_unsubscribe));
            subscribeButton.setTextColor(ContextCompat.getColor(this, R.color.red_3));
        }else{
            subscribeButton.setText(getString(R.string.details_subscribe));
            subscribeButton.setTextColor(ContextCompat.getColor(this, R.color.black));
        }
        if (feedbackState.isUpVoted()){
            votesText.setTextColor(ContextCompat.getColor(this, R.color.green_4));
            upButton.setEnabled(false);
        }
        if (feedbackState.isDownVoted()){
            votesText.setTextColor(ContextCompat.getColor(this, R.color.red_5));
            downButton.setEnabled(false);
        }
        if (feedbackState.isEqualVoted()){
            votesText.setTextColor(ContextCompat.getColor(this, R.color.black));
            upButton.setEnabled(true);
            downButton.setEnabled(true);
        }
    }

    @Override
    public void onButtonClicked(View view) {
        if (view.getId() == labelButton.getId()) {
            new PopUp(this)
                    .withTitle(getString(R.string.details_labels))
                    .withoutCancel()
                    .withMessage(StringUtility.concatWithDelimiter(", ", feedbackDetailsBean.getLabels())).buildAndShow();
        }else if (view.getId() == imageButton.getId()) {
            final Dialog builder = new Dialog(FeedbackDetailsActivity.this);
            builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
            builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            ImageView imageView = new ImageView(FeedbackDetailsActivity.this);
            imageView.setImageBitmap(RepositoryStub.loadFeedbackImage(FeedbackDetailsActivity.this,feedbackDetailsBean));
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
        }else if (view.getId() == upButton.getId()) {
            RepositoryStub.sendUpVote(this, feedbackDetailsBean.getFeedbackBean());
            votesText.setText(feedbackDetailsBean.getFeedbackBean().upVote());
        }else if (view.getId() == downButton.getId()){
            RepositoryStub.sendDownVote(this, feedbackDetailsBean.getFeedbackBean());
            votesText.setText(feedbackDetailsBean.getFeedbackBean().downVote());
        }else if (view.getId() == subscribeButton.getId()){
            RepositoryStub.sendSubscriptionChange(this, feedbackDetailsBean.getFeedbackBean(), !feedbackState.isSubscribed());
        }else if (view.getId() == responseButton.getId() && mode == READING){
            FeedbackResponseListItem item = new FeedbackResponseListItem(this,feedbackDetailsBean.getFeedbackBean(),null,configuration,EDITABLE);
            //Get to the Bottom
            scrollContainer.fullScroll(View.FOCUS_DOWN);
            responseLayout.addView(item);
            //Show new Entry
            scrollContainer.fullScroll(View.FOCUS_DOWN);
            item.requestInputFocus();
        }else if (view.getId() == makePublicButton.getId()){
            DialogInterface.OnClickListener okClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    makePublicButton.setVisibility(View.INVISIBLE);
                    RepositoryStub.makeFeedbackPublic(feedbackDetailsBean);
                    Toast.makeText(FeedbackDetailsActivity.this,R.string.details_published,Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }
            };
            new PopUp(this)
                    .withTitle(getString(R.string.details_make_public_title))
                    .withCustomOk("Confirm",okClickListener)
                    .withMessage(getString(R.string.details_make_public_content)).buildAndShow();
        }
        updateFeedbackState();
    }

    public static void persistFeedbackResponseLocally(Context context, FeedbackBean bean, LocalConfigurationBean configuration, String feedbackResponse) {
            String userName = FeedbackDatabase.getInstance(context).readString(USER_NAME, USER_NAME_ANONYMOUS);
            boolean isDeveloper = FeedbackDatabase.getInstance(context).readBoolean(IS_DEVELOPER, false);
            boolean isOwner = bean.getUserName() != null && bean.getUserName().equals(userName);
            FeedbackResponseBean responseBean = RepositoryStub.persist(bean, feedbackResponse, userName, isDeveloper, isOwner);
            FeedbackResponseListItem item = new FeedbackResponseListItem(context, bean, responseBean, configuration, FIXED);
            //Get to the Bottom
            scrollContainer.fullScroll(View.FOCUS_DOWN);
            responseLayout.addView(item);
            //Show new Entry
            scrollContainer.fullScroll(View.FOCUS_DOWN);
    }
}

