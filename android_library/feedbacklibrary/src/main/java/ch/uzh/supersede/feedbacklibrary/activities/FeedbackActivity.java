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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import ch.uzh.supersede.feedbacklibrary.BuildConfig;
import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.beans.FeedbackBean;
import ch.uzh.supersede.feedbacklibrary.beans.FeedbackDetailsBean;
import ch.uzh.supersede.feedbacklibrary.components.views.AbstractMechanismView;
import ch.uzh.supersede.feedbacklibrary.components.views.AudioMechanismView;
import ch.uzh.supersede.feedbacklibrary.components.views.ScreenshotMechanismView;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.models.AbstractMechanism;
import ch.uzh.supersede.feedbacklibrary.services.FeedbackService;
import ch.uzh.supersede.feedbacklibrary.services.IFeedbackServiceEventListener;
import ch.uzh.supersede.feedbacklibrary.stubs.OrchestratorStub;
import ch.uzh.supersede.feedbacklibrary.utils.DialogUtils;
import ch.uzh.supersede.feedbacklibrary.utils.Enums;
import ch.uzh.supersede.feedbacklibrary.utils.FeedbackTransformer;
import ch.uzh.supersede.feedbacklibrary.utils.ImageUtility;
import ch.uzh.supersede.feedbacklibrary.utils.Utils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.ActivitiesConstants.FEEDBACK_ACTIVITY_TAG;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Enums.FEEDBACK_STATUS.OPEN;

@SuppressWarnings({"squid:MaximumInheritanceDepth", "squid:S1170"})
public class FeedbackActivity extends AbstractBaseActivity implements AudioMechanismView.MultipleAudioMechanismsListener, IFeedbackServiceEventListener {
    private List<AbstractMechanism> mechanisms;
    private List<AbstractMechanismView> mechanismViews;

    private String language;
    private String baseURL;
    private FeedbackDetailsBean feedbackDetailsBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        language = "en"; //FIXME pay attention
        baseURL = SUPERSEDE_BASE_URL;

        initView();
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
        for (AbstractMechanismView mechanismView : mechanismViews) {
            if (mechanismView instanceof AudioMechanismView) {
                AudioMechanismView view = ((AudioMechanismView) mechanismView);
                if (view.getAudioMechanismId() != audioMechanismId) {
                    view.setAllButtonsClickable(false);
                }
            }
        }
    }

    @Override
    public void onRecordStop() {
        for (AbstractMechanismView mechanismView : mechanismViews) {
            if (mechanismView instanceof AudioMechanismView) {
                ((AudioMechanismView) mechanismView).setAllButtonsClickable(true);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onEventCompleted(EventType eventType, Object response) {
        switch (eventType) {
            case PING_REPOSITORY:
                FeedbackDetailsBean feedbackBean = prepareSendFeedback();
                Intent intent = new Intent(this, FeedbackDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra(EXTRA_KEY_FEEDBACK_DETAIL_BEAN, feedbackBean);
                intent.putExtra(EXTRA_KEY_APPLICATION_CONFIGURATION, configuration);
                this.onBackPressed(); //This serves the purpose of erasing the Feedback Activity from the Back-Button
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case CREATE_FEEDBACK_VARIANT:
                break;
            default:
                break;
        }
    }

    @Override
    public void onEventFailed(EventType eventType, Object response) {
        Log.e(FEEDBACK_ACTIVITY_TAG, "Failed to consume Event.");
        DialogUtils.showInformationDialog(this, new String[]{getResources().getString(R.string.info_error)}, true);
    }

    @Override
    public void onConnectionFailed(EventType eventType) {
        Log.e(FEEDBACK_ACTIVITY_TAG, "Failed to connect to Server.");
        DialogUtils.showInformationDialog(this, new String[]{getResources().getString(R.string.info_error)}, true);
    }

    private void initView() {
        mechanismViews = new ArrayList<>();
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LinearLayout linearLayout = getView(R.id.supersede_feedbacklibrary_feedback_activity_layout, LinearLayout.class);

        mechanisms = new OrchestratorStub.MechanismBuilder(this, getApplicationContext(), getResources(), getConfiguration(), linearLayout, layoutInflater)
                .withRating()
                .withText()
                .withScreenshot()
                .withAudio()
                .withCategory()
                .build(mechanismViews).getMechanisms();

        layoutInflater.inflate(R.layout.utility_feedback_button, linearLayout);
    }

    private void execCreateFeedbackVariant(FeedbackDetailsBean feedbackDetailsBean, List<MultipartBody.Part> multipartFiles) {
        FeedbackService.getInstance().createFeedbackVariant(this, this, language, configuration.getHostApplicationLongId(), feedbackDetailsBean, multipartFiles);
        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.feedback_success), Toast.LENGTH_SHORT);
        toast.show();
        Utils.wipeImages(this);
    }

    private FeedbackDetailsBean prepareSendFeedback() {
        String userName = FeedbackDatabase.getInstance(this).readString(USER_NAME, null);
        long timestamp = System.currentTimeMillis();
        UUID feedbackUid = UUID.randomUUID();
        Enums.FEEDBACK_STATUS status = OPEN;
        int upVotes = 0;
        int responses = 0;

        int minUpVotes = 0; //TODO [jfo] set
        int maxUpVotes = 0; //TODO [jfo] set

        String title = ""; //TODO [jfo] set
        String description = ""; //TODO [jfo] set
        String labels = ""; //TODO [jfo] set
        Bitmap bitmap = null; //TODO [jfo] set

        FeedbackBean feedbackBean = new FeedbackBean.Builder()
                .withUserName(userName)
                .withTimestamp(timestamp)
                .withFeedbackUid(feedbackUid)
                .withTitle(title)
                .withStatus(status)
                .withUpVotes(upVotes)
                .withResponses(responses)
                .withMinUpVotes(minUpVotes)
                .withMaxUpVotes(maxUpVotes)
                .build();
        feedbackDetailsBean = new FeedbackDetailsBean.Builder()
                .withUserName(userName)
                .withTimestamp(timestamp)
                .withFeedbackUid(feedbackUid)
                .withFeedbackBean(feedbackBean)
                .withTitle(title)
                .withDescription(description)
                .withLabels(labels)
                .withStatus(status)
                .withBitmap(bitmap)
                .build();

        List<MultipartBody.Part> multipartFiles = new ArrayList<>();
        multipartFiles.add(MultipartBody.Part.createFormData("screenshot", "screenshot", RequestBody.create(MediaType.parse("image/png"), ImageUtility.imageToBytes(feedbackDetailsBean.getBitmap()))));
        multipartFiles.add(MultipartBody.Part.createFormData("audio", "audio", RequestBody.create(MediaType.parse("audio/mp3"), ImageUtility.imageToBytes(feedbackDetailsBean.getBitmap()))));

        if (BuildConfig.DEBUG) {
            GsonBuilder builder = new GsonBuilder();
            builder.excludeFieldsWithoutExposeAnnotation();
            builder.serializeNulls();
            Gson gson = builder.create();
            String jsonString = gson.toJson(FeedbackTransformer.FeedbackDetailsBeanToFeedback(feedbackDetailsBean, configuration.getHostApplicationLongId(), mechanisms));

            Log.i("TEST:", "language: " + language);
            Log.i("TEST:", "applicationId: " + configuration.getHostApplicationLongId());
            Log.i("TEST:", "jsonString: " + jsonString);
            Log.i("TEST:", "files: " + multipartFiles.toString());
        }

        execCreateFeedbackVariant(feedbackDetailsBean, multipartFiles);
        return feedbackDetailsBean;
    }

    private void annotateMechanismView(Intent data) {
        ScreenshotMechanismView screenshotMechanismView = null;
        for (AbstractMechanismView mechanismView : mechanismViews) {
            if (mechanismView instanceof ScreenshotMechanismView) {
                screenshotMechanismView = (ScreenshotMechanismView) mechanismView;
            }
        }
        if (screenshotMechanismView == null) {
            return;
        }
        // Sticker annotations
        if (data.getBooleanExtra(EXTRA_KEY_HAS_STICKER_ANNOTATIONS, false)) {
            screenshotMechanismView.setAllStickerAnnotations((HashMap<Integer, String>) data.getSerializableExtra(EXTRA_KEY_ALL_STICKER_ANNOTATIONS));
        }
        screenshotMechanismView.refreshPreview(this);
    }

    private void onSelectFromGalleryResult(Intent data) {
        ScreenshotMechanismView screenshotMechanismView = null;

        for (AbstractMechanismView view : mechanismViews) {
            if (view instanceof ScreenshotMechanismView) {
                screenshotMechanismView = (ScreenshotMechanismView) view;
            }
        }
        if (screenshotMechanismView != null) {
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
                Utils.wipeImages(this);
                Utils.storeImageToDatabase(this, tempPictureBitmap);
                screenshotMechanismView.refreshPreview(this);
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

        if (baseURL == null || language == null) {
            if (baseURL == null) {
                Log.e(FEEDBACK_ACTIVITY_TAG, "Failed to send the feedback. baseURL is null");
            } else {
                Log.e(FEEDBACK_ACTIVITY_TAG, "Failed to send the feedback. language is null");
            }
            DialogUtils.showInformationDialog(this, new String[]{getResources().getString(R.string.info_error)}, true);
            return;
        }

        // The mechanism models are updated with the view values
        for (AbstractMechanismView mechanismView : mechanismViews) {
            mechanismView.updateModel();
        }

        final ArrayList<String> messages = new ArrayList<>();
        if (validateInput(mechanisms, messages)) {
            execPrepareAndSendFeedback();
        } else {
            Log.v(FEEDBACK_ACTIVITY_TAG, "Validation of the mechanism failed");
            DialogUtils.showInformationDialog(this, messages.toArray(new String[messages.size()]), false);
        }
    }

    private void execPrepareAndSendFeedback() {
        FeedbackService.getInstance().pingRepository(this);
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = null;
        if (cm != null) {
            netInfo = cm.getActiveNetworkInfo();
        }
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private boolean validateInput(List<AbstractMechanism> allMechanisms, List<String> errorMessages) {
        if (allMechanisms == null) {
            return true;
        }
        // Append an error message and return. The user is confronted with one error message at a time.
        for (AbstractMechanism mechanism : allMechanisms) {
            if (mechanism != null && !mechanism.isValid(errorMessages)) {
                return false;
            }
        }
        return true;
    }
}