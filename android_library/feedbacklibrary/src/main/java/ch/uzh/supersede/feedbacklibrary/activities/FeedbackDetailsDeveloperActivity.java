package ch.uzh.supersede.feedbacklibrary.activities;


import android.app.Dialog;
import android.content.*;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.*;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.*;
import android.widget.*;

import java.util.*;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.beans.*;
import ch.uzh.supersede.feedbacklibrary.components.buttons.FeedbackResponseListItem;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.services.*;
import ch.uzh.supersede.feedbacklibrary.stubs.RepositoryStub;
import ch.uzh.supersede.feedbacklibrary.utils.*;
import ch.uzh.supersede.feedbacklibrary.utils.Enums.RESPONSE_MODE;

import static ch.uzh.supersede.feedbacklibrary.components.buttons.FeedbackResponseListItem.RESPONSE_MODE.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.UserConstants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.RESPONSE_MODE.READING;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.ACTIVE;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class FeedbackDetailsDeveloperActivity extends AbstractBaseActivity implements IFeedbackServiceEventListener {
    public static RESPONSE_MODE mode = READING;
    private final FeedbackDetailsBean[] activeFeedbackDetailsBean = new FeedbackDetailsBean[1];
    private LocalFeedbackState feedbackState;
    private static LinearLayout responseLayout;
    private static ScrollView scrollContainer;
    private TextView votesText;
    private TextView userText;
    private Spinner status;
    private TextView titleText;
    private TextView descriptionText;
    private Button deleteButton;
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
        deleteButton = getView(R.id.details_developer_button_delete, Button.class);
        imageButton = getView(R.id.details_developer_button_images, Button.class);
        audioButton = getView(R.id.details_developer_button_audio, Button.class);
        tagButton = getView(R.id.details_developer_button_tags, Button.class);
        subscribeButton = getView(R.id.details_developer_button_subscribe, Button.class);
        responseButton = getView(R.id.details_developer_button_response, Button.class);
        awardKarmaButton = getView(R.id.details_developer_button_award_karma, Button.class);
        FeedbackBean feedbackBean = (FeedbackBean) getIntent().getSerializableExtra(EXTRA_KEY_FEEDBACK_BEAN);
        FeedbackDetailsBean cachedFeedbackDetailsBean = (FeedbackDetailsBean) getIntent().getSerializableExtra(EXTRA_KEY_FEEDBACK_DETAIL_BEAN);
        if (cachedFeedbackDetailsBean != null) {
            setFeedbackDetailsBean(cachedFeedbackDetailsBean);
            feedbackBean = cachedFeedbackDetailsBean.getFeedbackBean();
        } else if (feedbackBean != null) {
            setFeedbackDetailsBean(RepositoryStub.getFeedbackDetails(this, feedbackBean));
        }
        if (getFeedbackDetailsBean() != null) {
            updateFeedbackState();
            for (FeedbackResponseBean bean : getFeedbackDetailsBean().getResponses()) {
                FeedbackResponseListItem feedbackResponseListItem = new FeedbackResponseListItem(this, feedbackBean, bean, configuration, this,FIXED);
                responseList.add(feedbackResponseListItem);
            }
            Collections.sort(responseList);
            for (FeedbackResponseListItem item : responseList) {
                responseLayout.addView(item);
            }
            votesText.setText(getFeedbackDetailsBean().getUpVotesAsText());
            userText.setText(getString(R.string.details_user, getFeedbackDetailsBean().getUserName()));
            ArrayAdapter adapter= new ArrayAdapter(getApplicationContext(), R.layout.feedback_status_spinner_layout,
                    new String[]{Enums.FEEDBACK_STATUS.OPEN.getLabel(),
                            Enums.FEEDBACK_STATUS.IN_PROGRESS.getLabel(),
                            Enums.FEEDBACK_STATUS.REJECTED.getLabel(),
                            Enums.FEEDBACK_STATUS.DUPLICATE.getLabel(),
                            Enums.FEEDBACK_STATUS.CLOSED.getLabel()});
            status.setAdapter(adapter);
            status.setSelection(adapter.getPosition(getFeedbackDetailsBean().getFeedbackStatus().getLabel()));
            status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    Object feedbackStatus = ((AppCompatSpinner) parentView).getAdapter().getItem(position);
                    if (isStatusNew(feedbackStatus)){
                        FeedbackService.getInstance(getApplicationContext()).editFeedbackStatus(FeedbackDetailsDeveloperActivity.this, getFeedbackDetailsBean(), (String) feedbackStatus);
                        Toast.makeText(FeedbackDetailsDeveloperActivity.this,getString(R.string.details_developer_status_updated),Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    //NOP
                }

            });
            titleText.setText(getString(R.string.details_title, getFeedbackDetailsBean().getTitle()));
            descriptionText.setText(getFeedbackDetailsBean().getDescription());
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

    private boolean isStatusNew(Object item) {
        if (item instanceof String && getFeedbackDetailsBean()!= null){
           String feedbackStatus = (String) item;
           return !feedbackStatus.equals(getFeedbackDetailsBean().getFeedbackStatus().getLabel());
        }
        return false;
    }

    public FeedbackDetailsBean getFeedbackDetailsBean() {
        return activeFeedbackDetailsBean[0];
    }

    public void setFeedbackDetailsBean(FeedbackDetailsBean feedbackDetailsBean) {
        this.activeFeedbackDetailsBean[0] = feedbackDetailsBean;
    }

    private void updateFeedbackState() {
        if (ACTIVE.check(this,true)) {
            feedbackState = FeedbackDatabase.getInstance(this).getFeedbackState(getFeedbackDetailsBean().getFeedbackBean());
            if (feedbackState.isSubscribed() && subscribeButton.isEnabled()) {
                subscribeButton.setText(getString(R.string.details_unsubscribe));
                subscribeButton.setTextColor(ContextCompat.getColor(this, R.color.red_3));
            } else if (subscribeButton.isEnabled()){
                subscribeButton.setText(getString(R.string.details_subscribe));
                colorViews(0,subscribeButton);
            }
        }
    }

    @Override
    public void onButtonClicked(View view) {
        if (view.getId() == tagButton.getId()) {
            new PopUp(this)
                    .withTitle(getString(R.string.details_tags))
                    .withoutCancel()
                    .withMessage(StringUtility.concatWithDelimiter(", ", getFeedbackDetailsBean().getTags())).buildAndShow();
        }else if (view.getId() == imageButton.getId()) {
            final Dialog builder = new Dialog(FeedbackDetailsDeveloperActivity.this);
            builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
            builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            ImageView imageView = new ImageView(FeedbackDetailsDeveloperActivity.this);
            imageView.setImageBitmap(getFeedbackDetailsBean().getBitmap());
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
            RepositoryStub.sendSubscriptionChange(this, getFeedbackDetailsBean().getFeedbackBean(), !feedbackState.isSubscribed());
        }else if (view.getId() == responseButton.getId() && mode == READING){
            FeedbackResponseListItem item = new FeedbackResponseListItem(this,getFeedbackDetailsBean().getFeedbackBean(),null,configuration,this,EDITABLE);
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
                        RepositoryStub.sendKarma(getFeedbackDetailsBean(),karma);
                        Toast.makeText(FeedbackDetailsDeveloperActivity.this,getString(R.string.details_developer_karma_awarded,String.valueOf(karma),getFeedbackDetailsBean().getUserName()),Toast.LENGTH_SHORT).show();
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
                    .withMessage(getString(R.string.details_developer_award_karma_content,getFeedbackDetailsBean().getUserName())).buildAndShow();
        }else if (view.getId() == deleteButton.getId()){
            DialogInterface.OnClickListener okClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which==Dialog.BUTTON_POSITIVE){
                        FeedbackService.getInstance(getApplicationContext()).deleteFeedback(FeedbackDetailsDeveloperActivity.this, getFeedbackDetailsBean());
                        Toast.makeText(FeedbackDetailsDeveloperActivity.this,getString(R.string.details_developer_deleted),Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                        Intent intent = new Intent(getApplicationContext(), FeedbackListActivity.class);
                        intent.putExtra(EXTRA_KEY_FEEDBACK_DELETION,true);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(FeedbackDetailsDeveloperActivity.this,FeedbackListActivity.class,true, intent);
                    }
                }
            };
            new PopUp(this)
                    .withTitle(getString(R.string.details_developer_delete_confirm_title))
                    .withCustomOk("Confirm",okClickListener)
                    .withMessage(getString(R.string.details_developer_delete_confirm)).buildAndShow();
        }
        updateFeedbackState();
    }

    private void persistFeedbackResponseLocally(String feedbackResponse) {
        String userName = FeedbackDatabase.getInstance(getApplicationContext()).readString(USER_NAME, USER_NAME_ANONYMOUS);
        boolean isDeveloper = FeedbackDatabase.getInstance(getApplicationContext()).readBoolean(USER_IS_DEVELOPER, false);
        boolean isOwner = activeFeedbackDetailsBean[0].getUserName() != null && activeFeedbackDetailsBean[0].getUserName().equals(userName);
        FeedbackResponseBean responseBean = RepositoryStub.persist(activeFeedbackDetailsBean[0].getFeedbackBean(), feedbackResponse, userName, isDeveloper, isOwner);
        FeedbackResponseListItem item = new FeedbackResponseListItem(this, activeFeedbackDetailsBean[0].getFeedbackBean(), responseBean, configuration,this, FIXED);
        //Get to the Bottom
        scrollContainer.fullScroll(View.FOCUS_DOWN);
        responseLayout.addView(item);
        //Show new Entry
        scrollContainer.fullScroll(View.FOCUS_DOWN);
    }

    @Override
    public void onEventCompleted(EventType eventType, Object response) {
        switch (eventType){
            case CREATE_FEEDBACK_DELETION_MOCK:
            case DELETE_FEEDBACK:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    public void onEventFailed(EventType eventType, Object response) {
        //TDOO: Implement
    }

    @Override
    public void onConnectionFailed(EventType eventType) {
        //TODO: Implement
    }
}

