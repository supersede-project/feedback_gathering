package ch.uzh.supersede.feedbacklibrary.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
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
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.feedbacks.AudioFeedback;
import ch.uzh.supersede.feedbacklibrary.feedbacks.Feedback;
import ch.uzh.supersede.feedbacklibrary.feedbacks.ScreenshotFeedback;
import ch.uzh.supersede.feedbacklibrary.models.Mechanism;
import ch.uzh.supersede.feedbacklibrary.services.FeedbackService;
import ch.uzh.supersede.feedbacklibrary.stubs.OrchestratorStub;
import ch.uzh.supersede.feedbacklibrary.stubs.OrchestratorStub.MechanismBuilder;
import ch.uzh.supersede.feedbacklibrary.utils.DialogUtils;
import ch.uzh.supersede.feedbacklibrary.utils.Utils;
import ch.uzh.supersede.feedbacklibrary.components.views.AudioMechanismView;
import ch.uzh.supersede.feedbacklibrary.components.views.CategoryMechanismView;
import ch.uzh.supersede.feedbacklibrary.components.views.MechanismView;
import ch.uzh.supersede.feedbacklibrary.components.views.RatingMechanismView;
import ch.uzh.supersede.feedbacklibrary.components.views.ScreenshotMechanismView;
import ch.uzh.supersede.feedbacklibrary.components.views.TextMechanismView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.FeedbackActivityConstants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.FeedbackActivityConstants.TAG;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.ScreenshotConstants.*;

/**
 * The main activity where the feedback mechanisms are displayed.
 */
public class FeedbackActivity extends AbstractBaseActivity implements AudioMechanismView.MultipleAudioMechanismsListener {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //TODO [jfo]: remove me
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

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
        if (!isPush && selectedPullConfigurationIndex != -1  && jsonString != null) {
            // The feedback activity is started on behalf of a triggered pull configuration
            Log.v(TAG, "The feedback activity is started via a PULL configuration");

            // Save the current configuration under FeedbackActivity.CONFIGURATION_DIR}/FeedbackActivity.JSON_CONFIGURATION_FILE_NAME
            FeedbackDatabase.getInstance(getApplicationContext()).writeString(JSON_CONFIGURATION_FILE_NAME,jsonString);
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
        onPostCreate();
    }


    private void closeProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
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
                        FeedbackDatabase.getInstance(getApplicationContext()).writeString(JSON_CONFIGURATION_FILE_NAME,jsonString);
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
            if (BuildConfig.DEBUG) {
                new MechanismBuilder(this,getApplicationContext(),getResources(),linearLayout,layoutInflater)
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
            initCopyByEmailLayout(layoutInflater, linearLayout);
            layoutInflater.inflate(R.layout.utility_feedback_button, linearLayout);

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

    private void initCopyByEmailLayout(LayoutInflater layoutInflater, LinearLayout linearLayout) {
        //Check sharedPreferences on email existing
        sharedPreferences = getSharedPreferences("FeedbackApp", Context.MODE_PRIVATE);
        savedEmail = sharedPreferences.getString("email", "");

        View view = layoutInflater.inflate(R.layout.mechanism_email, null, false);
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
                    emailEditText.setEnabled(true);
                    emailEditText.setVisibility(View.VISIBLE);
                } else {
                    emailEditText.setEnabled(false);
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
        ScreenshotMechanismView screenshotMechanismView = null;
        for (MechanismView mechanismView : mechanismViews){
            if (mechanismView instanceof ScreenshotMechanismView){
                screenshotMechanismView = (ScreenshotMechanismView) mechanismView;
            }
        }
        if (screenshotMechanismView == null){
            return;
        }
        // Sticker annotations
        if (data.getBooleanExtra(EXTRA_KEY_HAS_STICKER_ANNOTATIONS, false)) {
            screenshotMechanismView.setAllStickerAnnotations((HashMap<Integer, String>) data.getSerializableExtra(EXTRA_KEY_ALL_STICKER_ANNOTATIONS));
        }
        screenshotMechanismView.refreshPreview(this);
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
                Utils.storeImageToDatabase(this,tempPictureBitmap);
                screenshotMechanismView.refreshPreview(this);
            }
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
        Utils.wipeImages(this);
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

    //TODO: Funktionalitaet geht noch nicht.
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
                    String category = categoryMechanismView.getCategorySpinner().getSelectedItem().toString();

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
}