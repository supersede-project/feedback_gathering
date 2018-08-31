package ch.uzh.supersede.feedbacklibrary.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.*;
import android.widget.*;

import java.io.*;
import java.util.*;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.beans.*;
import ch.uzh.supersede.feedbacklibrary.components.buttons.FeedbackResponseListItem;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.models.FeedbackResponse;
import ch.uzh.supersede.feedbacklibrary.models.FeedbackVote;
import ch.uzh.supersede.feedbacklibrary.services.FeedbackService;
import ch.uzh.supersede.feedbacklibrary.services.IFeedbackServiceEventListener;
import ch.uzh.supersede.feedbacklibrary.stubs.RepositoryStub;
import ch.uzh.supersede.feedbacklibrary.utils.*;
import okhttp3.ResponseBody;

import static ch.uzh.supersede.feedbacklibrary.components.buttons.FeedbackResponseListItem.RESPONSE_MODE.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.ModelsConstants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.UserConstants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.RESPONSE_MODE.READING;
import static ch.uzh.supersede.feedbacklibrary.utils.PermissionUtility.USER_LEVEL.ACTIVE;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public abstract class AbstractFeedbackDetailsActivity extends AbstractBaseActivity implements IFeedbackServiceEventListener {
    public static Enums.RESPONSE_MODE mode = READING;
    protected static ScrollView scrollContainer;
    private static LinearLayout responseLayout;
    protected TextView votesText;
    protected TextView userText;
    protected TextView titleText;
    protected TextView descriptionText;
    protected Button imageButton;
    protected Button audioButton;
    protected Button tagButton;
    protected Button subscribeButton;
    protected Button responseButton;
    private FeedbackDetailsBean feedbackDetailsBean;
    private LocalFeedbackState feedbackState;
    private List<FeedbackResponseListItem> responseList = new ArrayList<>();
    private IFeedbackServiceEventListener callback;
    private String userName;
    private Class<?> callerClass;
    private String audioFilePath;
    private boolean isAudioPlaying;
    private boolean isAudioStopped;
    private MediaPlayer mediaPlayer;
    private boolean initiallyVoted = false;

    public static Enums.RESPONSE_MODE getMode() {
        return mode;
    }

    public static void setMode(Enums.RESPONSE_MODE mode) {
        AbstractFeedbackDetailsActivity.mode = mode;
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

    public LocalFeedbackState getFeedbackState() {
        return feedbackState;
    }

    public void setFeedbackState(LocalFeedbackState feedbackState) {
        this.feedbackState = feedbackState;
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
        return feedbackDetailsBean;
    }

    public void setFeedbackDetailsBean(FeedbackDetailsBean feedbackDetailsBean) {
        this.feedbackDetailsBean = feedbackDetailsBean;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    protected abstract void initViews();

    public void setCallerClass(Class<?> callerClass) {
        this.callerClass = callerClass;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        checkViewsNotNull();
        colorViews(0, imageButton, audioButton, tagButton, subscribeButton, responseButton);
        colorViews(configuration.getLastColorIndex(), userText, titleText, descriptionText);

        setFeedbackDetailsBean(getCachedFeedbackDetailsBean());
        initFeedbackDetailView();
        initPermissionCheck();
        setVisibilityAttachmentButtons();
    }

    protected void drawLayoutOutlines(int... layouts) {
        GradientDrawable gradientDrawable = null;
        for (int layout : layouts) {
            gradientDrawable = new GradientDrawable();
            gradientDrawable.setStroke(1, ColorUtility.isDark(configuration.getTopColors()[0]) ? Color.WHITE : Color.BLACK);
            findViewById(layout).setBackground(gradientDrawable);
        }
    }

    protected void checkViewsNotNull() {
        if (CompareUtility.notNull(responseButton, responseLayout, scrollContainer, votesText, userText, titleText, descriptionText, imageButton, audioButton, tagButton, subscribeButton)) {
            return;
        }
        Log.e(getClass().getSimpleName(), "Could not start activity duet to a configuration error.");
        onBackPressed();
    }

    protected FeedbackDetailsBean getCachedFeedbackDetailsBean() {
        FeedbackBean cachedFeedbackBean = (FeedbackBean) getIntent().getSerializableExtra(EXTRA_KEY_FEEDBACK_BEAN);
        FeedbackDetailsBean cachedFeedbackDetailsBean = (FeedbackDetailsBean) getIntent().getSerializableExtra(EXTRA_KEY_FEEDBACK_DETAIL_BEAN);
        return cachedFeedbackDetailsBean != null ? cachedFeedbackDetailsBean : RepositoryStub.getFeedbackDetails(this, cachedFeedbackBean);
    }

    protected void initFeedbackDetailView() {
        if (getFeedbackDetailsBean() != null) {
            updateFeedbackState();
            initiallyVoted = !getFeedbackState().isEqualVoted();
            for (FeedbackResponseBean bean : getFeedbackDetailsBean().getResponses()) {
                FeedbackResponseListItem feedbackResponseListItem = new FeedbackResponseListItem(this, getFeedbackDetailsBean().getFeedbackBean(), bean, configuration, this, FIXED);
                responseList.add(feedbackResponseListItem);
            }
            Collections.sort(responseList);
            for (FeedbackResponseListItem item : responseList) {
                responseLayout.addView(item);
            }
            votesText.setText(getFeedbackDetailsBean().getUpVotesAsText());
            userText.setText(getString(R.string.details_user, getFeedbackDetailsBean().getUserName()));

            titleText.setText(getString(R.string.details_title, getFeedbackDetailsBean().getTitle()));
            descriptionText.setText(getFeedbackDetailsBean().getDescription());
        } else {
            this.onBackPressed();
        }
    }

    protected void initPermissionCheck() {
        //Disable all Database-related content, read only
        if (!ACTIVE.check(this, true)) {
            subscribeButton.setEnabled(false);
            responseButton.setEnabled(false);
        } else {
            userName = FeedbackDatabase.getInstance(this).readString(USER_NAME, null);
        }
    }

    @Override
    public void onButtonClicked(View view) {
        if (view.getId() == tagButton.getId()) {
            handleTagButtonClicked();
        } else if (view.getId() == imageButton.getId()) {
            handleImageButtonClicked();
        } else if (view.getId() == audioButton.getId()) {
            handleAudioButtonClicked();
        } else if (view.getId() == subscribeButton.getId()) {
            handleSubscribeButtonClicked();
        } else if (view.getId() == responseButton.getId() && mode == READING) {
            handleResponseButtonClicked();
        }
    }

    protected void handleTagButtonClicked() {
        new PopUp(this)
                .withTitle(getString(R.string.details_tags))
                .withoutCancel()
                .withMessage(StringUtility.concatWithDelimiter(", ", getFeedbackDetailsBean().getTags())).buildAndShow();
    }

    protected void handleImageButtonClicked() {
        if (feedbackDetailsBean.getBitmapName() != null) { // Own, just created Feedback
            FeedbackService.getInstance(getApplicationContext()).getFeedbackImage(this, feedbackDetailsBean);
        } else {
            showImageDialog(feedbackDetailsBean.getBitmap());
        }
    }

    protected void handleAudioButtonClicked() {
        if (feedbackDetailsBean.getAudioFileName() != null) {
            if (isAudioPlaying) {
                stopAudio();
            } else {
                FeedbackService.getInstance(getApplicationContext()).getFeedbackAudio(this, feedbackDetailsBean);
            }
        } else {
            new PopUp(this)
                    .withTitle(getString(R.string.details_audio))
                    .withoutCancel()
                    .withMessage("No Audio").buildAndShow();
        }
    }

    protected void handleSubscribeButtonClicked() {
        RepositoryStub.sendSubscriptionChange(this, getFeedbackDetailsBean().getFeedbackBean(), !feedbackState.isSubscribed());
    }

    protected void handleResponseButtonClicked() {
        FeedbackResponseListItem item = new FeedbackResponseListItem(this, getFeedbackDetailsBean().getFeedbackBean(), null, configuration, this, EDITABLE);
        //Get to the Bottom
        scrollContainer.fullScroll(View.FOCUS_DOWN);
        responseLayout.addView(item);
        //Show new Entry
        scrollContainer.fullScroll(View.FOCUS_DOWN);
        item.requestInputFocus();
    }

    @Override
    public void onEventCompleted(EventType eventType, Object response) {
        switch (eventType) {
            case DELETE_FEEDBACK_MOCK:
            case DELETE_FEEDBACK:
                Toast.makeText(this, getString(R.string.details_developer_deleted), Toast.LENGTH_SHORT).show();

                Class<?> callerClass = ObjectUtility.getCallerClass(getIntent());
                Intent intent = new Intent(getApplicationContext(), callerClass);
                intent.putExtra(EXTRA_KEY_FEEDBACK_DELETION, true);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(this, callerClass, true, intent);
                break;
            case GET_FEEDBACK_IMAGE:
            case GET_FEEDBACK_IMAGE_MOCK:
                if (response instanceof ResponseBody) {
                    try {
                        showImageDialog(((ResponseBody) response).bytes());
                    } catch (IOException e) {
                        showImageDialog(new byte[0]);
                    }
                }
                break;
            case GET_FEEDBACK_AUDIO:
            case GET_FEEDBACK_AUDIO_MOCK:
                if (response instanceof ResponseBody) {
                    try {
                        playAudio(((ResponseBody) response).bytes());
                    } catch (IOException e) {
                        Log.e("PlayAudio", "playing audio failed");
                    }
                }
                break;
            case CREATE_FEEDBACK_RESPONSE:
            case CREATE_FEEDBACK_RESPONSE_MOCK:
                if (response instanceof FeedbackResponse) {
                    Toast.makeText(this, R.string.details_response_sent, Toast.LENGTH_SHORT).show();
                    persistFeedbackResponseLocally((FeedbackResponse) response);
                }
                break;
            case DELETE_FEEDBACK_RESPONSE:
            case DELETE_FEEDBACK_RESPONSE_MOCK:
                FeedbackResponseListItem removeItem = null;
                for (int i = 0; i < responseLayout.getChildCount(); i++) {
                    View layoutItem = responseLayout.getChildAt(i);
                    if (layoutItem instanceof FeedbackResponseListItem) {
                        FeedbackResponseListItem item = (FeedbackResponseListItem) layoutItem;

                        if (item.isDeleted()) {
                            removeItem = item;
                        }
                    }
                }
                responseList.remove(removeItem);
                responseLayout.removeView(removeItem);
                break;
            case CREATE_VOTE:
            case CREATE_VOTE_MOCK:
                //NOP
               break;
            default:
                break;
        }
    }

    @Override
    public void onEventFailed(EventType eventType, Object response) {
        switch (eventType) {
            case GET_FEEDBACK_IMAGE:
            case GET_FEEDBACK_IMAGE_MOCK:
                showImageDialog(new byte[0]);
                break;
            case GET_FEEDBACK_AUDIO:
            case GET_FEEDBACK_AUDIO_MOCK:
                showImageDialog(new byte[0]);
                break;
            default:
        }
        Log.w(getClass().getSimpleName(), getResources().getString(R.string.api_service_event_failed, eventType, response.toString()));
    }

    @Override
    public void onConnectionFailed(EventType eventType) {
        switch (eventType) {
            case GET_FEEDBACK_IMAGE:
            case GET_FEEDBACK_IMAGE_MOCK:
                showImageDialog(new byte[0]);
                break;
            case GET_FEEDBACK_AUDIO:
            case GET_FEEDBACK_AUDIO_MOCK:
                showImageDialog(new byte[0]);
                break;
            default:
        }
        Log.w(getClass().getSimpleName(), getResources().getString(R.string.api_service_connection_failed, eventType));
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
            return !feedbackStatus.equalsIgnoreCase(getFeedbackDetailsBean().getFeedbackStatus().getLabel());
        }
        return false;
    }

    protected void persistFeedbackResponseLocally(FeedbackResponse feedbackResponse) {
        boolean isDeveloper = FeedbackDatabase.getInstance(getApplicationContext()).readBoolean(USER_IS_DEVELOPER, false);
        boolean isOwner = feedbackDetailsBean.getUserName() != null && feedbackDetailsBean.getUserName().equals(userName);
        FeedbackResponseBean responseBean = RepositoryStub.persist(feedbackDetailsBean.getFeedbackBean(), feedbackResponse.getId(), feedbackResponse.getContent(), userName, isDeveloper, isOwner);
        FeedbackResponseListItem item = new FeedbackResponseListItem(this, feedbackDetailsBean.getFeedbackBean(), responseBean, configuration, this, FIXED);
        //Get to the Bottom
        scrollContainer.fullScroll(View.FOCUS_DOWN);
        responseLayout.addView(item);
        //Show new Entry
        scrollContainer.fullScroll(View.FOCUS_DOWN);
    }

    protected void setVisibilityAttachmentButtons() {
        if (getFeedbackDetailsBean().getBitmapName() == null) {
            disableViews(imageButton);
        }
        if (getFeedbackDetailsBean().getTags() == null || getFeedbackDetailsBean().getTags().length == 0) {
            disableViews(tagButton);
        }
        if (getFeedbackDetailsBean().getAudioFileName() == null) {
            disableViews(audioButton);
        }

    }

    protected void showImageDialog(byte[] bitmap) {
        Bitmap bitmapImage = ImageUtility.bytesToImage(bitmap);
        if (bitmapImage != null) {
            showImageDialog(bitmapImage);
        }
    }

    protected void showImageDialog(Bitmap bitmap) {
        final Dialog builder = new Dialog(this);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = builder.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
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

    protected void playAudio(byte[] audio) {
        try {
            audioFilePath = getApplicationContext().getDir(AUDIO_DIR, Context.MODE_PRIVATE).getAbsolutePath() + PATH_DELIMITER + AUDIO_FILENAME + "." + AUDIO_EXTENSION;
            FileOutputStream stream = new FileOutputStream(audioFilePath);
            stream.write(audio);
            stream.close();
            File audioFile = new File(audioFilePath);
            if (audioFile.length() != 0) {
                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(audioFilePath);
                    mediaPlayer.prepare();
                }
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        stopAudio();
                    }
                });
                mediaPlayer.start();
                mediaPlayer.setVolume(50, 50);
                audioButton.setText(R.string.details_audio_stop);
                isAudioPlaying = true;
            }
        } catch (IOException e) {
            Log.e("Audio",e.getMessage());
        }
    }

    @Override
    protected void onPause() {
        execFinalize();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        execFinalize();
        if (isAudioPlaying) {
            stopAudio();
        }
        super.onDestroy();
    }

    protected void execFinalize() {
        if (!getFeedbackState().isEqualVoted()) {
            FeedbackService.getInstance(getApplicationContext()).createVote(this, feedbackDetailsBean, getFeedbackState().isUpVoted() ? 1 : -1, userName);
        } else if (getFeedbackState().isEqualVoted() && initiallyVoted) {
            FeedbackService.getInstance(getApplicationContext()).createVote(this, feedbackDetailsBean, 0, userName);
        }
        FeedbackService.getInstance(getApplicationContext()).createSubscription(this, feedbackDetailsBean.getFeedbackBean());
    }

    private void stopAudio() {
        mediaPlayer.stop();
        isAudioStopped = true;
        isAudioPlaying = false;
        audioButton.setText(R.string.details_audio);
        mediaPlayer.release();
        mediaPlayer = null;
    }
}
