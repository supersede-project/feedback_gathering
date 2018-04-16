package ch.uzh.supersede.feedbacklibrary.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ch.uzh.supersede.feedbacklibrary.BuildConfig;
import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.components.views.AudioMechanismView;
import ch.uzh.supersede.feedbacklibrary.components.views.CategoryMechanismView;
import ch.uzh.supersede.feedbacklibrary.components.views.MechanismView;
import ch.uzh.supersede.feedbacklibrary.components.views.RatingMechanismView;
import ch.uzh.supersede.feedbacklibrary.components.views.ScreenshotMechanismView;
import ch.uzh.supersede.feedbacklibrary.components.views.TextMechanismView;
import ch.uzh.supersede.feedbacklibrary.configurations.Configuration;
import ch.uzh.supersede.feedbacklibrary.configurations.OrchestratorConfiguration;
import ch.uzh.supersede.feedbacklibrary.configurations.OrchestratorConfigurationItem;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.feedbacks.AudioFeedback;
import ch.uzh.supersede.feedbacklibrary.feedbacks.Feedback;
import ch.uzh.supersede.feedbacklibrary.feedbacks.ScreenshotFeedback;
import ch.uzh.supersede.feedbacklibrary.models.Mechanism;
import ch.uzh.supersede.feedbacklibrary.services.ConfigurationRequestWrapper;
import ch.uzh.supersede.feedbacklibrary.services.EmailService;
import ch.uzh.supersede.feedbacklibrary.services.FeedbackService;
import ch.uzh.supersede.feedbacklibrary.services.IFeedbackServiceEventListener;
import ch.uzh.supersede.feedbacklibrary.stubs.OrchestratorStub;
import ch.uzh.supersede.feedbacklibrary.stubs.OrchestratorStub.MechanismBuilder;
import ch.uzh.supersede.feedbacklibrary.utils.DialogUtils;
import ch.uzh.supersede.feedbacklibrary.utils.Utils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.FeedbackActivityConstants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.FeedbackActivityConstants.TAG;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.ScreenshotConstants.*;

@SuppressWarnings({"squid:MaximumInheritanceDepth", "squid:S1170"})
public class FeedbackActivity extends AbstractBaseActivity implements AudioMechanismView.MultipleAudioMechanismsListener, IFeedbackServiceEventListener {
    private OrchestratorConfigurationItem orchestratorConfigurationItem;
    private OrchestratorConfiguration orchestratorConfiguration;
    private Configuration activeConfiguration;
    private List<Mechanism> mechanisms;
    private List<MechanismView> mechanismViews;

    private boolean isPush;
    private long selectedPullConfigurationIndex;

    private String language;
    private String baseURL;
    private ProgressDialog progressDialog;
    private Feedback feedback;

    private CheckBox sendViaEmailCheckbox;
    private EditText emailEditText;

    public long getSelectedPullConfigurationIndex() {
        return selectedPullConfigurationIndex;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Intent intent = getIntent();
        // Get the configuration type
        isPush = intent.getBooleanExtra(IS_PUSH_STRING, true);
        // Get values related to a pull configuration
        selectedPullConfigurationIndex = intent.getLongExtra(SELECTED_PULL_CONFIGURATION_INDEX_STRING, -1);
        String jsonString = intent.getStringExtra(JSON_CONFIGURATION_STRING);

        // Initialization based on the type of configuration, i.e., if it is push or pull
        language = intent.getStringExtra(EXTRA_KEY_LANGUAGE);
        baseURL = intent.getStringExtra(EXTRA_KEY_BASE_URL);

        if (!isPush && selectedPullConfigurationIndex != -1 && jsonString != null) {
            // The feedback activity is started on behalf of a triggered pull configuration
            Log.v(TAG, "The feedback activity is started via a PULL configuration");

            // Save the current configuration under FeedbackActivity.CONFIGURATION_DIR}/FeedbackActivity.JSON_CONFIGURATION_FILE_NAME
            FeedbackDatabase.getInstance(getApplicationContext()).writeString(JSON_CONFIGURATION_FILE_NAME, jsonString);
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setLenient();
            Gson gson = gsonBuilder.create();
            orchestratorConfigurationItem = gson.fromJson(jsonString, OrchestratorConfigurationItem.class);
            initModel();
            initView();
        } else if (BuildConfig.DEBUG) {
            initModel();
            initView();
        } else {
            // The feedback activity is started on behalf of the user
            Log.v(TAG, "The feedback activity is started via a PUSH configuration");

            init(intent.getLongExtra(EXTRA_KEY_APPLICATION_ID, -1L));
        }
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
        for (MechanismView mechanismView : mechanismViews) {
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
        for (MechanismView mechanismView : mechanismViews) {
            if (mechanismView instanceof AudioMechanismView) {
                ((AudioMechanismView) mechanismView).setAllButtonsClickable(true);
            }
        }
    }

    @Override
    public void onEventCompleted(EventType eventType, Response response) {
        switch (eventType) {
            case PING_REPOSITORY:
                prepareSendFeedback(getScreenshotMultipartbodyParts(), getAudioMultipartbodyParts());
                break;
            case CREATE_FEEDBACK_VARIANT:
                break;
            case GET_CONFIGURATION:
                execLoadConfiguration(response);
                break;
            default:
                break;
        }
    }

    @Override
    public void onEventFailed(EventType eventType, Response response) {
        switch (eventType) {
            case GET_CONFIGURATION:
                closeProgressDialog();
                handleConfigurationRetrievalError();
                break;
            default:
                break;
        }
        Log.e(TAG, "Failed to consume Event.");
        DialogUtils.showInformationDialog(this, new String[]{getResources().getString(R.string.supersede_feedbacklibrary_error_text)}, true);
    }

    @Override
    public void onConnectionFailed(EventType eventType) {
        Log.e(TAG, "Failed to connect to Server.");
        DialogUtils.showInformationDialog(this, new String[]{getResources().getString(R.string.supersede_feedbacklibrary_error_text)}, true);
    }

    private void init(long applicationId) {
        if (applicationId != -1 && baseURL != null && language != null) {
            initConfiguration(applicationId);
        } else {
            if (applicationId == -1) {
                Log.e(TAG, "Failed to retrieve the configuration. applicationId is -1");
            } else if (baseURL == null) {
                Log.e(TAG, "Failed to retrieve the configuration. baseURL is null");
            } else {
                Log.e(TAG, "Failed to retrieve the configuration. language is null");
            }
            handleConfigurationRetrievalError();
        }
    }

    private void initConfiguration(long applicationId) {
        ConfigurationRequestWrapper configurationRequestWrapper =
                new ConfigurationRequestWrapper.ConfigurationRequestWrapperBuilder(this, this.getClass())
                        .withApplicationId(applicationId)
                        .withLanguage(language)
                        .build();

        progressDialog = DialogUtils.createProgressDialog(FeedbackActivity.this, getResources().getString(R.string.supersede_feedbacklibrary_loading_string), false);
        progressDialog.show();
        FeedbackService.getInstance().getConfiguration(this, configurationRequestWrapper);
    }

    private void initModel() {
        if (orchestratorConfigurationItem != null) {
            orchestratorConfiguration = new OrchestratorConfiguration(orchestratorConfigurationItem, isPush, getSelectedPullConfigurationIndex());
            activeConfiguration = orchestratorConfiguration.getActiveConfiguration();
            mechanisms = activeConfiguration.getMechanisms();
        }
    }

    private void initView() {
        mechanismViews = new ArrayList<>();
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LinearLayout linearLayout = getView(R.id.supersede_feedbacklibrary_feedback_activity_layout, LinearLayout.class);

        if (linearLayout == null) {
            return;
        }
        if (BuildConfig.DEBUG) {
            new MechanismBuilder(this, getApplicationContext(), getResources(), linearLayout, layoutInflater)
                    .withRating() //Uncomment for Enabling
                    .withText() //Uncomment for Enabling
                    .withScreenshot() //Uncomment for Enabling
                    .withAudio() //Uncomment for Enabling
                    //                        .withCategory() //Uncomment for Enabling
                    .build(mechanismViews);
        } else {
            for (Mechanism mechanism : mechanisms) {
                if (mechanism != null && mechanism.isActive()) {
                    resolveMechanism(layoutInflater, linearLayout, mechanism);
                }
            }
        }
        initCopyByEmailLayout(this, layoutInflater, linearLayout);
        layoutInflater.inflate(R.layout.utility_feedback_button, linearLayout);
    }

    public void initCopyByEmailLayout(Activity activity, LayoutInflater layoutInflater, LinearLayout linearLayout) {
        String savedEmail = activity.getSharedPreferences("FeedbackApp", Context.MODE_PRIVATE).getString("email", "");

        View view = layoutInflater.inflate(R.layout.mechanism_email, null, false);
        linearLayout.addView(view);

        emailEditText = (EditText) view.findViewById(R.id.sbe_email_et);
        if (!TextUtils.isEmpty(savedEmail)) {
            emailEditText.setText(savedEmail);
        }

        sendViaEmailCheckbox = (CheckBox) view.findViewById(R.id.sbe_get_copy_cb);
        sendViaEmailCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    emailEditText.setEnabled(true);
                    emailEditText.setVisibility(View.VISIBLE);
                } else {
                    emailEditText.setEnabled(false);
                    emailEditText.setVisibility(View.GONE);
                }
            }
        });
    }

    public void execLoadConfiguration(Response<OrchestratorConfigurationItem> response) {
        Log.i(TAG, "Configuration successfully retrieved");
        orchestratorConfigurationItem = response.body();
        // Save the current configuration under FeedbackActivity.CONFIGURATION_DIR}/FeedbackActivity.JSON_CONFIGURATION_FILE_NAME
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();
        Gson gson = gsonBuilder.create();
        String jsonString = gson.toJson(orchestratorConfigurationItem);
        FeedbackDatabase.getInstance(getApplicationContext()).writeString(JSON_CONFIGURATION_FILE_NAME, jsonString);
        initModel();
        initView();
        closeProgressDialog();
    }

    private void execCreateFeedbackVariant(MultipartBody.Part jsonPart, List<MultipartBody.Part> multipartFiles) {
        FeedbackService.getInstance().createFeedbackVariant(this, language, feedback.getApplicationId(), jsonPart, multipartFiles);
        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.supersede_feedbacklibrary_success_text), Toast.LENGTH_SHORT);
        toast.show();
        Utils.wipeImages(this);
    }

    public void prepareSendFeedback(List<MultipartBody.Part> screenshotBodyParts, List<MultipartBody.Part> audioBodyParts) {
        feedback = new Feedback(mechanisms);
        feedback.setTitle(getResources().getString(R.string.supersede_feedbacklibrary_feedback_title_text, System.currentTimeMillis()));
        feedback.setApplicationId(orchestratorConfiguration.getId());
        feedback.setConfigurationId(activeConfiguration.getId());
        feedback.setLanguage(language);
        feedback.setUserIdentification(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));

        // The JSON string of the feedback
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        builder.serializeNulls();
        Gson gson = builder.create();

        String jsonString = gson.toJson(feedback);
        MultipartBody.Part jsonPart = MultipartBody.Part.createFormData("json", "json", RequestBody.create(MediaType.parse("application/json"), jsonString.getBytes()));

        List<MultipartBody.Part> multipartFiles = new ArrayList<>();
        multipartFiles.addAll(screenshotBodyParts);
        multipartFiles.addAll(audioBodyParts);

        execCreateFeedbackVariant(jsonPart, multipartFiles);
        EmailService.getInstance().checkAndSendViaEmail(this, sendViaEmailCheckbox, emailEditText, feedback, mechanismViews);
    }

    private void closeProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void handleConfigurationRetrievalError() {
        new AlertDialog.Builder(this).setMessage(R.string.supersede_feedbacklibrary_feedback_application_unavailable_text).
                setPositiveButton(R.string.supersede_feedbacklibrary_ok_string, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        FeedbackActivity.this.onBackPressed();
                    }
                }).setCancelable(false).show();
    }


    private void resolveMechanism(LayoutInflater layoutInflater, LinearLayout linearLayout, Mechanism mechanism) {
        MechanismView mechanismView = null;
        View view = null;
        String type = mechanism.getType();
        switch (type) {
            case Mechanism.ATTACHMENT_TYPE:
                break;
            case Mechanism.AUDIO_TYPE:
                mechanismView = new AudioMechanismView(layoutInflater, mechanism, getResources(), this, getApplicationContext());
                view = mechanismView.getEnclosingLayout();
                break;
            case Mechanism.CATEGORY_TYPE:
                mechanismView = new CategoryMechanismView(layoutInflater, mechanism);
                view = mechanismView.getEnclosingLayout();
                break;
            case Mechanism.RATING_TYPE:
                mechanismView = new RatingMechanismView(layoutInflater, mechanism);
                view = mechanismView.getEnclosingLayout();
                break;
            case Mechanism.SCREENSHOT_TYPE:
                mechanismView = new ScreenshotMechanismView(layoutInflater, this, mechanism, mechanismViews.size());
                view = mechanismView.getEnclosingLayout();
                break;
            case Mechanism.TEXT_TYPE:
                mechanismView = new TextMechanismView(layoutInflater, mechanism);
                view = mechanismView.getEnclosingLayout();
                break;
            default:
                Log.wtf(TAG, "Unknown mechanism type '" + type + "'");
                break;
        }

        if (mechanismView != null && view != null) {
            mechanismViews.add(mechanismView);
            linearLayout.addView(view);
        }
    }

    private void annotateMechanismView(Intent data) {
        ScreenshotMechanismView screenshotMechanismView = null;
        for (MechanismView mechanismView : mechanismViews) {
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

        for (MechanismView view : mechanismViews) {
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
     * This method performs a POST request in order to send the feedback to the repository.
     */
    public void sendButtonClicked(View view) {
        if (BuildConfig.DEBUG) {
            OrchestratorStub.receiveFeedback(this, view);
            return;
        }
        if (!isOnline()) {
            DialogUtils.showInformationDialog(this, new String[]{getResources().getString(R.string.check_network_state)}, true);
            return;
        }

        if (baseURL == null || language == null) {
            if (baseURL == null) {
                Log.e(TAG, "Failed to send the feedback. baseURL is null");
            } else {
                Log.e(TAG, "Failed to send the feedback. language is null");
            }
            DialogUtils.showInformationDialog(this, new String[]{getResources().getString(R.string.supersede_feedbacklibrary_error_text)}, true);
            return;
        }

        // The mechanism models are updated with the view values
        for (MechanismView mechanismView : mechanismViews) {
            mechanismView.updateModel();
        }

        final ArrayList<String> messages = new ArrayList<>();
        if (validateInput(mechanisms, messages)) {
            execPrepareAndSendFeedback();
        } else {
            Log.v(TAG, "Validation of the mechanism failed");
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

    private boolean validateInput(List<Mechanism> allMechanisms, List<String> errorMessages) {
        // Append an error message and return. The user is confronted with one error message at a time.
        for (Mechanism mechanism : allMechanisms) {
            if (mechanism != null && mechanism.isActive() && !mechanism.isValid(errorMessages)) {
                return false;
            }
        }
        return true;
    }

    public List<MultipartBody.Part> getScreenshotMultipartbodyParts() {
        List<MultipartBody.Part> multipartFiles = new ArrayList<>();
        List<ScreenshotFeedback> screenshotFeedbackList = feedback.getScreenshotFeedbacks();
        if (screenshotFeedbackList != null) {
            for (int pos = 0; pos < screenshotFeedbackList.size(); ++pos) {
                File screenshotFile = new File(screenshotFeedbackList.get(pos).getImagePath());
                String fileName = screenshotFeedbackList.get(pos).getFileName();
                String partName = "screenshot" + pos;

                MultipartBody.Part filePart = MultipartBody.Part.createFormData(partName, fileName, RequestBody.create(MediaType.parse("image/png"), screenshotFile));
                multipartFiles.add(filePart);
            }
        }
        return multipartFiles;
    }

    public List<MultipartBody.Part> getAudioMultipartbodyParts() {
        List<MultipartBody.Part> multipartFiles = new ArrayList<>();
        List<AudioFeedback> audioFeedbackList = feedback.getAudioFeedbacks();
        if (audioFeedbackList != null) {
            for (int pos = 0; pos < audioFeedbackList.size(); ++pos) {
                File audioFile = new File(audioFeedbackList.get(pos).getAudioPath());
                String fileName = audioFeedbackList.get(pos).getFileName();
                String partName = "audio" + pos;

                MultipartBody.Part filePart = MultipartBody.Part.createFormData(partName, fileName, RequestBody.create(MediaType.parse("audio/mp3"), audioFile));
                multipartFiles.add(filePart);
            }
        }
        return multipartFiles;
    }
}