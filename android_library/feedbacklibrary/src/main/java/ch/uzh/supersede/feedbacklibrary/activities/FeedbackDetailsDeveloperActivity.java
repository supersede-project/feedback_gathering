package ch.uzh.supersede.feedbacklibrary.activities;


import android.app.Dialog;
import android.content.*;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.ContentFrameLayout;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.*;
import android.widget.*;

import java.util.*;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.beans.*;
import ch.uzh.supersede.feedbacklibrary.components.buttons.FeedbackResponseListItem;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.stubs.RepositoryStub;
import ch.uzh.supersede.feedbacklibrary.utils.Enums.RESPONSE_MODE;
import ch.uzh.supersede.feedbacklibrary.utils.*;

import static ch.uzh.supersede.feedbacklibrary.components.buttons.FeedbackResponseListItem.RESPONSE_MODE.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.FEEDBACK_STATUS.CLOSED;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.RESPONSE_MODE.READING;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.ACTIVE;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class FeedbackDetailsDeveloperActivity extends AbstractBaseActivity {
    public static RESPONSE_MODE mode = READING;
    private FeedbackDetailsBean feedbackDetailsBean;
    private LocalFeedbackState feedbackState;
    private static LinearLayout responseLayout;
    private static ScrollView scrollContainer;
    private TextView votesText;
    private TextView userText;
    private Spinner status;
    private TextView titleText;
    private TextView descriptionText;
    private Button imageButton;
    private Button audioButton;
    private Button tagButton;
    private Button subscribeButton;
    private Button responseButton;
    private Button awardKarmaButton;
    private ArrayList<FeedbackResponseListItem> responseList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mode = READING;
        setContentView(R.layout.activity_feedback_details_developer);
        responseLayout = getView(R.id.details_developer_layout_scroll_layout, LinearLayout.class);
        scrollContainer = getView(R.id.details_developer_layout_scroll_container, ScrollView.class);
        votesText = getView(R.id.details_developer_text_votes, TextView.class);
        userText = getView(R.id.details_developer_text_user, TextView.class);
        status = getView(R.id.details_developer_spinner_status, Spinner.class);
        titleText = getView(R.id.details_developer_text_title, TextView.class);
        descriptionText = getView(R.id.details_developer_text_description, TextView.class);
        imageButton = getView(R.id.details_developer_button_images, Button.class);
        audioButton = getView(R.id.details_developer_button_audio, Button.class);
        tagButton = getView(R.id.details_developer_button_tags, Button.class);
        subscribeButton = getView(R.id.details_developer_button_subscribe, Button.class);
        responseButton = getView(R.id.details_developer_button_response, Button.class);
        awardKarmaButton = getView(R.id.details_developer_button_award_karma, Button.class);
        FeedbackBean feedbackBean = (FeedbackBean) getIntent().getSerializableExtra(EXTRA_KEY_FEEDBACK_BEAN);
        FeedbackDetailsBean cachedFeedbackDetailsBean = (FeedbackDetailsBean) getIntent().getSerializableExtra(EXTRA_KEY_FEEDBACK_DETAIL_BEAN);
        if (cachedFeedbackDetailsBean != null) {
            feedbackDetailsBean = cachedFeedbackDetailsBean;
            feedbackBean = feedbackDetailsBean.getFeedbackBean();
        } else if (feedbackBean != null) {
            feedbackDetailsBean = RepositoryStub.getFeedbackDetails(this, feedbackBean);
        }
        if (feedbackDetailsBean != null) {
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
            ArrayAdapter adapter= new ArrayAdapter(getApplicationContext(), R.layout.feedback_status_spinner_layout,
                    new String[]{Enums.FEEDBACK_STATUS.OPEN.getLabel(),
                            Enums.FEEDBACK_STATUS.IN_PROGRESS.getLabel(),
                            Enums.FEEDBACK_STATUS.REJECTED.getLabel(),
                            Enums.FEEDBACK_STATUS.DUPLICATE.getLabel(),
                            Enums.FEEDBACK_STATUS.CLOSED.getLabel()});
            status.setAdapter(adapter);
            status.setSelection(adapter.getPosition(feedbackDetailsBean.getFeedbackStatus().getLabel()));
            titleText.setText(getString(R.string.details_title, feedbackDetailsBean.getTitle()));
            descriptionText.setText(feedbackDetailsBean.getDescription());
        }else{
            this.onBackPressed();
        }
        //Disable all Database-related content, read only
        if (!ACTIVE.check(this,true)) {
            subscribeButton.setEnabled(false);
            responseButton.setEnabled(false);
        }
        colorViews(0,imageButton,audioButton, tagButton,subscribeButton,responseButton);
        colorViews(1,getView(R.id.details_developer_root,ContentFrameLayout.class));
        colorViews(2,userText,titleText, status,descriptionText);
        onPostCreate();
    }

    @Override
    public void onButtonClicked(View view) {
        if (view.getId() == tagButton.getId()) {
            new PopUp(this)
                    .withTitle(getString(R.string.details_tags))
                    .withoutCancel()
                    .withMessage(StringUtility.concatWithDelimiter(", ", feedbackDetailsBean.getTags())).buildAndShow();
        }else if (view.getId() == imageButton.getId()) {
            final Dialog builder = new Dialog(FeedbackDetailsDeveloperActivity.this);
            builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
            builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            ImageView imageView = new ImageView(FeedbackDetailsDeveloperActivity.this);
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
        }else if (view.getId() == awardKarmaButton.getId()){
            final EditText karmaInputText = new EditText(this);
            karmaInputText.setSingleLine();
            karmaInputText.setMaxLines(1);
            karmaInputText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
            karmaInputText.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
            DialogInterface.OnClickListener okClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (karmaInputText.getText().length()>0){
                        Integer karma = Integer.parseInt(karmaInputText.getText().toString());
                        RepositoryStub.sendKarma(feedbackDetailsBean,karma);
                        Toast.makeText(FeedbackDetailsDeveloperActivity.this,getString(R.string.details_developer_karma_awarded,String.valueOf(karma),feedbackDetailsBean.getUserName()),Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }else{
                        Toast.makeText(FeedbackDetailsDeveloperActivity.this,getString(R.string.details_developer_karma_error),Toast.LENGTH_SHORT).show();
                    }
                }
            };
            new PopUp(this)
                    .withTitle(getString(R.string.details_developer_award_karma_title))
                    .withInput(karmaInputText)
                    .withCustomOk("Confirm",okClickListener)
                    .withMessage(getString(R.string.details_developer_award_karma_content,feedbackDetailsBean.getUserName())).buildAndShow();
        }
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

