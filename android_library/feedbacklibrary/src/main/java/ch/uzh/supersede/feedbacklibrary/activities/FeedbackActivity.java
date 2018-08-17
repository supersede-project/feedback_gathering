package ch.uzh.supersede.feedbacklibrary.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.beans.FeedbackDetailsBean;
import ch.uzh.supersede.feedbacklibrary.components.views.AbstractFeedbackPartView;
import ch.uzh.supersede.feedbacklibrary.components.views.AudioFeedbackView;
import ch.uzh.supersede.feedbacklibrary.components.views.ScreenshotFeedbackView;
import ch.uzh.supersede.feedbacklibrary.models.AbstractFeedbackPart;
import ch.uzh.supersede.feedbacklibrary.models.Feedback;
import ch.uzh.supersede.feedbacklibrary.services.FeedbackService;
import ch.uzh.supersede.feedbacklibrary.services.IFeedbackServiceEventListener;
import ch.uzh.supersede.feedbacklibrary.stubs.OrchestratorStub;
import ch.uzh.supersede.feedbacklibrary.utils.DialogUtils;
import ch.uzh.supersede.feedbacklibrary.utils.FeedbackUtility;
import ch.uzh.supersede.feedbacklibrary.utils.ImageUtility;
import ch.uzh.supersede.feedbacklibrary.utils.VersionUtility;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.ActivitiesConstants.FEEDBACK_ACTIVITY_TAG;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;

@SuppressWarnings({"squid:MaximumInheritanceDepth", "squid:S1170"})
public class FeedbackActivity extends AbstractBaseActivity implements AudioFeedbackView.MultipleAudioMechanismsListener, IFeedbackServiceEventListener {
    private List<AbstractFeedbackPart> feedbackParts;
    private List<AbstractFeedbackPartView> feedbackPartViews;
    private FeedbackDetailsBean feedbackDetailsBean;
    private String feedbackTitle;
    private String[] feedbackTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        feedbackTitle = getIntent().getStringExtra(EXTRA_KEY_FEEDBACK_TITLE);
        feedbackTags = getIntent().getStringArrayExtra(EXTRA_KEY_FEEDBACK_TAGS);
        initView();
        colorViews(1,getView(R.id.feedback_root, ScrollView.class));
        onPostCreate();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LinearLayout emptyLayout = (LinearLayout) findViewById(R.id.supersede_feedbacklibrary_empty_layout);
        if (emptyLayout != null) {
            emptyLayout.requestFocus();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK || data == null) {
            return;
        }

        if (requestCode == REQUEST_PHOTO) {
            onSelectFromGalleryResult(data);
        } else if (requestCode == REQUEST_ANNOTATE) {
            annotateMechanismView(data);
        }
    }

    @Override
    public void onRecordStart(long audioMechanismId) {
        for (AbstractFeedbackPartView feedbackPartView : feedbackPartViews) {
            if (feedbackPartView instanceof AudioFeedbackView) {
                AudioFeedbackView view = ((AudioFeedbackView) feedbackPartView);
                if (view.getAudioMechanismId() != audioMechanismId) {
                    view.setAllButtonsClickable(false);
                }
            }
        }
    }

    @Override
    public void onRecordStop() {
        for (AbstractFeedbackPartView feedbackPartView : feedbackPartViews) {
            if (feedbackPartView instanceof AudioFeedbackView) {
                ((AudioFeedbackView) feedbackPartView).setAllButtonsClickable(true);
            }
        }
    }

    @Override
    public void onEventFailed(EventType eventType, Object response) {
        String msg = "Failed to consume the Event.";
        Log.e(FEEDBACK_ACTIVITY_TAG, msg);
        DialogUtils.showInformationDialog(this, new String[]{getResources().getString(R.string.info_error),msg}, true);
    }

    @Override
    public void onConnectionFailed(EventType eventType) {
        String msg = "Failed to connect to the Server.";
        Log.e(FEEDBACK_ACTIVITY_TAG, msg);
        DialogUtils.showInformationDialog(this, new String[]{getResources().getString(R.string.info_error),msg}, true);
    }

    private void initView() {
        feedbackPartViews = new ArrayList<>();
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LinearLayout linearLayout = getView(R.id.supersede_feedbacklibrary_feedback_activity_layout, LinearLayout.class);

        layoutInflater.inflate(R.layout.utility_feedback_button, linearLayout);
        feedbackParts = new OrchestratorStub.FeedbackBuilder(this, getApplicationContext(), getResources(), getConfiguration(), linearLayout, layoutInflater)
                .withRating()
                .withText()
                .withScreenshot()
                .withAudio()
                .build(feedbackPartViews).getFeedbackParts();

    }

    private void execPrepareAndSendFeedback() {
        Bitmap screenshot = ImageUtility.loadAnnotatedImageFromDatabase(this);
        screenshot = screenshot != null ? screenshot : ImageUtility.loadImageFromDatabase(this);

        Feedback feedback = FeedbackUtility.createFeedback(this, feedbackParts, feedbackTitle, feedbackTags);

        feedbackDetailsBean = FeedbackUtility.feedbackToFeedbackDetailsBean(this, feedback);
        FeedbackService.getInstance(this).createFeedback(this, this, feedback, ImageUtility.imageToBytes(screenshot));
        ImageUtility.wipeImages(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onEventCompleted(EventType eventType, Object response) {
        switch (eventType) {
            case CREATE_FEEDBACK:
                if (VersionUtility.getDateVersion() > 2 ) {
                    if (response instanceof Feedback) {
                        feedbackDetailsBean = FeedbackUtility.feedbackToFeedbackDetailsBean(this,(Feedback) response);
                    }
                    Intent intent = new Intent(this, FeedbackDetailsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra(EXTRA_KEY_FEEDBACK_DETAIL_BEAN, feedbackDetailsBean);
                    intent.putExtra(EXTRA_KEY_APPLICATION_CONFIGURATION, configuration);
                    intent.putExtra(EXTRA_FROM_CREATION, true);
                    intent.putExtra(EXTRA_KEY_CALLER_CLASS, this.getClass().getName());
                    startActivity(this,FeedbackDetailsActivity.class, true,intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.feedback_success), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), FeedbackHubActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(this,FeedbackHubActivity.class, true,intent);
                }
                break;
            default:
                break;
        }
    }

    private void annotateMechanismView(Intent data) {
        ScreenshotFeedbackView screenshotFeedbackView = null;
        for (AbstractFeedbackPartView feedbackPartView : feedbackPartViews) {
            if (feedbackPartView instanceof ScreenshotFeedbackView) {
                screenshotFeedbackView = (ScreenshotFeedbackView) feedbackPartView;
            }
        }
        if (screenshotFeedbackView == null) {
            return;
        }
        // Sticker annotations
        if (data.getBooleanExtra(EXTRA_KEY_HAS_STICKER_ANNOTATIONS, false)) {
            screenshotFeedbackView.setAllStickerAnnotations((HashMap<Integer, String>) data.getSerializableExtra(EXTRA_KEY_ALL_STICKER_ANNOTATIONS));
        }
        screenshotFeedbackView.refreshPreview(this);
    }

    private void onSelectFromGalleryResult(Intent data) {
        ScreenshotFeedbackView screenshotFeedbackView = null;

        for (AbstractFeedbackPartView view : feedbackPartViews) {
            if (view instanceof ScreenshotFeedbackView) {
                screenshotFeedbackView = (ScreenshotFeedbackView) view;
            }
        }
        if (screenshotFeedbackView != null) {
            Uri selectedImage = data.getData();

            if (selectedImage == null) {
                return;
            }

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String tempPicturePath = cursor.getString(columnIndex);
                cursor.close();
                Bitmap tempPictureBitmap = BitmapFactory.decodeFile(tempPicturePath);
                ImageUtility.wipeImages(this);
                ImageUtility.storeImageToDatabase(this, tempPictureBitmap);
                screenshotFeedbackView.refreshPreview(this);
            }
        }
    }

    /*
     * This method performs a POST request in viewOrder to send the feedback to the repository.
     */
    public void sendButtonClicked(View view) {
        if (!isOnline()) {
            DialogUtils.showInformationDialog(this, new String[]{getResources().getString(R.string.feedback_check_network_state)}, true);
            return;
        }

        // The feedbackPart models are updated with the view values
        for (AbstractFeedbackPartView feedbackPartView : feedbackPartViews) {
            feedbackPartView.updateModel();
        }

        final ArrayList<String> messages = new ArrayList<>();
        if (validateInput(feedbackParts, messages)) {
            execPrepareAndSendFeedback();
        } else {
            Log.v(FEEDBACK_ACTIVITY_TAG, "Validation of the feedback part failed");
            DialogUtils.showInformationDialog(this, messages.toArray(new String[messages.size()]), false);
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private boolean validateInput(List<AbstractFeedbackPart> feedbackParts, List<String> errorMessages) {
        if (feedbackParts == null) {
            return true;
        }
        // Append an error message and return. The user is confronted with one error message at a time.
        for (AbstractFeedbackPart feedbackPart : feedbackParts) {
            if (feedbackPart != null && !feedbackPart.isValid(errorMessages)) {
                return false;
            }
        }
        return true;
    }
}