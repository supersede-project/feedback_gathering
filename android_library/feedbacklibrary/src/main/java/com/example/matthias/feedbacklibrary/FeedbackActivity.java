package com.example.matthias.feedbacklibrary;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.matthias.feedbacklibrary.API.feedbackAPI;
import com.example.matthias.feedbacklibrary.configurations.Configuration;
import com.example.matthias.feedbacklibrary.configurations.OrchestratorConfiguration;
import com.example.matthias.feedbacklibrary.configurations.OrchestratorConfigurationItem;
import com.example.matthias.feedbacklibrary.feedbacks.AudioFeedback;
import com.example.matthias.feedbacklibrary.feedbacks.Feedback;
import com.example.matthias.feedbacklibrary.feedbacks.ScreenshotFeedback;
import com.example.matthias.feedbacklibrary.models.Mechanism;
import com.example.matthias.feedbacklibrary.utils.DialogUtils;
import com.example.matthias.feedbacklibrary.utils.Utils;
import com.example.matthias.feedbacklibrary.views.AudioMechanismView;
import com.example.matthias.feedbacklibrary.views.CategoryMechanismView;
import com.example.matthias.feedbacklibrary.views.MechanismView;
import com.example.matthias.feedbacklibrary.views.RatingMechanismView;
import com.example.matthias.feedbacklibrary.views.ScreenshotMechanismView;
import com.example.matthias.feedbacklibrary.views.TextMechanismView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The main activity where the feedback mechanisms are displayed.
 */
public class FeedbackActivity extends AppCompatActivity implements ScreenshotMechanismView.OnImageChangedListener {
    public final static String ANNOTATED_IMAGE_NAME_WITHOUT_STICKERS = "annotatedImageWithoutStickers.png";
    public final static String ANNOTATED_IMAGE_NAME_WITH_STICKERS = "annotatedImageWithStickers.png";
    public final static String CONFIGURATION_DIR = "configDir";
    public final static String DEFAULT_IMAGE_PATH = "defaultImagePath";
    public final static String IS_PUSH_STRING = "isPush";
    public final static String JSON_CONFIGURATION_FILE_NAME = "currentConfiguration.json";
    public final static String JSON_CONFIGURATION_STRING = "jsonConfigurationString";
    public final static String SELECTED_PULL_CONFIGURATION_INDEX_STRING = "selectedPullConfigurationIndex";
    public final static int TEXT_ANNOTATION_MAXIMUM = 4;
    // Microphone permission (android.permission-group.MICROPHONE)
    public static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 101;
    // Initialization
    public final static String EXTRA_KEY_APPLICATION_ID = "applicationId";
    public final static String EXTRA_KEY_BASE_URL = "baseURL";
    public final static String EXTRA_KEY_LANGUAGE = "language";
    private final static int REQUEST_CAMERA = 10;
    private final static int REQUEST_PHOTO = 11;
    private final static int REQUEST_ANNOTATE = 12;
    private static final String MULTIPART_FORM_DATA = "multipart/form-data";
    // Storage permission (android.permission-group.STORAGE)
    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 100;
    private feedbackAPI fbAPI;
    // Orchestrator configuration fetched from the orchestrator
    private OrchestratorConfigurationItem orchestratorConfigurationItem;
    // Orchestrator configuration initialized from the previously fetched orchestrator configuration
    private OrchestratorConfiguration orchestratorConfiguration;
    // Active configuration
    private Configuration activeConfiguration;
    // All mechanisms including inactive ones which represent the models
    private List<Mechanism> allMechanisms;
    // All views of active mechanisms which represent the views
    private List<MechanismView> allMechanismViews;
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

    private void closeProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @NonNull
    private RequestBody createRequestBody(@NonNull File file) {
        return RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), file);
    }

    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_PHOTO);
    }

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

    /**
     * This method performs a GET request to the feedback orchestrator in order to receive the configuration.
     *
     * @param applicationId the application to retrieve
     * @param language      the language
     */
    private void init(long applicationId, String baseURL, String language) {
        if (applicationId != -1 && baseURL != null && language != null) {
            Retrofit rtf = new Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).build();
            fbAPI = rtf.create(feedbackAPI.class);
            Call<OrchestratorConfigurationItem> result = fbAPI.getConfiguration(language, applicationId);
            this.language = language;

            // Asynchronous call
            if (result != null) {
                // Make progress dialog visible
                progressDialog = DialogUtils.createProgressDialog(FeedbackActivity.this, getResources().getString(R.string.supersede_feedbacklibrary_loading_string), false);
                progressDialog.show();
                result.enqueue(new Callback<OrchestratorConfigurationItem>() {
                    @Override
                    public void onFailure(Call<OrchestratorConfigurationItem> call, Throwable t) {
                        closeProgressDialog();
                        handleConfigurationRetrievalError();
                    }

                    @Override
                    public void onResponse(Call<OrchestratorConfigurationItem> call, Response<OrchestratorConfigurationItem> response) {
                        if (response.code() == 200) {
                            orchestratorConfigurationItem = response.body();
                            // Save the current configuration under FeedbackActivity.CONFIGURATION_DIR}/FeedbackActivity.JSON_CONFIGURATION_FILE_NAME
                            Gson gson = new Gson();
                            String jsonString = gson.toJson(orchestratorConfigurationItem);
                            Utils.saveStringContentToInternalStorage(getApplicationContext(), CONFIGURATION_DIR, JSON_CONFIGURATION_FILE_NAME, jsonString, MODE_PRIVATE);
                            initModel();
                            initView();
                            closeProgressDialog();
                        } else {
                            closeProgressDialog();
                            handleConfigurationRetrievalError();
                        }
                    }
                });
            } else {
                handleConfigurationRetrievalError();
            }
        } else {
            handleConfigurationRetrievalError();
        }
    }

    private void initModel() {
        if (orchestratorConfigurationItem != null) {
            orchestratorConfiguration = new OrchestratorConfiguration(orchestratorConfigurationItem, isPush(), getSelectedPullConfigurationIndex());
            activeConfiguration = orchestratorConfiguration.getActiveConfiguration();
            allMechanisms = activeConfiguration.getMechanisms();
        }
    }

    // TODO: Remove before release
    private void initOfflineConfiguration() {
        String jsonString;
        Gson gson = new Gson();
        //jsonString = Utils.readFileAsString("android_application_v1_offline.json", getAssets());
        jsonString = Utils.readFileAsString("android_application_v2_offline_multiple.json", getAssets());
        orchestratorConfigurationItem = gson.fromJson(jsonString, OrchestratorConfigurationItem.class);

        initModel();
        initView();
    }

    private void initView() {
        allMechanismViews = new ArrayList<>();
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.supersede_feedbacklibrary_feedback_activity_layout);

        if (linearLayout != null) {
            for (int i = 0; i < allMechanisms.size(); ++i) {
                if (allMechanisms.get(i).isActive()) {
                    MechanismView mechanismView = null;
                    View view = null;
                    String type = allMechanisms.get(i).getType();
                    switch (type) {
                        case Mechanism.ATTACHMENT_TYPE:
                            // TODO: Implement attachment mechanism
                            break;
                        case Mechanism.AUDIO_TYPE:
                            mechanismView = new AudioMechanismView(layoutInflater, allMechanisms.get(i), getResources(), this, getApplicationContext());
                            view = mechanismView.getEnclosingLayout();
                            break;
                        case Mechanism.CATEGORY_TYPE:
                            mechanismView = new CategoryMechanismView(layoutInflater, allMechanisms.get(i));
                            view = mechanismView.getEnclosingLayout();
                            break;
                        case Mechanism.RATING_TYPE:
                            mechanismView = new RatingMechanismView(layoutInflater, allMechanisms.get(i));
                            view = mechanismView.getEnclosingLayout();
                            break;
                        case Mechanism.SCREENSHOT_TYPE:
                            mechanismView = new ScreenshotMechanismView(layoutInflater, allMechanisms.get(i), this, allMechanismViews.size(), defaultImagePath);
                            view = mechanismView.getEnclosingLayout();
                            break;
                        case Mechanism.TEXT_TYPE:
                            mechanismView = new TextMechanismView(layoutInflater, allMechanisms.get(i));
                            view = mechanismView.getEnclosingLayout();
                            break;
                        default:
                            // Should never happen!
                            break;
                    }

                    if (mechanismView != null && view != null) {
                        allMechanismViews.add(mechanismView);
                        linearLayout.addView(view);
                    }
                }
            }

            layoutInflater.inflate(R.layout.send_feedback_layout, linearLayout);
        }
    }

    private boolean isPush() {
        return isPush;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_PHOTO) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
            } else if (requestCode == REQUEST_ANNOTATE && data != null) {
                // If mechanismViewId == -1, an error occurred
                int mechanismViewId = data.getIntExtra(Utils.EXTRA_KEY_MECHANISM_VIEW_ID, -1);
                if (mechanismViewId != -1) {
                    ScreenshotMechanismView screenshotMechanismView = (ScreenshotMechanismView) allMechanismViews.get(mechanismViewId);

                    // Sticker annotations
                    if (data.getBooleanExtra(Utils.EXTRA_KEY_HAS_STICKER_ANNOTATIONS, false)) {
                        screenshotMechanismView.setAllStickerAnnotations((HashMap<Integer, String>) data.getSerializableExtra(Utils.EXTRA_KEY_ALL_STICKER_ANNOTATIONS));
                    }
                    // Text annotations
                    if (data.getBooleanExtra(Utils.EXTRA_KEY_HAS_TEXT_ANNOTATIONS, false)) {
                        screenshotMechanismView.setAllTextAnnotations((HashMap<Integer, String>) data.getSerializableExtra(Utils.EXTRA_KEY_ALL_TEXT_ANNOTATIONS));
                    }

                    // Annotated image with stickers
                    String tempPathWithStickers = data.getStringExtra(Utils.EXTRA_KEY_ANNOTATED_IMAGE_PATH_WITH_STICKERS) + "/" + mechanismViewId + ANNOTATED_IMAGE_NAME_WITH_STICKERS;
                    screenshotMechanismView.setAnnotatedImagePath(tempPathWithStickers);
                    screenshotMechanismView.setPicturePath(tempPathWithStickers);
                    Bitmap annotatedBitmap = Utils.loadImageFromStorage(tempPathWithStickers);
                    if (annotatedBitmap != null) {
                        screenshotMechanismView.setPictureBitmap(annotatedBitmap);
                        screenshotMechanismView.getScreenShotPreviewImageView().setImageBitmap(annotatedBitmap);
                    }

                    // Annotated image without stickers
                    if (data.getStringExtra(Utils.EXTRA_KEY_ANNOTATED_IMAGE_PATH_WITHOUT_STICKERS) == null) {
                        screenshotMechanismView.setPicturePathWithoutStickers(null);
                    } else {
                        String tempPathWithoutStickers = data.getStringExtra(Utils.EXTRA_KEY_ANNOTATED_IMAGE_PATH_WITHOUT_STICKERS) + "/" + mechanismViewId + ANNOTATED_IMAGE_NAME_WITHOUT_STICKERS;
                        screenshotMechanismView.setPicturePathWithoutStickers(tempPathWithoutStickers);
                    }
                } else {
                    throw new RuntimeException("no " + Utils.EXTRA_KEY_MECHANISM_VIEW_ID + " provided.");
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

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

            // Save the current configuration under FeedbackActivity.CONFIGURATION_DIR}/FeedbackActivity.JSON_CONFIGURATION_FILE_NAME
            Utils.saveStringContentToInternalStorage(getApplicationContext(), CONFIGURATION_DIR, JSON_CONFIGURATION_FILE_NAME, jsonString, MODE_PRIVATE);
            orchestratorConfigurationItem = new Gson().fromJson(jsonString, OrchestratorConfigurationItem.class);
            initModel();
            initView();
        } else {
            // The feedback activity is started on behalf of the user

            // TODO: Uncomment before release
            // Get the application id and language
            init(intent.getLongExtra(EXTRA_KEY_APPLICATION_ID, -1L), baseURL, language);

            // TODO: Remove before release
            // Only for demo purposes
            //initOfflineConfiguration();
        }
    }

    @Override
    public void onImageAnnotate(ScreenshotMechanismView screenshotMechanismView) {
        Intent intent = new Intent(this, AnnotateImageActivity.class);
        if (screenshotMechanismView.getAllStickerAnnotations() != null && screenshotMechanismView.getAllStickerAnnotations().size() > 0) {
            intent.putExtra(Utils.EXTRA_KEY_HAS_STICKER_ANNOTATIONS, true);
            intent.putExtra(Utils.EXTRA_KEY_ALL_STICKER_ANNOTATIONS, screenshotMechanismView.getAllStickerAnnotations());
        }
        if (screenshotMechanismView.getAllTextAnnotations() != null && screenshotMechanismView.getAllTextAnnotations().size() > 0) {
            intent.putExtra(Utils.EXTRA_KEY_HAS_TEXT_ANNOTATIONS, true);
            intent.putExtra(Utils.EXTRA_KEY_ALL_TEXT_ANNOTATIONS, screenshotMechanismView.getAllTextAnnotations());
        }

        String path = screenshotMechanismView.getPicturePathWithoutStickers() == null ? screenshotMechanismView.getPicturePath() : screenshotMechanismView.getPicturePathWithoutStickers();
        intent.putExtra(Utils.EXTRA_KEY_MECHANISM_VIEW_ID, screenshotMechanismView.getMechanismViewIndex());
        intent.putExtra(Utils.EXTRA_KEY_IMAGE_PATCH, path);
        intent.putExtra(Utils.TEXT_ANNOTATION_COUNTER_MAXIMUM, TEXT_ANNOTATION_MAXIMUM);
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Resources res = getResources();
                    if (userScreenshotChosenTask.equals(res.getString(R.string.supersede_feedbacklibrary_photo_capture_text))) {
                    } else if (userScreenshotChosenTask.equals(res.getString(R.string.supersede_feedbacklibrary_library_chooser_text))) {
                        galleryIntent();
                    }
                } else {
                    // The user denied the permission
                    onRequestPermissionsResultDenied(requestCode, permissions, grantResults, Manifest.permission.READ_EXTERNAL_STORAGE,
                            R.string.supersede_feedbacklibrary_external_storage_permission_text,
                            getResources().getString(R.string.supersede_feedbacklibrary_external_storage_permission_text_instructions));
                }
                break;
            case PERMISSIONS_REQUEST_RECORD_AUDIO:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.supersede_feedbacklibrary_record_audio_permission_granted_text, Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    // The user denied the permission
                    onRequestPermissionsResultDenied(requestCode, permissions, grantResults, Manifest.permission.RECORD_AUDIO,
                            R.string.supersede_feedbacklibrary_record_audio_permission_text,
                            getResources().getString(R.string.supersede_feedbacklibrary_record_audio_permission_text_instructions));
                }
                break;
        }
    }

    private void onRequestPermissionsResultDenied(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults,
                                                  @NonNull final String permission, int dialogMessage, @NonNull String dialogInstructions) {
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
        if (tempMechanismViewId != -1 && allMechanismViews.get(tempMechanismViewId) instanceof ScreenshotMechanismView) {
            ScreenshotMechanismView screenshotMechanismView = (ScreenshotMechanismView) allMechanismViews.get(tempMechanismViewId);
            Uri selectedImage = data.getData();
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
        if (baseURL != null && language != null) {
            // The mechanism models are updated with the view values
            for (MechanismView mechanismView : allMechanismViews) {
                mechanismView.updateModel();
            }

            final ArrayList<String> messages = new ArrayList<>();
            if (validateInput(allMechanisms, messages)) {
                if (fbAPI == null) {
                    Retrofit rtf = new Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).build();
                    fbAPI = rtf.create(feedbackAPI.class);
                }

                Feedback feedback = new Feedback(allMechanisms);
                feedback.setTitle("Test title");
                feedback.setApplicationId(orchestratorConfiguration.getId());
                feedback.setConfigurationId(activeConfiguration.getId());
                feedback.setLanguage(language);
                feedback.setUserIdentification(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                //feedback.initContextInformation();

                // The JSON string of the feedback
                GsonBuilder builder = new GsonBuilder();
                builder.excludeFieldsWithoutExposeAnnotation();
                builder.serializeNulls();
                Gson gson = builder.create();
                Type feedbackType = new TypeToken<Feedback>() {
                }.getType();
                String feedbackJsonString = gson.toJson(feedback, feedbackType);
                RequestBody feedbackJSONPart = RequestBody.create(MediaType.parse("multipart/form-data"), feedbackJsonString);

                Map<String, RequestBody> files = new HashMap<>();
                // Audio multipart
                List<AudioFeedback> audioFeedbackList = feedback.getAudioFeedbacks();
                if (audioFeedbackList != null) {
                    for (int pos = 0; pos < audioFeedbackList.size(); ++pos) {
                        RequestBody requestBody = createRequestBody(new File(audioFeedbackList.get(pos).getAudioPath()));
                        String fileName = audioFeedbackList.get(pos).getFileName();
                        String key = String.format("%1$s\"; filename=\"%2$s", audioFeedbackList.get(pos).getPartString() + String.valueOf(pos), fileName);
                        files.put(key, requestBody);
                    }
                }
                // Screenshots multipart
                List<ScreenshotFeedback> screenshotFeedbackList = feedback.getScreenshotFeedbacks();
                if (screenshotFeedbackList != null) {
                    for (int pos = 0; pos < screenshotFeedbackList.size(); ++pos) {
                        RequestBody requestBody = createRequestBody(new File(screenshotFeedbackList.get(pos).getImagePath()));
                        String fileName = screenshotFeedbackList.get(pos).getFileName();
                        String key = String.format("%1$s\"; filename=\"%2$s", screenshotFeedbackList.get(pos).getPartString() + String.valueOf(pos), fileName);
                        files.put(key, requestBody);
                    }
                }

                // Send the feedback
                Call<JsonObject> result = fbAPI.createFeedbackVariant(language, feedbackJSONPart, files);
                if (result != null) {
                    result.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            DialogUtils.showInformationDialog(FeedbackActivity.this, new String[]{getResources().getString(R.string.supersede_feedbacklibrary_error_text)}, true);
                        }

                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                            if (response.code() == 200 || response.code() == 201) {
                                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.supersede_feedbacklibrary_success_text), Toast.LENGTH_SHORT);
                                toast.show();
                            } else {
                                DialogUtils.showInformationDialog(FeedbackActivity.this, new String[]{getResources().getString(R.string.supersede_feedbacklibrary_error_text)}, true);
                            }
                        }
                    });
                } else {
                    DialogUtils.showInformationDialog(FeedbackActivity.this, new String[]{getResources().getString(R.string.supersede_feedbacklibrary_error_text)}, true);
                }
            } else {
                DialogUtils.showInformationDialog(this, messages.toArray(new String[messages.size()]), false);
            }
        } else {
            DialogUtils.showInformationDialog(this, new String[]{getResources().getString(R.string.supersede_feedbacklibrary_error_text)}, true);
        }
    }

    public void sendStub(View view) {
        System.out.println("sendStub");
        if (baseURL != null && language != null) {
            // The mechanism models are updated with the view values
            for (MechanismView mechanismView : allMechanismViews) {
                mechanismView.updateModel();
            }

            final ArrayList<String> messages = new ArrayList<>();
            if (validateInput(allMechanisms, messages)) {
                if (fbAPI == null) {
                    Retrofit rtf = new Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).build();
                    fbAPI = rtf.create(feedbackAPI.class);
                }

                Feedback feedback = new Feedback(allMechanisms);
                feedback.setTitle("Test title");
                feedback.setApplicationId(orchestratorConfiguration.getId());
                feedback.setConfigurationId(activeConfiguration.getId());
                feedback.setLanguage(language);
                feedback.setUserIdentification(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
                feedback.initContextInformation();

                // The JSON string of the feedback
                GsonBuilder builder = new GsonBuilder();
                builder.excludeFieldsWithoutExposeAnnotation();
                builder.serializeNulls();
                Gson gson = builder.create();
                Type feedbackType = new TypeToken<Feedback>() {
                }.getType();
                String feedbackJsonString = gson.toJson(feedback, feedbackType);
                RequestBody feedbackJSONPart = RequestBody.create(MediaType.parse("multipart/form-data"), feedbackJsonString);

                System.out.println(feedbackJsonString);

                Map<String, RequestBody> files = new HashMap<>();
                // Audio multipart
                List<AudioFeedback> audioFeedbackList = feedback.getAudioFeedbacks();
                if (audioFeedbackList != null) {
                    for (int pos = 0; pos < audioFeedbackList.size(); ++pos) {
                        RequestBody requestBody = createRequestBody(new File(audioFeedbackList.get(pos).getAudioPath()));
                        String fileName = audioFeedbackList.get(pos).getFileName();
                        String key = String.format("%1$s\"; filename=\"%2$s", audioFeedbackList.get(pos).getPartString() + String.valueOf(pos + 1), fileName);
                        files.put(key, requestBody);
                    }
                }
                // Screenshots multipart
                List<ScreenshotFeedback> screenshotFeedbackList = feedback.getScreenshotFeedbacks();
                if (screenshotFeedbackList != null) {
                    for (int pos = 0; pos < screenshotFeedbackList.size(); ++pos) {
                        RequestBody requestBody = createRequestBody(new File(screenshotFeedbackList.get(pos).getImagePath()));
                        String fileName = screenshotFeedbackList.get(pos).getFileName();
                        String key = String.format("%1$s\"; filename=\"%2$s", screenshotFeedbackList.get(pos).getPartString() + String.valueOf(pos + 1), fileName);
                        files.put(key, requestBody);
                    }
                }
            } else {
                DialogUtils.showInformationDialog(this, messages.toArray(new String[messages.size()]), false);
            }
        } else {
            DialogUtils.showInformationDialog(this, new String[]{getResources().getString(R.string.supersede_feedbacklibrary_error_text)}, true);
        }
    }

    /**
     * This method checks if the inputs of the active mechanisms are valid.
     *
     * @param allMechanisms all mechanism to check for their input validity
     * @param errorMessages error messages to show if the validation fails
     * @return true if all inputs are valid, false otherwise
     */
    private boolean validateInput(List<Mechanism> allMechanisms, List<String> errorMessages) {
        // Append an error message and return. The user is confronted with one error message at a time.
        for (Mechanism mechanism : allMechanisms) {
            if (mechanism.isActive()) {
                boolean isValid = mechanism.isValid(errorMessages);
                if (!isValid) {
                    return false;
                }
            }
        }
        return true;
    }
}