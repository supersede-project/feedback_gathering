package com.example.matthias.feedbacklibrary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.matthias.feedbacklibrary.API.feedbackAPI;
import com.example.matthias.feedbacklibrary.feedbacks.Feedback;
import com.example.matthias.feedbacklibrary.models.FeedbackConfiguration;
import com.example.matthias.feedbacklibrary.models.FeedbackConfigurationItem;
import com.example.matthias.feedbacklibrary.models.Mechanism;
import com.example.matthias.feedbacklibrary.models.TextMechanism;
import com.example.matthias.feedbacklibrary.utils.Utils;
import com.example.matthias.feedbacklibrary.views.MechanismView;
import com.example.matthias.feedbacklibrary.views.RatingMechanismView;
import com.example.matthias.feedbacklibrary.views.ScreenshotMechanismView;
import com.example.matthias.feedbacklibrary.views.TextMechanismView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The main activity where the feedback items are displayed
 */
public class FeedbackActivity extends AppCompatActivity {
    public final static String IMAGE_NAME = "annotatedImage.jpg";
    public final static String DEFAULT_IMAGE_PATH = "defaultImagePath";
    private static final String endpoint = "http://ec2-54-175-37-30.compute-1.amazonaws.com/";
    private final static int REQUEST_CAMERA = 10;
    private final static int REQUEST_PHOTO = 11;
    private final static int REQUEST_ANNOTATE = 12;
    private feedbackAPI fbAPI;
    // List of feedback configuration items fetched from the orchestrator
    private List<FeedbackConfigurationItem> configuration;
    // Feedback configuration initialized from the previously fetched feedback configuration items
    private FeedbackConfiguration feedbackConfiguration;
    // All mechanisms (including inactive ones) --> models
    private List<Mechanism> allMechanisms;
    // All views representing active mechanisms --> view
    private List<MechanismView> allMechanismViews;
    // Path of the annotated image
    private String annotatedImagePath = null;
    private ProgressDialog progressDialog;
    private Button annotateScreenshotButton;
    private Button deleteScreenshotButton;
    private ImageView screenShotPreviewImageView;
    private Bitmap pictureBitmap;
    private String picturePath;
    private String defaultImagePath;
    private String userScreenshotChosenTask = "";

    public void annotateImage() {
        Intent intent = new Intent(this, AnnotateImageActivity.class);
        intent.putExtra("imagePath", picturePath);
        startActivityForResult(intent, REQUEST_ANNOTATE);
    }

    private void cameraIntent() {
        //TODO: Implement image capture
    }

    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_PHOTO);
    }

    /**
     * This method performs a GET request to feedback orchestrator in order to receive the configuration file (format JSON).
     * If successful, it initializes the model and view.
     */
    private void init() {
        Call<List<FeedbackConfigurationItem>> result = null;
        result = fbAPI.getConfiguration();

        // Asynchronous call
        if (result != null) {
            result.enqueue(new Callback<List<FeedbackConfigurationItem>>() {
                @Override
                public void onFailure(Call<List<FeedbackConfigurationItem>> call, Throwable t) {
                }

                @Override
                public void onResponse(Call<List<FeedbackConfigurationItem>> call, Response<List<FeedbackConfigurationItem>> response) {
                    configuration = response.body();
                    initModel();
                    initView();
                }
            });
        } else {
            // Should never happen!
        }
    }

    /**
     * This method initializes the model.
     */
    private void initModel() {
        if (configuration != null) {
            // TODO: Save original configuration on the device --> possibly SQLite database
            feedbackConfiguration = new FeedbackConfiguration(configuration);
            allMechanisms = feedbackConfiguration.getAllMechanisms();
        }
    }

    private void initOfflineConfiguration() {
        String jsonString;
        Gson gson = new Gson();
        Type listType = new TypeToken<List<FeedbackConfigurationItem>>() {
        }.getType();

        //jsonString = Utils.readFileAsString("offline_configuration_file_text_variables.json", getAssets());
        jsonString = Utils.readFileAsString("offline_configuration_file_text_variables_material_design.json", getAssets());
        configuration = gson.fromJson(jsonString, listType);
        initModel();
        initView();
    }

    private void initScreenshotView(View view) {
        screenShotPreviewImageView = (ImageView) view.findViewById(R.id.supersede_feedbacklibrary_screenshot_imageview);
        boolean isEnabled = false;

        // Use the default image path for the screenshot if present
        if (defaultImagePath != null) {
            picturePath = defaultImagePath;
            annotatedImagePath = picturePath;
            pictureBitmap = BitmapFactory.decodeFile(picturePath);
            screenShotPreviewImageView.setBackground(null);
            screenShotPreviewImageView.setImageBitmap(pictureBitmap);
            isEnabled = true;
        }
        Button selectScreenshotButton = (Button) view.findViewById(R.id.supersede_feedbacklibrary_select_screenshot_btn);
        selectScreenshotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        annotateScreenshotButton = (Button) view.findViewById(R.id.supersede_feedbacklibrary_annotate_screenshot_btn);
        annotateScreenshotButton.setEnabled(isEnabled);
        annotateScreenshotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                annotateImage();
            }
        });
        deleteScreenshotButton = (Button) view.findViewById(R.id.supersede_feedbacklibrary_remove_screenshot_btn);
        deleteScreenshotButton.setEnabled(isEnabled);
        deleteScreenshotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pictureBitmap = null;
                picturePath = null;
                annotatedImagePath = null;
                annotateScreenshotButton.setEnabled(false);
                deleteScreenshotButton.setEnabled(false);
                screenShotPreviewImageView.setImageBitmap(null);
                screenShotPreviewImageView.setBackgroundResource(R.drawable.camera_picture_big);
            }
        });
    }

    /**
     * This method initializes the view.
     */
    public void initView() {
        allMechanismViews = new ArrayList<>();
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.feedback_activity_layout);

        if (linearLayout != null) {
            for (int i = 0; i < allMechanisms.size(); ++i) {
                if (allMechanisms.get(i).isActive()) {
                    MechanismView mechanismView = null;
                    View view = null;
                    String type = allMechanisms.get(i).getType();

                    if (type.equals("TEXT_TYPE")) {
                        mechanismView = new TextMechanismView(layoutInflater, allMechanisms.get(i));
                        view = mechanismView.getEnclosingLayout();
                    } else if (type.equals("RATING_TYPE")) {
                        mechanismView = new RatingMechanismView(layoutInflater, allMechanisms.get(i));
                        view = mechanismView.getEnclosingLayout();
                    } else if (type.equals("AUDIO_TYPE")) {
                        // TODO: Implement audio mechanism
                    } else if (type.equals("SCREENSHOT_TYPE")) {
                        mechanismView = new ScreenshotMechanismView(layoutInflater, allMechanisms.get(i));
                        view = mechanismView.getEnclosingLayout();
                        initScreenshotView(view);
                    } else {
                        // Should never happen!
                    }

                    if (mechanismView != null && view != null) {
                        allMechanismViews.add(mechanismView);
                        linearLayout.addView(view);
                    }
                }
            }

            layoutInflater.inflate(R.layout.send_feedback_layout, linearLayout);
        }

        // After successfully loading the model data and view, make the progress dialog disappear
        progressDialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_PHOTO)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
            else if (requestCode == REQUEST_ANNOTATE && data != null) {
                annotatedImagePath = data.getStringExtra("annotatedImagePath") + "/" + IMAGE_NAME;
                picturePath = annotatedImagePath;
                Bitmap annotatedBitmap = Utils.loadImageFromStorage(annotatedImagePath);
                if (annotatedBitmap != null) {
                    pictureBitmap = annotatedBitmap;
                    screenShotPreviewImageView.setImageBitmap(pictureBitmap);
                }
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        annotateScreenshotButton.setEnabled(true);
        deleteScreenshotButton.setEnabled(true);
        screenShotPreviewImageView.setBackground(null);
        screenShotPreviewImageView.setImageBitmap(thumbnail);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Get the default image path for the screenshot if present
        Intent intent = getIntent();
        defaultImagePath = intent.getStringExtra(DEFAULT_IMAGE_PATH);

        // Make progress dialog visible
        progressDialog = new ProgressDialog((findViewById(R.id.feedback_activity_layout)).getContext());
        progressDialog.setTitle("Loading. Please wait.");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        Retrofit rtf = new Retrofit.Builder().baseUrl(endpoint).addConverterFactory(GsonConverterFactory.create()).build();
        fbAPI = rtf.create(feedbackAPI.class);

        //init();
        initOfflineConfiguration();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utils.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userScreenshotChosenTask.equals("Take a Photo")) {
                        cameraIntent();
                    } else if (userScreenshotChosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    // Code for denial
                }
                break;
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        Uri selectedImage = data.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        picturePath = cursor.getString(columnIndex);
        cursor.close();
        annotatedImagePath = picturePath;
        pictureBitmap = BitmapFactory.decodeFile(picturePath);
        screenShotPreviewImageView.setBackground(null);
        screenShotPreviewImageView.setImageBitmap(pictureBitmap);
        annotateScreenshotButton.setEnabled(true);
        deleteScreenshotButton.setEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LinearLayout emptyLayout = (LinearLayout) findViewById(R.id.supersede_feedbacklibrary_empty_layout);
        if (emptyLayout != null) {
            emptyLayout.requestFocus();
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take a Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(FeedbackActivity.this);
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utils.checkPermission_READ_EXTERNAL_STORAGE(FeedbackActivity.this, Utils.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                if (items[item].equals("Take a Photo")) {
                    userScreenshotChosenTask = "Take a Photo";
                    if (result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userScreenshotChosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    /*
     * This method performs a POST request in order to send the feedback to the feedback repository.
     */
    public void sendButtonClicked(View view) {
        String screenShotImagePath = annotatedImagePath != null ? annotatedImagePath : "";

        // The mechanism models are updated with the view values
        for (MechanismView mechanismView : allMechanismViews) {
            mechanismView.updateModel();
            if (mechanismView instanceof ScreenshotMechanismView) {
                ((ScreenshotMechanismView) mechanismView).setAnnotatedImagePath(screenShotImagePath);
            }
        }

        final ArrayList<String> messages = new ArrayList<>();
        if (validateInput(allMechanisms, messages)) {
            Feedback feedback = new Feedback(allMechanisms);

            Call<JsonObject> result;
            feedback.setApplication(feedbackConfiguration.getApplication());
            feedback.setUser(feedbackConfiguration.getUser());
            feedback.setConfigVersion(feedbackConfiguration.getConfigVersion());

            // JSON string of feedback
            Gson gson = new Gson();
            Type feedbackType = new TypeToken<Feedback>() {
            }.getType();
            String feedbackJsonString = gson.toJson(feedback, feedbackType);
            // Screenshot file
            File imageFile = new File(screenShotImagePath);

            RequestBody feedbackJsonPart = RequestBody.create(MediaType.parse("multipart/form-data"), feedbackJsonString);
            RequestBody feedbackScreenshotPart = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
            if (!screenShotImagePath.equals("")) {
                result = fbAPI.createFeedbackMultipart(feedbackScreenshotPart, feedbackJsonPart);
            } else {
                result = fbAPI.createFeedback(feedbackJsonPart);
            }

            if (result != null) {
                result.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        messages.add("Oops. Something went wrong!");
                        Utils.DataDialog d = Utils.DataDialog.newInstance(messages);
                        d.show(getFragmentManager(), "dataDialog");
                    }

                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.code() == 201) {
                            Toast toast = Toast.makeText(getApplicationContext(), "Your feedback was successfully sent. Thank you", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
            }
        } else {
            Utils.DataDialog d = Utils.DataDialog.newInstance(messages);
            d.show(getFragmentManager(), "dataDialog");
        }
    }

    public void sendDummy(View view) {
        String screenShotImagePath = annotatedImagePath != null ? annotatedImagePath : "";

        // The mechanism models are updated with the view values
        for (MechanismView mechanismView : allMechanismViews) {
            mechanismView.updateModel();
            if (mechanismView instanceof ScreenshotMechanismView) {
                ((ScreenshotMechanismView) mechanismView).setAnnotatedImagePath(screenShotImagePath);
            }
        }

        final ArrayList<String> messages = new ArrayList<>();
        if (validateInput(allMechanisms, messages)) {
            System.out.println("Validation successful");
        } else {
            Utils.DataDialog d = Utils.DataDialog.newInstance(messages);
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