package com.example.matthias.feedbacklibrary;

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
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.matthias.feedbacklibrary.API.feedbackAPI;
import com.example.matthias.feedbacklibrary.configurations.Configuration;
import com.example.matthias.feedbacklibrary.configurations.OrchestratorConfiguration;
import com.example.matthias.feedbacklibrary.configurations.OrchestratorConfigurationItem;
import com.example.matthias.feedbacklibrary.feedbacks.Feedback;
import com.example.matthias.feedbacklibrary.feedbacks.ScreenshotFeedback;
import com.example.matthias.feedbacklibrary.models.AttachmentMechanism;
import com.example.matthias.feedbacklibrary.models.AudioMechanism;
import com.example.matthias.feedbacklibrary.models.Mechanism;
import com.example.matthias.feedbacklibrary.models.ScreenshotMechanism;
import com.example.matthias.feedbacklibrary.utils.DialogUtils;
import com.example.matthias.feedbacklibrary.utils.Utils;
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

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
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
    private final static int REQUEST_CAMERA = 10;
    private final static int REQUEST_PHOTO = 11;
    private final static int REQUEST_ANNOTATE = 12;
    private static final String MULTIPART_FORM_DATA = "multipart/form-data";
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
    private ProgressDialog progressDialog;
    private String userScreenshotChosenTask = "";

    @NonNull
    private RequestBody createRequestBody(@NonNull File file) {
        return RequestBody.create(
                MediaType.parse(MULTIPART_FORM_DATA), file);
    }

    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_PHOTO);
    }

    public long getSelectedPullConfigurationIndex() {
        return selectedPullConfigurationIndex;
    }

    /**
     * This method performs a GET request to feedback orchestrator in order to receive the configuration file (format JSON).
     * If successful, it initializes the models and views.
     */
    private void init() {
        Retrofit rtf = new Retrofit.Builder().baseUrl(feedbackAPI.endpoint).addConverterFactory(GsonConverterFactory.create()).build();
        fbAPI = rtf.create(feedbackAPI.class);
        long applicationId = 8;
        Call<OrchestratorConfigurationItem> result = fbAPI.getConfiguration("en", applicationId);

        // Asynchronous call
        if (result != null) {
            result.enqueue(new Callback<OrchestratorConfigurationItem>() {
                @Override
                public void onFailure(Call<OrchestratorConfigurationItem> call, Throwable t) {
                }

                @Override
                public void onResponse(Call<OrchestratorConfigurationItem> call, Response<OrchestratorConfigurationItem> response) {
                    // TODO: Ask Florian if NULL values are serialized or left out
                    orchestratorConfigurationItem = response.body();
                    /**
                     * Save the current configuration under {@link FeedbackActivity#CONFIGURATION_DIR}/{@link FeedbackActivity#JSON_CONFIGURATION_FILE_NAME}.
                     */
                    Gson gson = new Gson();
                    String jsonString = gson.toJson(orchestratorConfigurationItem);
                    Utils.saveStringContentToInternalStorage(getApplicationContext(), CONFIGURATION_DIR, JSON_CONFIGURATION_FILE_NAME, jsonString, MODE_PRIVATE);
                    initModel();
                    initView();
                }
            });
        } else {
            // Should never happen!
        }
    }

    /**
     * This method initializes the models.
     */
    private void initModel() {
        if (orchestratorConfigurationItem != null) {
            orchestratorConfiguration = new OrchestratorConfiguration(orchestratorConfigurationItem, isPush(), getSelectedPullConfigurationIndex());
            activeConfiguration = orchestratorConfiguration.getActiveConfiguration();
            allMechanisms = activeConfiguration.getMechanisms();
        }
    }

    private void initOfflineConfiguration() {
        System.out.println("initOfflineConfiguration executed (ONLY with PUSH correct!)");

        String jsonString;
        Gson gson = new Gson();
        // For single category
        jsonString = Utils.readFileAsString("feedback_orchestrator_adapted_single_selection.json", getAssets());
        // For multiple category
        //jsonString = Utils.readFileAsString("feedback_orchestrator_adapted_multiple_selection.json", getAssets());
        orchestratorConfigurationItem = gson.fromJson(jsonString, OrchestratorConfigurationItem.class);

        initModel();
        initView();
    }

    /**
     * This method initializes the views.
     */
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
                            // TODO: Implement audio mechanism
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
                            mechanismView = new ScreenshotMechanismView(layoutInflater, allMechanisms.get(i), FeedbackActivity.this, allMechanismViews.size(), defaultImagePath);
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

        // Make the progress dialog disappear after successfully loading the models and views
        progressDialog.dismiss();
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

        // Get the default image path for the screenshot if present
        Intent intent = getIntent();
        defaultImagePath = intent.getStringExtra(DEFAULT_IMAGE_PATH);

        isPush = intent.getBooleanExtra(IS_PUSH_STRING, true);
        selectedPullConfigurationIndex = intent.getLongExtra(SELECTED_PULL_CONFIGURATION_INDEX_STRING, -1);
        String jsonString = intent.getStringExtra(JSON_CONFIGURATION_STRING);

        // Make progress dialog visible
        View view = findViewById(R.id.supersede_feedbacklibrary_feedback_activity_layout);
        if (view != null) {
            progressDialog = DialogUtils.createProgressDialog(view.getContext(), getResources().getString(R.string.supersede_feedbacklibrary_loading_string), false);
            progressDialog.show();
        }

        if (!isPush && selectedPullConfigurationIndex != -1 && jsonString != null) {
            // The feedback activity is started on behalf of a triggered pull configuration

            /**
             * Save the current configuration under {@link FeedbackActivity#CONFIGURATION_DIR}/{@link FeedbackActivity#JSON_CONFIGURATION_FILE_NAME}.
             */
            Utils.saveStringContentToInternalStorage(getApplicationContext(), CONFIGURATION_DIR, JSON_CONFIGURATION_FILE_NAME, jsonString, MODE_PRIVATE);
            orchestratorConfigurationItem = new Gson().fromJson(jsonString, OrchestratorConfigurationItem.class);
            initModel();
            initView();
        } else {
            // The feedback activity is started on behalf of the user

            // Actual init
            //init();

            // Only for demo purposes
            initOfflineConfiguration();
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
                boolean result = Utils.checkPermission_READ_EXTERNAL_STORAGE(FeedbackActivity.this, Utils.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                if (items[item].equals(res.getString(R.string.supersede_feedbacklibrary_photo_capture_text))) {
                    userScreenshotChosenTask = res.getString(R.string.supersede_feedbacklibrary_photo_capture_text);
                    if (result) {
                    }
                } else if (items[item].equals(res.getString(R.string.supersede_feedbacklibrary_library_chooser_text))) {
                    userScreenshotChosenTask = res.getString(R.string.supersede_feedbacklibrary_library_chooser_text);
                    if (result)
                        galleryIntent();
                } else if (items[item].equals(res.getString(R.string.supersede_feedbacklibrary_cancel_string))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utils.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Resources res = getResources();
                    if (userScreenshotChosenTask.equals(res.getString(R.string.supersede_feedbacklibrary_photo_capture_text))) {
                    } else if (userScreenshotChosenTask.equals(res.getString(R.string.supersede_feedbacklibrary_library_chooser_text))) {
                        galleryIntent();
                    }
                }
                break;
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
     * This method performs a POST request in order to send the feedback to the feedback repository.
     */
    public void sendButtonClicked(View view) {
        // The mechanism models are updated with the view values
        for (MechanismView mechanismView : allMechanismViews) {
            mechanismView.updateModel();
        }

        final ArrayList<String> messages = new ArrayList<>();
        if (validateInput(allMechanisms, messages)) {
            if (fbAPI == null) {
                Retrofit rtf = new Retrofit.Builder().baseUrl(feedbackAPI.endpoint).addConverterFactory(GsonConverterFactory.create()).build();
                fbAPI = rtf.create(feedbackAPI.class);
            }

            Feedback feedback = new Feedback(allMechanisms);
            feedback.setApplicationId(orchestratorConfiguration.getId());
            feedback.setConfigurationId(activeConfiguration.getId());
            feedback.setLanguage("EN");
            feedback.setTitle("Feedback test title");

            // The JSON string of the feedback
            GsonBuilder builder = new GsonBuilder();
            builder.excludeFieldsWithoutExposeAnnotation();
            // TODO: Ask Florian if NULL values should be serialized or left out --> we should serialize also the null values (consistency!)
            builder.serializeNulls();
            Gson gson = builder.create();
            Type feedbackType = new TypeToken<Feedback>() {
            }.getType();
            String feedbackJsonString = gson.toJson(feedback, feedbackType);
            RequestBody feedbackJSONPart = RequestBody.create(MediaType.parse("multipart/form-data"), feedbackJsonString);

            List<String> screenshotPaths = new ArrayList<>();
            List<String> audioPaths = new ArrayList<>();
            List<List<String>> attachmentPaths = new ArrayList<>();

            // Screenshots multipart
            List<ScreenshotFeedback> screenshotFeedbacks = feedback.getScreenshotFeedbacks();
            // Variant 1?
            Map<String, RequestBody> files = new HashMap<>();
            /*
            // Variant 2?
            MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            */
            for (int pos = 0; pos < screenshotFeedbacks.size(); ++pos) {
                // Variant 1?
                RequestBody requestBody = createRequestBody(new File(screenshotFeedbacks.get(pos).getImagePath()));
                String fileName = screenshotFeedbacks.get(pos).getFileName();
                String key = String.format("%1$s\"; filename=\"%2$s", screenshotFeedbacks.get(pos).getPartString() + String.valueOf(pos + 1), fileName);
                files.put(key, requestBody);

                /*
                // Variant 2?
                RequestBody requestBody = RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), new File(screenshotFeedbacks.get(pos).getImagePath()));
                multipartBodyBuilder = multipartBodyBuilder.addFormDataPart(screenshotFeedbacks.get(pos).getPartString() + String.valueOf(pos + 1), screenshotFeedbacks.get(pos).getFileName(), requestBody);
                */
            }


            /*
            Call<JsonObject> result;
            // Variant 1?
            result = fbAPI.createFeedbackVariant1("en", feedbackJSONPart, files);
            // Variant 2?
            result = fbAPI.createFeedbackVariant2("en", feedbackJSONPart, multipartBodyBuilder.build());

            if (result != null) {
                result.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        messages.add(getResources().getString(R.string.supersede_feedbacklibrary_error_text));
                        DialogUtils.DataDialog d = DialogUtils.DataDialog.newInstance(messages);
                        d.show(getFragmentManager(), "dataDialog");
                    }

                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.code() == 201) {
                            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.supersede_feedbacklibrary_success_text), Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
            }
            */
        } else {
            DialogUtils.DataDialog d = DialogUtils.DataDialog.newInstance(messages);
            d.show(getFragmentManager(), "dataDialog");
        }
    }

    public void sendStub(View view) {
        // The mechanism models are updated with the view values
        for (MechanismView mechanismView : allMechanismViews) {
            mechanismView.updateModel();
        }

        final ArrayList<String> messages = new ArrayList<>();
        if (validateInput(allMechanisms, messages)) {
            if (fbAPI == null) {
                Retrofit rtf = new Retrofit.Builder().baseUrl(feedbackAPI.endpoint).addConverterFactory(GsonConverterFactory.create()).build();
                fbAPI = rtf.create(feedbackAPI.class);
            }

            Feedback feedback = new Feedback(allMechanisms);
            feedback.setApplicationId(orchestratorConfiguration.getId());
            feedback.setConfigurationId(activeConfiguration.getId());
            feedback.setLanguage("EN");
            feedback.setTitle("Feedback test title");

            // The JSON string of the feedback
            GsonBuilder builder = new GsonBuilder();
            builder.excludeFieldsWithoutExposeAnnotation();
            // TODO: Ask Florian if NULL values should be serialized or left out --> we should serialize also the null values (consistency!)
            builder.serializeNulls();
            Gson gson = builder.create();
            Type feedbackType = new TypeToken<Feedback>() {
            }.getType();
            String feedbackJsonString = gson.toJson(feedback, feedbackType);
            RequestBody feedbackJSONPart = RequestBody.create(MediaType.parse("multipart/form-data"), feedbackJsonString);

            List<String> screenshotPaths = new ArrayList<>();
            List<String> audioPaths = new ArrayList<>();
            List<List<String>> attachmentPaths = new ArrayList<>();

            // Screenshots multipart
            List<ScreenshotFeedback> screenshotFeedbacks = feedback.getScreenshotFeedbacks();
            // Variant 1?
            Map<String, RequestBody> files = new HashMap<>();
            /*
            // Variant 2?
            MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            */
            if (screenshotFeedbacks != null) {
                for (int pos = 0; pos < screenshotFeedbacks.size(); ++pos) {
                    // Variant 1?
                    RequestBody requestBody = createRequestBody(new File(screenshotFeedbacks.get(pos).getImagePath()));
                    String fileName = screenshotFeedbacks.get(pos).getFileName();
                    String key = String.format("%1$s\"; filename=\"%2$s", screenshotFeedbacks.get(pos).getPartString() + String.valueOf(pos + 1), fileName);
                    files.put(key, requestBody);

                    // TODO: Must key and filename be congruent?
                    System.out.println("key == " + key);
                    System.out.println("" + screenshotFeedbacks.get(pos).getPartString() + String.valueOf(pos + 1) + "\"; filename=\"" + fileName + ".jpg");

                /*
                // Variant 2?
                RequestBody requestBody = RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), new File(screenshotFeedbacks.get(pos).getImagePath()));
                multipartBodyBuilder = multipartBodyBuilder.addFormDataPart(screenshotFeedbacks.get(pos).getPartString() + String.valueOf(pos + 1), screenshotFeedbacks.get(pos).getFileName(), requestBody);
                */
                }
            }

            /*
            Call<JsonObject> result;
            // Variant 1?
            result = fbAPI.createFeedbackVariant1("en", feedbackJSONPart, files);
            // Variant 2?
            result = fbAPI.createFeedbackVariant2("en", feedbackJSONPart, multipartBodyBuilder.build());

            if (result != null) {
                result.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        messages.add(getResources().getString(R.string.supersede_feedbacklibrary_error_text));
                        DialogUtils.DataDialog d = DialogUtils.DataDialog.newInstance(messages);
                        d.show(getFragmentManager(), "dataDialog");
                    }

                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.code() == 201) {
                            Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.supersede_feedbacklibrary_success_text), Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
            }
            */
        } else {
            DialogUtils.DataDialog d = DialogUtils.DataDialog.newInstance(messages);
            d.show(getFragmentManager(), "dataDialog");
        }
    }

    /**
     * This method checks if the inputs of the active mechanisms are valid.
     *
     * @param allMechanisms all mechanism to check for their input validity
     * @param errorMessages error messages to show if the validation fails
     * @return true if the all inputs are valid, false otherwise
     */
    private boolean validateInput(List<Mechanism> allMechanisms, List<String> errorMessages) {
        /*
         * Two different options:
         * 1. Append all error messages and show all of them to the user.
         * --> Problem: potentially having a lot of error messages inside a tiny dialog.
         * 2. Append an error message and return.
         * --> The user is confronted with one error message at a time.
         *
         * Option 2 is implemented.
         */

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