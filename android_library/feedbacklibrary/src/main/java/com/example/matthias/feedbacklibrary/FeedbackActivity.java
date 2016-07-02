package com.example.matthias.feedbacklibrary;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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
    private static final String endpoint = "http://ec2-54-175-37-30.compute-1.amazonaws.com/";
    private final static int REQUEST_CAMERA = 10;
    private final static int REQUEST_PHOTO = 11;
    private final static int REQUEST_ANNOTATE = 12;
    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
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
    private String userScreenshotChosenTask = "";

    public void annotateImage() {
        Intent intent = new Intent(this, AnnotateActivity.class);
        intent.putExtra("imagePath", picturePath);
        startActivityForResult(intent, REQUEST_ANNOTATE);
    }

    private void cameraIntent() {
        //TODO: Implement image capture
    }

    /**
     * Check permission at runtime required for Android versions 6 (API version 23) and higher
     *
     * @param context the context
     * @return true if permission is granted, false otherwise
     */
    private boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission Request");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_PHOTO);
    }

    /**
     * GET request to feedback orchestrator to receive the configuration file (format JSON).
     * If successful, initialize the model and view.
     */
    private void init() {
        Call<List<FeedbackConfigurationItem>> result = null;
        result = fbAPI.getConfiguration();

        // asynchronous call
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
     * Initialize the model data
     */
    private void initModel() {
        if (configuration != null) {
            feedbackConfiguration = new FeedbackConfiguration(configuration);
            // TODO: Save original configuration on the device --> probably SQLite database
            allMechanisms = feedbackConfiguration.getAllMechanisms();
        }
    }

    private void initOfflineConfiguration() {
        String jsonString;
        Gson gson = new Gson();
        Type listType = new TypeToken<List<FeedbackConfigurationItem>>() {
        }.getType();

        jsonString = readJSONConfigurationFile("offline_configuration_file_screenshot_active.json");
        configuration = gson.fromJson(jsonString, listType);
        initModel();
        initView();
    }

    private void initScreenshotView(View view) {
        Button selectScreenshotButton = (Button) view.findViewById(R.id.supersede_feedbacklibrary_select_screenshot_btn);
        selectScreenshotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        annotateScreenshotButton = (Button) view.findViewById(R.id.supersede_feedbacklibrary_annotate_screenshot_btn);
        annotateScreenshotButton.setEnabled(false);
        annotateScreenshotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                annotateImage();
            }
        });
        deleteScreenshotButton = (Button) view.findViewById(R.id.supersede_feedbacklibrary_remove_screenshot_btn);
        deleteScreenshotButton.setEnabled(false);
        deleteScreenshotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pictureBitmap = null;
                annotateScreenshotButton.setEnabled(false);
                deleteScreenshotButton.setEnabled(false);
                screenShotPreviewImageView.setImageBitmap(null);
                screenShotPreviewImageView.setBackgroundResource(R.drawable.camera_picture_big);
            }
        });
        screenShotPreviewImageView = (ImageView) view.findViewById(R.id.supersede_feedbacklibrary_screenshot_imageview);
    }

    /**
     * Initialize the view
     */
    public void initView() {
        allMechanismViews = new ArrayList<>();
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.feedback_activity_layout);

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
                    // TODO
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

        View sendLayout = layoutInflater.inflate(R.layout.send_feedback_layout, null);
        if (sendLayout != null) {
            linearLayout.addView(sendLayout);
        }

        // After successfully loading the model data and view, make the progress dialog disappear
        progressDialog.dismiss();
    }

    private Bitmap loadImageFromStorage(String path) {
        try {
            File f = new File(path);
            return BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
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
                Bitmap annotatedBitmap = loadImageFromStorage(annotatedImagePath);
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
            case PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userScreenshotChosenTask.equals("Take a Photo")) {
                        cameraIntent();
                    } else if (userScreenshotChosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
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

    private String readJSONConfigurationFile(String fileName) {
        String ret = "";

        try {
            InputStream inputStream = getAssets().open(fileName);

            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder out = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    out.append(line);
                }
                reader.close();
                return out.toString();
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.toString());
        } catch (IOException e) {
            System.out.println("Cannot read file: " + e.toString());
        }

        return ret;
    }

    private void selectImage() {
        final CharSequence[] items = {"Take a Photo", "Choose from Library", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(FeedbackActivity.this);
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = checkPermission(FeedbackActivity.this);
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
     * User sends the feedback via a POST request to the feedback repository
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
            result = fbAPI.createFeedback(feedback);

            if (result != null) {
                result.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        messages.add("Oops. Something went wrong!");
                        DataDialog d = DataDialog.newInstance(messages);
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
            DataDialog d = DataDialog.newInstance(messages);
            d.show(getFragmentManager(), "dataDialog");
        }
    }

    // Move later on into a helper/utils class
    private boolean validateInput(List<Mechanism> allMechanisms, List<String> errorMessages) {
        boolean isValid = true;
        for (Mechanism mechanism : allMechanisms) {
            if (mechanism.getType().equals("TEXT_TYPE")) {
                int length = ((TextMechanism) mechanism).getInputText().length();
                int maxLength = ((TextMechanism) mechanism).getMaxLength();
                if (length > maxLength) {
                    isValid = false;
                    errorMessages.add("Text has " + length + " characters. Maximum allowed characters are " + maxLength);
                }
            }
        }

        return isValid;
    }

    // Move later on into a helper/utils class
    public static class DataDialog extends DialogFragment {
        static DataDialog newInstance(ArrayList<String> messages) {
            DataDialog f = new DataDialog();
            Bundle args = new Bundle();
            args.putStringArrayList("messages", messages);
            f.setArguments(args);
            return f;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            List<String> messages = getArguments().getStringArrayList("messages");
            StringBuilder message = new StringBuilder("");
            for (String s : messages) {
                message.append(s).append(".");
            }
            builder.setMessage(message.toString()).setNegativeButton("Close", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            return builder.create();
        }
    }
}