/**
 * Copyright [2016] [Matthias Scherrer]
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.uzh.supersede.feedbacklibrary;

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
import android.support.v7.app.AppCompatActivity;
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
import com.google.gson.JsonObject;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import ch.uzh.supersede.feedbacklibrary.API.feedbackAPI;
import ch.uzh.supersede.feedbacklibrary.configurations.Configuration;
import ch.uzh.supersede.feedbacklibrary.configurations.OrchestratorConfiguration;
import ch.uzh.supersede.feedbacklibrary.configurations.OrchestratorConfigurationItem;
import ch.uzh.supersede.feedbacklibrary.feedbacks.AudioFeedback;
import ch.uzh.supersede.feedbacklibrary.feedbacks.Feedback;
import ch.uzh.supersede.feedbacklibrary.feedbacks.ScreenshotFeedback;
import ch.uzh.supersede.feedbacklibrary.models.Mechanism;
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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The main activity where the feedback mechanisms are displayed.
 */
public class FeedbackActivity extends AppCompatActivity implements ScreenshotMechanismView.OnImageChangedListener, AudioMechanismView.MultipleAudioMechanismsListener {
    public final static String ANNOTATED_IMAGE_NAME_WITHOUT_STICKERS = "annotatedImageWithoutStickers.png";
    public final static String ANNOTATED_IMAGE_NAME_WITH_STICKERS = "annotatedImageWithStickers.png";
    public final static String CONFIGURATION_DIR = "configDir";
    public final static String DEFAULT_IMAGE_PATH = "defaultImagePath";
    public final static String IS_PUSH_STRING = "isPush";
    public final static String JSON_CONFIGURATION_FILE_NAME = "currentConfiguration.json";
    public final static String JSON_CONFIGURATION_STRING = "jsonConfigurationString";
    public final static String SELECTED_PULL_CONFIGURATION_INDEX_STRING = "selectedPullConfigurationIndex";
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
    private static final String TAG = "FeedbackActivity";
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
    private String email;
    private Map<String, RequestBody> files = new HashMap<>();

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

    @NonNull
    private RequestBody createRequestBody(@NonNull File file) {
        return RequestBody.create(MediaType.parse(MULTIPART_FORM_DATA), file);
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

    private void initModel() {
        if (orchestratorConfigurationItem != null) {
            orchestratorConfiguration = new OrchestratorConfiguration(orchestratorConfigurationItem, isPush(), getSelectedPullConfigurationIndex());
            activeConfiguration = orchestratorConfiguration.getActiveConfiguration();
            allMechanisms = activeConfiguration.getMechanisms();
        }
    }

    private void initView() {
        allMechanismViews = new ArrayList<>();
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.supersede_feedbacklibrary_feedback_activity_layout);

        if (linearLayout != null) {

            orderMechanisms();

            for (int i = 0; i < allMechanisms.size(); ++i) {
                if (allMechanisms.get(i) != null && allMechanisms.get(i).isActive()) {
                    MechanismView mechanismView = null;
                    View view = null;
                    String type = allMechanisms.get(i).getType();
                    switch (type) {
                        case Mechanism.ATTACHMENT_TYPE:
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
                            Log.wtf(TAG, "Unknown mechanism type '" + type + "'");
                            break;
                    }

                    if (mechanismView != null && view != null) {
                        allMechanismViews.add(mechanismView);
                        linearLayout.addView(view);
                    }
                }
            }

            initCopyByEmailLayout(layoutInflater, linearLayout);
            layoutInflater.inflate(R.layout.send_feedback_layout, linearLayout);
        }
    }

    private void initCopyByEmailLayout(LayoutInflater layoutInflater, LinearLayout linearLayout) {
        //CHeck sharedPreferences on email existing
        sharedPreferences = getSharedPreferences("FeedbackApp", Context.MODE_PRIVATE);
        savedEmail = sharedPreferences.getString("email","");

        View view = layoutInflater.inflate(R.layout.send_by_email_layout, null, false);
        linearLayout.addView(view);

        emailEditText = (EditText)view.findViewById(R.id.sbe_email_et);
        if(!TextUtils.isEmpty(savedEmail)) emailEditText.setText(savedEmail);

        getCopyCheckBox = (CheckBox)view.findViewById(R.id.sbe_get_copy_cb);

        getCopyCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    emailEditText.setVisibility(View.VISIBLE);
                }
                else {
                    emailEditText.setVisibility(View.GONE);
                }
            }
        });
    }

    private void orderMechanisms() {
        ArrayList<Mechanism> list = new ArrayList<>();
        /*
        list.add(allMechanisms.get(2));
        list.add(allMechanisms.get(0));
        list.add(allMechanisms.get(1));
        list.add(allMechanisms.get(3));
        list.add(allMechanisms.get(4));
        */

        //allMechanisms = list;
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
                    Log.e(TAG, "Failed to annotate the image. No mechanismViewID provided");
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        } else {
            // The feedback activity is started on behalf of the user
            Log.v(TAG, "The feedback activity is started via a PUSH configuration");

            // TODO: remove before release
            //initOfflineConfiguration();

            // TODO: uncomment before release
            // Get the application id and language
            init(intent.getLongExtra(EXTRA_KEY_APPLICATION_ID, -1L), baseURL, language);
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
        intent.putExtra(Utils.TEXT_ANNOTATION_COUNTER_MAXIMUM, screenshotMechanismView.getMaxNumberTextAnnotation());
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
    public void onImageClick(ScreenshotMechanismView screenshotMechanismView) {
        tempMechanismViewId = screenshotMechanismView.getMechanismViewIndex();
        galleryIntent();
    }

    @Override
    public void onRecordStart(long audioMechanismId) {
        for (MechanismView mechanismView : allMechanismViews) {
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
        for (MechanismView mechanismView : allMechanismViews) {
            if (mechanismView instanceof AudioMechanismView) {
                ((AudioMechanismView) mechanismView).setAllButtonsClickable(true);
            }
        }
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

        if (!isOnline()) {
            DialogUtils.showInformationDialog(this, new String[]{getResources().getString(R.string.check_network_state)}, true);
            return;
        }

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

                Call<ResponseBody> checkUpAndRunning = fbAPI.pingRepository();
                if (checkUpAndRunning != null) {
                    checkUpAndRunning.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.e(TAG, "Failed to ping the server. onFailure method called", t);
                            DialogUtils.showInformationDialog(FeedbackActivity.this, new String[]{getResources().getString(R.string.supersede_feedbacklibrary_error_text)}, true);
                        }

                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.code() == 200) {
                                feedback = new Feedback(allMechanisms);
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

                                Call<JsonObject> result = fbAPI.createFeedbackVariant(language, feedback.getApplicationId(), jsonPart, multipartFiles);
                                if (result != null) {
                                    result.enqueue(new Callback<JsonObject>() {
                                        @Override
                                        public void onFailure(Call<JsonObject> call, Throwable t) {
                                            Log.e(TAG, "Failed to send the feedback. onFailure method called", t);
                                            DialogUtils.showInformationDialog(FeedbackActivity.this, new String[]{getResources().getString(R.string.supersede_feedbacklibrary_error_text)}, true);
                                        }

                                        @Override
                                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                            if (response.code() == 201) {
                                                Log.i(TAG, "Feedback successfully sent");
                                                Toast toast = Toast.makeText(getApplicationContext(), getResources().getString(R.string.supersede_feedbacklibrary_success_text), Toast.LENGTH_SHORT);
                                                toast.show();
                                            } else {
                                                Log.e(TAG, "Failed to send the feedback. Response code == " + response.code());
                                                DialogUtils.showInformationDialog(FeedbackActivity.this, new String[]{getResources().getString(R.string.supersede_feedbacklibrary_error_text)}, true);
                                            }
                                        }
                                    });
                                } else {
                                    Log.e(TAG, "Failed to send the feebdkack. Call<JsonObject> result is null");
                                    DialogUtils.showInformationDialog(FeedbackActivity.this, new String[]{getResources().getString(R.string.supersede_feedbacklibrary_error_text)}, true);
                                }

                                checkAndSendViaEmail();

                            } else {
                                Log.e(TAG, "The server is not up and running. Response code == " + response.code());
                                DialogUtils.showInformationDialog(FeedbackActivity.this, new String[]{getResources().getString(R.string.supersede_feedbacklibrary_error_text)}, true);
                            }
                        }
                    });
                } else {
                    Log.e(TAG, "Failed to ping the server. Call<ResponseBody> checkUpAndRunning result is null");
                    DialogUtils.showInformationDialog(FeedbackActivity.this, new String[]{getResources().getString(R.string.supersede_feedbacklibrary_error_text)}, true);
                }
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
        if (getCopyCheckBox.isChecked())
        {
            email = emailEditText.getText().toString();
            if (Utils.isEmailValid(email))
            {
                sendCopyByEmail();
            }

            else DialogUtils.showInformationDialog(this, new String[]{getResources().getString(R.string.invalid_email)}, true);
        }
    }

    private void sendCopyByEmail() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Thread emailThread = new Thread(new Runnable() {
            @Override
            public void run() {
                //TODO replace with your email
                final String username = "supersede.zurich@gmail.com";
                final String password = "University2017";

                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");

                Session session = Session.getInstance(props,
                        new javax.mail.Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(username, password);
                            }
                        });
                try {
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(username));
                    message.setRecipients(Message.RecipientType.TO,
                            InternetAddress.parse(email));
                    message.setSubject("Copy of your feedback");

                    BodyPart messageBodyPart = new MimeBodyPart();

                    String feedbackText = feedback.getTextFeedbacks().get(0).getText();

                    CategoryMechanismView categoryMechanismView = (CategoryMechanismView) allMechanismViews.get(4);
                    String category = categoryMechanismView.getCustomSpinner().getSelectedItem().toString();

                    if(category.equals("My feedback is about…")) category = "-";

                    RatingMechanismView ratingMechanismView = (RatingMechanismView) allMechanismViews.get(3);
                    String rating = String.valueOf(ratingMechanismView.getRating());

                    // Now set the actual message
                    messageBodyPart.setText(String.format("Feedback text: %s, Rating: %s, Category: %s", feedbackText, rating, category));

                    Multipart multipart = new MimeMultipart();

                    // Set text message part
                    multipart.addBodyPart(messageBodyPart);

                    for (ScreenshotFeedback feedback:screenshotFeedbackList) {
                        addAttachment(multipart, feedback.getImagePath());
                    }
                    for (AudioFeedback feedback:audioFeedbackList) {
                        addAttachment(multipart, feedback.getAudioPath());
                    }

                    message.setContent(multipart);

                    Transport.send(message);

                    if (TextUtils.isEmpty(savedEmail)){
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString("email", email);

                        editor.apply();
                    }

                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        emailThread.start();
    }

    private void addAttachment(Multipart multipart, String filePath) throws MessagingException {

        URI uri = null;
        try {
            uri = new URI(filePath);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        String[] segments = uri.getPath().split("/");
        String filename = segments[segments.length-1];

        DataSource source = new FileDataSource(filePath);
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);
        multipart.addBodyPart(messageBodyPart);
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
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