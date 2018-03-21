package ch.uzh.supersede.feedbacklibrary.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.os.UserManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import ch.uzh.supersede.feedbacklibrary.API.IFeedbackAPI;
import ch.uzh.supersede.feedbacklibrary.BuildConfig;
import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.configurations.Configuration;
import ch.uzh.supersede.feedbacklibrary.configurations.OrchestratorConfiguration;
import ch.uzh.supersede.feedbacklibrary.configurations.OrchestratorConfigurationItem;
import ch.uzh.supersede.feedbacklibrary.feedbacks.AudioFeedback;
import ch.uzh.supersede.feedbacklibrary.feedbacks.Feedback;
import ch.uzh.supersede.feedbacklibrary.feedbacks.ScreenshotFeedback;
import ch.uzh.supersede.feedbacklibrary.models.Mechanism;
import ch.uzh.supersede.feedbacklibrary.services.FeedbackService;
import ch.uzh.supersede.feedbacklibrary.stubs.OrchestratorStub;
import ch.uzh.supersede.feedbacklibrary.stubs.OrchestratorStub.MechanismBuilder;
import ch.uzh.supersede.feedbacklibrary.utils.DialogUtils;
import ch.uzh.supersede.feedbacklibrary.utils.Utils;
import ch.uzh.supersede.feedbacklibrary.views.AudioMechanismView;
import ch.uzh.supersede.feedbacklibrary.views.CategoryMechanismView;
import ch.uzh.supersede.feedbacklibrary.views.MechanismView;
import ch.uzh.supersede.feedbacklibrary.views.RatingMechanismView;
import ch.uzh.supersede.feedbacklibrary.views.ScreenshotMechanismView;
import ch.uzh.supersede.feedbacklibrary.views.TextMechanismView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.internal.platform.Platform;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.FeedbackActivityConstants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.PATH_DELIMITER;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.ScreenshotConstants.*;

/**
 * The main activity where the feedback mechanisms are displayed.
 */
public class FeedbackActivity extends AbstractBaseActivity implements ScreenshotMechanismView.OnImageChangedListener, AudioMechanismView.MultipleAudioMechanismsListener {
    private IFeedbackAPI feedbackAPI;
    // Orchestrator configuration fetched from the orchestrator
    private OrchestratorConfigurationItem orchestratorConfigurationItem;
    // Orchestrator configuration initialized from the previously fetched orchestrator configuration
    private OrchestratorConfiguration orchestratorConfiguration;
    // Active configuration
    private Configuration activeConfiguration;
    // All mechanisms including inactive ones which represent the models
    private List<Mechanism> mechanisms;
    // All views of active mechanisms which represent the views
    private List<MechanismView> mechanismViews;
    // Image annotation
    private int tempMechanismViewId = -1;
    private String defaultImagePath;
    // Pull configuration
    private boolean isPush;
    private long selectedPullConfigurationIndex;
    // General
    private String language;
    private String baseURL;
    private ProgressDialog progressDialog;
    private String userScreenshotChosenTask = "";
    private String email;

    private EditText emailEditText;
    private CheckBox getCopyCheckBox;
    private Feedback feedback;
    private List<ScreenshotFeedback> screenshotFeedbackList;
    private List<AudioFeedback> audioFeedbackList;
    private String savedEmail;
    private SharedPreferences sharedPreferences;

    private void closeProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_PHOTO);
    }

    /**
     * This method returns the id of the selected PULL configuration.
     *
     * @return the id
     */
    public long getSelectedPullConfigurationIndex() {
        return selectedPullConfigurationIndex;
    }

    private void handleConfigurationRetrievalError() {
        new AlertDialog.Builder(this).setMessage(R.string.supersede_feedbacklibrary_feedback_application_unavailable_text).
                setPositiveButton(R.string.supersede_feedbacklibrary_ok_string, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        FeedbackActivity.this.onBackPressed();
                    }
                }).
                setCancelable(false).
                show();
    }

    private void init(long applicationId, String baseURL, String language) {
        if (applicationId != -1 && baseURL != null && language != null) {
            Retrofit rtf = new Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).build();
            feedbackAPI = rtf.create(IFeedbackAPI.class);
            initConfiguration(language, applicationId);
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

    private void initConfiguration(String language, long applicationId) {
        Call<OrchestratorConfigurationItem> result = feedbackAPI.getConfiguration(language, applicationId);
        this.language = language;

        // Asynchronous call
        if (result != null) {
            // Make progress dialog visible
            progressDialog = DialogUtils.createProgressDialog(FeedbackActivity.this, getResources().getString(R.string.supersede_feedbacklibrary_loading_string), false);
            progressDialog.show();
            result.enqueue(new Callback<OrchestratorConfigurationItem>() {
                @Override
                public void onFailure(Call<OrchestratorConfigurationItem> call, Throwable t) {
                    Log.e(TAG, "Failed to retrieve the configuration. onFailure method called", t);
                    closeProgressDialog();
                    handleConfigurationRetrievalError();
                }

                @Override
                public void onResponse(Call<OrchestratorConfigurationItem> call, Response<OrchestratorConfigurationItem> response) {
                    if (response.code() == 200) {
                        Log.i(TAG, "Configuration successfully retrieved");
                        orchestratorConfigurationItem = response.body();
                        // Save the current configuration under FeedbackActivity.CONFIGURATION_DIR}/FeedbackActivity.JSON_CONFIGURATION_FILE_NAME
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.setLenient();
                        Gson gson = gsonBuilder.create();
                        String jsonString = gson.toJson(orchestratorConfigurationItem);
                        Utils.saveStringContentToInternalStorage(getApplicationContext(), CONFIGURATION_DIR, JSON_CONFIGURATION_FILE_NAME, jsonString, MODE_PRIVATE);
                        initModel();
                        initView();
                        closeProgressDialog();
                    } else {
                        Log.e(TAG, "Failed to retrieve the configuration. Response code == " + response.code());
                        closeProgressDialog();
                        handleConfigurationRetrievalError();
                    }
                }
            });
        } else {
            Log.e(TAG, "Failed to retrieve the configuration. Call<OrchestratorConfigurationItem> result is null");
            handleConfigurationRetrievalError();
        }
    }

    private void initModel() {
        if (orchestratorConfigurationItem != null) {
            orchestratorConfiguration = new OrchestratorConfiguration(orchestratorConfigurationItem, isPush(), getSelectedPullConfigurationIndex());
            activeConfiguration = orchestratorConfiguration.getActiveConfiguration();
            mechanisms = activeConfiguration.getMechanisms();
        }
    }

    private void initView() {
        mechanismViews = new ArrayList<>();
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LinearLayout linearLayout = getView(R.id.supersede_feedbacklibrary_feedback_activity_layout, LinearLayout.class);

        if (linearLayout != null) {
            if (BuildConfig.DEBUG){
                OrchestratorStub stub = new MechanismBuilder(this,getApplicationContext(),getResources(),linearLayout,layoutInflater,defaultImagePath)
//                        .withRating() //Uncomment for Enabling
                        .withText() //Uncomment for Enabling
                        .withScreenshot() //Uncomment for Enabling
                        .withAudio() //Uncomment for Enabling
//                        .withCategory() //Uncomment for Enabling
                        .build(mechanismViews);
                stub.addAll(linearLayout,mechanismViews);
            }else{
                for (Mechanism mechanism : mechanisms) {
                    if (mechanism != null && mechanism.isActive()) {
                        resolveMechanism(layoutInflater, linearLayout, mechanism);
                    }
                }
            }
            initCopyByEmailLayout(layoutInflater, linearLayout);
            layoutInflater.inflate(R.layout.send_feedback_layout, linearLayout);

        }
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
                mechanismView = new ScreenshotMechanismView(layoutInflater, mechanism, this, mechanismViews.size(), defaultImagePath);
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

    private void initCopyByEmailLayout(LayoutInflater layoutInflater, LinearLayout linearLayout) {
        //Check sharedPreferences on email existing
        sharedPreferences = getSharedPreferences("FeedbackApp", Context.MODE_PRIVATE);
        savedEmail = sharedPreferences.getString("email", "");

        View view = layoutInflater.inflate(R.layout.send_by_email_layout, null, false);
        linearLayout.addView(view);

        emailEditText = (EditText) view.findViewById(R.id.sbe_email_et);
        if (!TextUtils.isEmpty(savedEmail)) {
            emailEditText.setText(savedEmail);
        }

        getCopyCheckBox = (CheckBox) view.findViewById(R.id.sbe_get_copy_cb);

        getCopyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    emailEditText.setVisibility(View.VISIBLE);
                } else {
                    emailEditText.setVisibility(View.GONE);
                }
            }
        });
    }

    private boolean isPush() {
        return isPush;
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

    private void annotateMechanismView(Intent data) {
        int mechanismViewId = data.getIntExtra(EXTRA_KEY_MECHANISM_VIEW_ID, -1);

        if (mechanismViewId == -1) {
            Log.e(TAG, "Failed to annotate the image. No mechanismViewID provided");
            return;
        }

        ScreenshotMechanismView screenshotMechanismView = (ScreenshotMechanismView) mechanismViews.get(mechanismViewId);

        // Sticker annotations
        if (data.getBooleanExtra(EXTRA_KEY_HAS_STICKER_ANNOTATIONS, false)) {
            screenshotMechanismView.setAllStickerAnnotations((HashMap<Integer, String>) data.getSerializableExtra(EXTRA_KEY_ALL_STICKER_ANNOTATIONS));
        }
        // Text annotations
        if (data.getBooleanExtra(EXTRA_KEY_HAS_TEXT_ANNOTATIONS, false)) {
            screenshotMechanismView.setAllTextAnnotations((HashMap<Integer, String>) data.getSerializableExtra(EXTRA_KEY_ALL_TEXT_ANNOTATIONS));
        }

        // Annotated image with stickers
        String tempPathWithStickers = data.getStringExtra(EXTRA_KEY_ANNOTATED_IMAGE_PATH_WITH_STICKERS) + PATH_DELIMITER + mechanismViewId + ANNOTATED_IMAGE_NAME_WITH_STICKERS;
        screenshotMechanismView.setAnnotatedImagePath(tempPathWithStickers);
        screenshotMechanismView.setPicturePath(tempPathWithStickers);
        Bitmap annotatedBitmap = Utils.loadImageFromStorage(tempPathWithStickers);
        if (annotatedBitmap != null) {
            screenshotMechanismView.setPictureBitmap(annotatedBitmap);
            screenshotMechanismView.getScreenShotPreviewImageView().setImageBitmap(annotatedBitmap);
        }

        // Annotated image without stickers
        if (data.getStringExtra(EXTRA_KEY_ANNOTATED_IMAGE_PATH_WITHOUT_STICKERS) == null) {
            screenshotMechanismView.setPicturePathWithoutStickers(null);
        } else {
            String tempPathWithoutStickers = data.getStringExtra(EXTRA_KEY_ANNOTATED_IMAGE_PATH_WITHOUT_STICKERS) + PATH_DELIMITER + mechanismViewId + ANNOTATED_IMAGE_NAME_WITHOUT_STICKERS;
            screenshotMechanismView.setPicturePathWithoutStickers(tempPathWithoutStickers);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO [jfo]: remove me
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_feedback);

        UserManager userManager = (UserManager) getApplicationContext().getSystemService(Context.USER_SERVICE);
        if (userManager.isUserAGoat()) {
            Log.v(TAG, "The user IS a goat and subject to teleportations");
        } else {
            Log.v(TAG, "The user IS NOT a goat and subject to teleportations");
        }

        Intent intent = getIntent();
        // Get the default image path for the screenshot if present
        defaultImagePath = intent.getStringExtra(DEFAULT_IMAGE_PATH);
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
            Utils.saveStringContentToInternalStorage(getApplicationContext(), CONFIGURATION_DIR, JSON_CONFIGURATION_FILE_NAME, jsonString, MODE_PRIVATE);
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.setLenient();
            Gson gson = gsonBuilder.create();
            orchestratorConfigurationItem = gson.fromJson(jsonString, OrchestratorConfigurationItem.class);
            initModel();
            initView();
        } else if (BuildConfig.DEBUG){
            initModel();
            initView();
        }else{
            // The feedback activity is started on behalf of the user
            Log.v(TAG, "The feedback activity is started via a PUSH configuration");

            init(intent.getLongExtra(EXTRA_KEY_APPLICATION_ID, -1L), baseURL, language);
        }
    }

    @Override
    public void onImageAnnotate(ScreenshotMechanismView screenshotMechanismView) {
        Intent intent = new Intent(this, AnnotateImageActivity.class);
        if (screenshotMechanismView.getAllStickerAnnotations() != null && !screenshotMechanismView.getAllStickerAnnotations().isEmpty()) {
            intent.putExtra(EXTRA_KEY_HAS_STICKER_ANNOTATIONS, true);
            intent.putExtra(EXTRA_KEY_ALL_STICKER_ANNOTATIONS, new HashMap<>(screenshotMechanismView.getAllStickerAnnotations()));
        }
        if (screenshotMechanismView.getAllTextAnnotations() != null && !screenshotMechanismView.getAllTextAnnotations().isEmpty()) {
            intent.putExtra(EXTRA_KEY_HAS_TEXT_ANNOTATIONS, true);
            intent.putExtra(EXTRA_KEY_ALL_TEXT_ANNOTATIONS, new HashMap<>(screenshotMechanismView.getAllTextAnnotations()));
        }

        String path = screenshotMechanismView.getPicturePathWithoutStickers() == null ? screenshotMechanismView.getPicturePath() : screenshotMechanismView.getPicturePathWithoutStickers();
        intent.putExtra(EXTRA_KEY_MECHANISM_VIEW_ID, screenshotMechanismView.getMechanismViewIndex());
        intent.putExtra(EXTRA_KEY_IMAGE_PATCH, path);
        intent.putExtra(TEXT_ANNOTATION_COUNTER_MAXIMUM, screenshotMechanismView.getMaxNumberTextAnnotation());
        startActivityForResult(intent, REQUEST_ANNOTATE);
    }

    @Override
    public void onImageSelect(ScreenshotMechanismView screenshotMechanismView) {
        tempMechanismViewId = screenshotMechanismView.getMechanismViewIndex();
        final Resources res = getResources();
        final CharSequence[] items = {res.getString(R.string.supersede_feedbacklibrary_library_chooser_text), res.getString(R.string.supersede_feedbacklibrary_cancel_string)};
        AlertDialog.Builder builder = new AlertDialog.Builder(FeedbackActivity.this);
        builder.setTitle(res.getString(R.string.supersede_feedbacklibrary_image_selection_dialog_title));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (!items[item].equals(res.getString(R.string.supersede_feedbacklibrary_cancel_string))) {
                    boolean result = Utils.checkSinglePermission(FeedbackActivity.this, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, null, null, false);
                    if (items[item].equals(res.getString(R.string.supersede_feedbacklibrary_photo_capture_text))) {
                        userScreenshotChosenTask = res.getString(R.string.supersede_feedbacklibrary_photo_capture_text);
                        if (result) {
                            //TODO remove or handle
                        }
                    } else if (items[item].equals(res.getString(R.string.supersede_feedbacklibrary_library_chooser_text))) {
                        userScreenshotChosenTask = res.getString(R.string.supersede_feedbacklibrary_library_chooser_text);
                        if (result) {
                            galleryIntent();
                        }
                    }
                } else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onImageClick(ScreenshotMechanismView screenshotMechanismView) {
        tempMechanismViewId = screenshotMechanismView.getMechanismViewIndex();
        galleryIntent();
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Resources res = getResources();
                if (userScreenshotChosenTask.equals(res.getString(R.string.supersede_feedbacklibrary_photo_capture_text))) {
                    //TODO remove or handle
                } else if (userScreenshotChosenTask.equals(res.getString(R.string.supersede_feedbacklibrary_library_chooser_text))) {
                    galleryIntent();
                }
            } else {
                // The user denied the permission
                onRequestPermissionsResultDenied(requestCode, Manifest.permission.READ_EXTERNAL_STORAGE, R.string.supersede_feedbacklibrary_external_storage_permission_text, getResources().getString(R.string.supersede_feedbacklibrary_external_storage_permission_text_instructions));
            }
        } else if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast toast = Toast.makeText(getApplicationContext(), R.string.supersede_feedbacklibrary_record_audio_permission_granted_text, Toast.LENGTH_SHORT);
                toast.show();
            } else {
                // The user denied the permission
                onRequestPermissionsResultDenied(requestCode, Manifest.permission.RECORD_AUDIO, R.string.supersede_feedbacklibrary_record_audio_permission_text, getResources().getString(R.string.supersede_feedbacklibrary_record_audio_permission_text_instructions));
            }
        }
    }

    private void onRequestPermissionsResultDenied(final int requestCode, @NonNull final String permission, int dialogMessage, @NonNull String dialogInstructions) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            // The user denied without checking 'Never ask again'. Show again the rationale
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
            alertBuilder.setTitle(R.string.supersede_feedbacklibrary_permission_request_title);
            alertBuilder.setMessage(dialogMessage);
            alertBuilder.setPositiveButton(R.string.supersede_feedbacklibrary_retry_string, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(FeedbackActivity.this, new String[]{permission}, requestCode);
                }
            });
            alertBuilder.setNegativeButton(R.string.supersede_feedbacklibrary_not_now_text, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertBuilder.setCancelable(false);
            alertBuilder.show();
        } else {
            // The user denied and checked the 'Never ask again' option. Show a short explanation dialog
            DialogUtils.showInformationDialog(this, new String[]{dialogInstructions}, false);
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        if (tempMechanismViewId != -1 && mechanismViews.get(tempMechanismViewId) instanceof ScreenshotMechanismView) {
            ScreenshotMechanismView screenshotMechanismView = (ScreenshotMechanismView) mechanismViews.get(tempMechanismViewId);
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
                screenshotMechanismView.setPicturePath(tempPicturePath);
                screenshotMechanismView.setAnnotatedImagePath(tempPicturePath);
                Bitmap tempPictureBitmap = BitmapFactory.decodeFile(tempPicturePath);
                screenshotMechanismView.setPictureBitmap(tempPictureBitmap);
                screenshotMechanismView.getScreenShotPreviewImageView().setBackground(null);
                screenshotMechanismView.getScreenShotPreviewImageView().setImageBitmap(tempPictureBitmap);
                screenshotMechanismView.getAnnotateScreenshotButton().setEnabled(true);
                screenshotMechanismView.getDeleteScreenshotButton().setEnabled(true);
            }
            tempMechanismViewId = -1;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LinearLayout emptyLayout = (LinearLayout) findViewById(R.id.supersede_feedbacklibrary_empty_layout);
        if (emptyLayout != null) {
            emptyLayout.requestFocus();
        }
    }

    /*
     * This method performs a POST request in order to send the feedback to the repository.
     */
    public void sendButtonClicked(View view) {
        if (BuildConfig.DEBUG){
            OrchestratorStub.receiveFeedback(this,view);
            return;
        }
        if (!isOnline()) {
            DialogUtils.showInformationDialog(this, new String[]{getResources().getString(R.string.check_network_state)}, true);
            return;
        }

        if (baseURL != null && language != null) {
            // The mechanism models are updated with the view values
            for (MechanismView mechanismView : mechanismViews) {
                mechanismView.updateModel();
            }

            final ArrayList<String> messages = new ArrayList<>();
            if (validateInput(mechanisms, messages)) {
                if (feedbackAPI == null) {
                    Retrofit rtf = new Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).build();
                    feedbackAPI = rtf.create(IFeedbackAPI.class);
                }
                sendFeedback();
            } else {
                Log.v(TAG, "Validation of the mechanism failed");
                DialogUtils.showInformationDialog(this, messages.toArray(new String[messages.size()]), false);
            }
        } else {
            if (baseURL == null) {
                Log.e(TAG, "Failed to send the feedback. baseURL is null");
            } else {
                Log.e(TAG, "Failed to send the feedback. language is null");
            }
            DialogUtils.showInformationDialog(this, new String[]{getResources().getString(R.string.supersede_feedbacklibrary_error_text)}, true);
        }
    }

    private void sendFeedback() {
        FeedbackService.getInstance().pingRepository();

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
        multipartFiles.addAll(getScreenshotMultipartbodyParts(feedback));
        multipartFiles.addAll(getAudioMultipartbodyParts(feedback));

        createFeedbackVariant(jsonPart, multipartFiles);
        checkAndSendViaEmail();
    }

    private void createFeedbackVariant(MultipartBody.Part jsonPart, List<MultipartBody.Part> multipartFiles) {
        FeedbackService.getInstance().createFeedbackVariant(language, feedback.getApplicationId(), jsonPart, multipartFiles);
        Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.supersede_feedbacklibrary_success_text), Toast.LENGTH_SHORT);
        toast.show();
    }

    private List<MultipartBody.Part> getScreenshotMultipartbodyParts(Feedback feedback) {
        List<MultipartBody.Part> multipartFiles = new ArrayList<>();

        screenshotFeedbackList = feedback.getScreenshotFeedbacks();
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

    private List<MultipartBody.Part> getAudioMultipartbodyParts(Feedback feedback) {
        List<MultipartBody.Part> multipartFiles = new ArrayList<>();

        audioFeedbackList = feedback.getAudioFeedbacks();
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

    private void checkAndSendViaEmail() {
        if (getCopyCheckBox.isChecked()) {
            email = emailEditText.getText().toString();
            if (Utils.isEmailValid(email)) {
                sendCopyByEmail();
            } else {
                DialogUtils.showInformationDialog(this, new String[]{getResources().getString(R.string.invalid_email)}, true);
            }
        }
    }

    private void sendCopyByEmail() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Thread emailThread = new Thread(new Runnable() {
            @Override
            public void run() {
                final String username = "supersede.zurich@gmail.com";
                final String password = System.getenv("F2F_SMTP_EMAIL_PASSWORD");

                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");

                Session session = Session.getInstance(props, new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
                try {
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(username));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                    message.setSubject("Copy of your feedback");

                    BodyPart messageBodyPart = new MimeBodyPart();

                    String feedbackText = feedback.getTextFeedbacks().get(0).getText();

                    CategoryMechanismView categoryMechanismView = (CategoryMechanismView) mechanismViews.get(4);
                    String category = categoryMechanismView.getCustomSpinner().getSelectedItem().toString();

                    if (category.equals("My feedback is about…")) {
                        category = "-";
                    }

                    RatingMechanismView ratingMechanismView = (RatingMechanismView) mechanismViews.get(3);
                    String rating = String.valueOf(ratingMechanismView.getRating());

                    // Now set the actual message
                    messageBodyPart.setText(String.format("Feedback text: %s, Rating: %s, Category: %s", feedbackText, rating, category));

                    Multipart multipart = new MimeMultipart();

                    // Set text message part
                    multipart.addBodyPart(messageBodyPart);

                    for (ScreenshotFeedback screenshotFeedback : screenshotFeedbackList) {
                        addAttachment(multipart, screenshotFeedback.getImagePath());
                    }
                    for (AudioFeedback audioFeedback : audioFeedbackList) {
                        addAttachment(multipart, audioFeedback.getAudioPath());
                    }

                    message.setContent(multipart);

                    Transport.send(message);

                    if (TextUtils.isEmpty(savedEmail)) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("email", email);
                        editor.apply();
                    }

                } catch (MessagingException e) {
                    Log.e(e.getMessage(), e.toString());
                    DialogUtils.showInformationDialog(FeedbackActivity.this, new String[]{getResources().getString(R.string.supersede_feedbacklibrary_error_text)}, true);
                }
            }
        });

        emailThread.start();
    }

    private void addAttachment(Multipart multipart, String filePath) throws MessagingException {
        URI uri;
        try {
            uri = new URI(filePath);
        } catch (URISyntaxException e) {
            Log.e(TAG, "Failed to create URI", e);
            return;
        }
        String[] segments = uri.getPath().split("/");
        String filename = segments[segments.length - 1];

        DataSource source = new FileDataSource(filePath);
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);
        multipart.addBodyPart(messageBodyPart);

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
            if (mechanism != null && mechanism.isActive()) {
                boolean isValid = mechanism.isValid(errorMessages);
                if (!isValid) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_help, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mh_help) {
            Intent intent = new Intent(this, HelpActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}