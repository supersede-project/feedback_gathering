package ch.uzh.supersede.feedbacklibrary.activities;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.ContentFrameLayout;
import android.view.*;
import android.widget.*;

import java.util.*;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.beans.*;
import ch.uzh.supersede.feedbacklibrary.components.buttons.FeedbackResponseListItem;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.services.FeedbackService;
import ch.uzh.supersede.feedbacklibrary.services.IFeedbackServiceEventListener;
import ch.uzh.supersede.feedbacklibrary.stubs.RepositoryStub;
import ch.uzh.supersede.feedbacklibrary.utils.*;

import static ch.uzh.supersede.feedbacklibrary.components.buttons.FeedbackResponseListItem.RESPONSE_MODE.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.UserConstants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.RESPONSE_MODE.READING;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.ACTIVE;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public abstract class AbstractFeedbackDetailsActivity extends AbstractBaseActivity implements IFeedbackServiceEventListener {
    public static Enums.RESPONSE_MODE mode = READING;
    private final FeedbackDetailsBean[] activeFeedbackDetailsBean = new FeedbackDetailsBean[1];
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
    private List<FeedbackResponseListItem> responseList = new ArrayList<>();
    private IFeedbackServiceEventListener callback;

    public static Enums.RESPONSE_MODE getMode() {
        return mode;
    }

    public static void setMode(Enums.RESPONSE_MODE mode) {
        AbstractFeedbackDetailsActivity.mode = mode;
    }

    public FeedbackDetailsBean[] getActiveFeedbackDetailsBean() {
        return activeFeedbackDetailsBean;
    }

    public LocalFeedbackState getFeedbackState() {
        return feedbackState;
    }

    public void setFeedbackState(LocalFeedbackState feedbackState) {
        this.feedbackState = feedbackState;
    }

    public static LinearLayout getResponseLayout() {
        return responseLayout;
    }

    public static void setResponseLayout(LinearLayout responseLayout) {
        AbstractFeedbackDetailsActivity.responseLayout = responseLayout;
    }

    public static ScrollView getScrollContainer() {
        return scrollContainer;
    }

    public static void setScrollContainer(ScrollView scrollContainer) {
        AbstractFeedbackDetailsActivity.scrollContainer = scrollContainer;
    }

    public TextView getVotesText() {
        return votesText;
    }

    public void setVotesText(TextView votesText) {
        this.votesText = votesText;
    }

    public TextView getUserText() {
        return userText;
    }

    public void setUserText(TextView userText) {
        this.userText = userText;
    }

    public Spinner getStatus() {
        return status;
    }

    public void setStatus(Spinner status) {
        this.status = status;
    }

    public TextView getTitleText() {
        return titleText;
    }

    public void setTitleText(TextView titleText) {
        this.titleText = titleText;
    }

    public TextView getDescriptionText() {
        return descriptionText;
    }

    public void setDescriptionText(TextView descriptionText) {
        this.descriptionText = descriptionText;
    }

    public Button getImageButton() {
        return imageButton;
    }

    public void setImageButton(Button imageButton) {
        this.imageButton = imageButton;
    }

    public Button getAudioButton() {
        return audioButton;
    }

    public void setAudioButton(Button audioButton) {
        this.audioButton = audioButton;
    }

    public Button getTagButton() {
        return tagButton;
    }

    public void setTagButton(Button tagButton) {
        this.tagButton = tagButton;
    }

    public Button getSubscribeButton() {
        return subscribeButton;
    }

    public void setSubscribeButton(Button subscribeButton) {
        this.subscribeButton = subscribeButton;
    }

    public Button getResponseButton() {
        return responseButton;
    }

    public void setResponseButton(Button responseButton) {
        this.responseButton = responseButton;
    }

    public List<FeedbackResponseListItem> getResponseList() {
        return responseList;
    }

    public void setResponseList(List<FeedbackResponseListItem> responseList) {
        this.responseList = responseList;
    }

    public IFeedbackServiceEventListener getCallback() {
        return callback;
    }

    public void setCallback(IFeedbackServiceEventListener callback) {
        this.callback = callback;
    }

    public FeedbackDetailsBean getFeedbackDetailsBean() {
        return activeFeedbackDetailsBean[0];
    }

    public void setFeedbackDetailsBean(FeedbackDetailsBean feedbackDetailsBean) {
        this.activeFeedbackDetailsBean[0] = feedbackDetailsBean;
    }


    protected abstract void initButtons();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initButtons();

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
                FeedbackResponseListItem feedbackResponseListItem = new FeedbackResponseListItem(this, feedbackBean, bean, configuration, this, FIXED);
                responseList.add(feedbackResponseListItem);
            }
            Collections.sort(responseList);
            for (FeedbackResponseListItem item : responseList) {
                responseLayout.addView(item);
            }
            votesText.setText(getFeedbackDetailsBean().getUpVotesAsText());
            userText.setText(getString(R.string.details_user, getFeedbackDetailsBean().getUserName()));
            ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.feedback_status_spinner_layout,
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
                    if (isStatusNew(feedbackStatus)) {
                        FeedbackService.getInstance(getApplicationContext()).editFeedbackStatus(callback, getFeedbackDetailsBean(), (String) feedbackStatus);
                        Toast.makeText(getApplicationContext(), getString(R.string.details_developer_status_updated), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    //NOP
                }

            });
            titleText.setText(getString(R.string.details_title, getFeedbackDetailsBean().getTitle()));
            descriptionText.setText(getFeedbackDetailsBean().getDescription());
        } else {
            this.onBackPressed();
        }
        //Disable all Database-related content, read only
        if (!ACTIVE.check(this, true)) {
            subscribeButton.setEnabled(false);
            responseButton.setEnabled(false);
        }
        colorViews(0, imageButton, audioButton, tagButton, subscribeButton, responseButton);
        colorViews(1, getView(R.id.details_developer_root, ContentFrameLayout.class));
        colorViews(2, userText, titleText, status, descriptionText);
    }

    @Override
    public void onButtonClicked(View view) {
        if (view.getId() == tagButton.getId()) {
            new PopUp(this)
                    .withTitle(getString(R.string.details_tags))
                    .withoutCancel()
                    .withMessage(StringUtility.concatWithDelimiter(", ", getFeedbackDetailsBean().getTags())).buildAndShow();
        } else if (view.getId() == imageButton.getId()) {
            if (feedbackDetailsBean.getBitmapName() != null) { // Own, just created Feedback
                FeedbackService.getInstance(getApplicationContext()).getFeedbackImage(this, feedbackDetailsBean);
            } else {
                showImageDialog(feedbackDetailsBean.getBitmap());
            }
        } else if (view.getId() == subscribeButton.getId()) {
            RepositoryStub.sendSubscriptionChange(this, getFeedbackDetailsBean().getFeedbackBean(), !feedbackState.isSubscribed());
        } else if (view.getId() == responseButton.getId() && mode == READING) {
            FeedbackResponseListItem item = new FeedbackResponseListItem(this, getFeedbackDetailsBean().getFeedbackBean(), null, configuration, this, EDITABLE);
            //Get to the Bottom
            scrollContainer.fullScroll(View.FOCUS_DOWN);
            responseLayout.addView(item);
            //Show new Entry
            scrollContainer.fullScroll(View.FOCUS_DOWN);
            item.requestInputFocus();
        }
    }

    @Override
    public void onEventCompleted(EventType eventType, Object response) {
        switch (eventType) {
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

    protected void updateFeedbackState() {
        if (ACTIVE.check(this, true)) {
            feedbackState = FeedbackDatabase.getInstance(this).getFeedbackState(getFeedbackDetailsBean().getFeedbackBean());
            if (feedbackState.isSubscribed() && subscribeButton.isEnabled()) {
                subscribeButton.setText(getString(R.string.details_unsubscribe));
                subscribeButton.setTextColor(ContextCompat.getColor(this, R.color.red_3));
            } else if (subscribeButton.isEnabled()) {
                subscribeButton.setText(getString(R.string.details_subscribe));
                colorViews(0, subscribeButton);
            }
        }
    }

    protected boolean isStatusNew(Object item) {
        if (item instanceof String && getFeedbackDetailsBean() != null) {
            String feedbackStatus = (String) item;
            return !feedbackStatus.equals(getFeedbackDetailsBean().getFeedbackStatus().getLabel());
        }
        return false;
    }

    protected void persistFeedbackResponseLocally(String feedbackResponse) {
        String userName = FeedbackDatabase.getInstance(getApplicationContext()).readString(USER_NAME, USER_NAME_ANONYMOUS);
        boolean isDeveloper = FeedbackDatabase.getInstance(getApplicationContext()).readBoolean(USER_IS_DEVELOPER, false);
        boolean isOwner = activeFeedbackDetailsBean[0].getUserName() != null && activeFeedbackDetailsBean[0].getUserName().equals(userName);
        FeedbackResponseBean responseBean = RepositoryStub.persist(activeFeedbackDetailsBean[0].getFeedbackBean(), feedbackResponse, userName, isDeveloper, isOwner);
        FeedbackResponseListItem item = new FeedbackResponseListItem(this, activeFeedbackDetailsBean[0].getFeedbackBean(), responseBean, configuration, this, FIXED);
        //Get to the Bottom
        scrollContainer.fullScroll(View.FOCUS_DOWN);
        responseLayout.addView(item);
        //Show new Entry
        scrollContainer.fullScroll(View.FOCUS_DOWN);
    }

    private void showImageDialog(byte[] bitmap) {
        Bitmap bitmapImage = ImageUtility.bytesToImage(bitmap);
        if (bitmapImage != null) {
            showImageDialog(bitmapImage);
        }
    }

    private void showImageDialog(Bitmap bitmap) {
        final Dialog builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(bitmap);
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
    }

}
