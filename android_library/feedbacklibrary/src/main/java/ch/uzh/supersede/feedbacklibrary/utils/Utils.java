/*
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
package ch.uzh.supersede.feedbacklibrary.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.uzh.supersede.feedbacklibrary.API.feedbackAPI;
import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.activities.FeedbackActivity;
import ch.uzh.supersede.feedbacklibrary.configurations.ConfigurationItem;
import ch.uzh.supersede.feedbacklibrary.configurations.OrchestratorConfigurationItem;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.FeedbackActivityConstants.*;

/**
 * Class with various helper methods
 */
public class Utils {
    // Audio
    public static final String AUDIO_DIR = "audioDir";
    public static final String AUDIO_EXTENSION = "m4a";
    public static final String AUDIO_FILENAME = "audioFile";
    // Screenshot
    public static final String EXTRA_KEY_ALL_STICKER_ANNOTATIONS = "allStickerAnnotations";
    public static final String EXTRA_KEY_ALL_TEXT_ANNOTATIONS = "allTextAnnotations";
    public static final String EXTRA_KEY_ANNOTATED_IMAGE_PATH_WITHOUT_STICKERS = "annotatedImagePathWithoutStickers";
    public static final String EXTRA_KEY_ANNOTATED_IMAGE_PATH_WITH_STICKERS = "annotatedImagePathWithStickers";
    public static final String EXTRA_KEY_HAS_STICKER_ANNOTATIONS = "hasStickerAnnotations";
    public static final String EXTRA_KEY_HAS_TEXT_ANNOTATIONS = "hasTextAnnotations";
    public static final String EXTRA_KEY_IMAGE_PATCH = "imagePath";
    public static final String EXTRA_KEY_MECHANISM_VIEW_ID = "mechanismViewID";
    public static final String SEPARATOR = "::;;::;;";
    public static final String TEXT_ANNOTATION_COUNTER_MAXIMUM = "textAnnotationCounterMaximum";
    private static final String SCREENSHOTS_DIR_NAME = "Screenshots";
    // General
    private static final String TAG = "Utils";

    @NonNull
    private static String captureScreenshot(final Activity activity) {
        // Create the 'Screenshots' folder if it does not already exist
        File screenshotDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), SCREENSHOTS_DIR_NAME);
        screenshotDir.mkdirs();

        // Image name 'Screenshot_YearMonthDay-HourMinuteSecondMillisecond.png'
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        String imageName = "Screenshot_" + calendar.get(Calendar.YEAR) + (calendar.get(Calendar.MONTH) + 1) + calendar.get(Calendar.DAY_OF_MONTH);
        imageName += "-" + calendar.get(Calendar.HOUR_OF_DAY) + calendar.get(Calendar.MINUTE) + calendar.get(Calendar.SECOND) + calendar.get(Calendar.MILLISECOND) + ".png";
        File screenshotFile = new File(screenshotDir, imageName);

        // Create the screenshot file
        try {
            if (screenshotFile.exists()) {
                screenshotFile.delete();
            }
            screenshotFile.createNewFile();
        } catch (IOException e) {
            Log.e(TAG, "Failed to create a new file", e);
        }

        // Capture the current screen
        View rootView = activity.getWindow().getDecorView().getRootView();
        rootView.setDrawingCacheEnabled(true);
        Bitmap imageBitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(screenshotFile);
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            Log.e(TAG, "Failed to write the bitmap to the file", e);
        }

        // Add the screenshot image to the Media Provider's database
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(screenshotFile);
        mediaScanIntent.setData(contentUri);
        activity.sendBroadcast(mediaScanIntent);

        return screenshotFile.getAbsolutePath();
    }

    /**
     * This method checks a single permission at runtime. Required for Android versions Marshmallow (API version 23) and higher.
     * The request code must be handled in the onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) method
     * which has to be overridden in each activity that needs to request runtime permission.
     *
     * @param context       the context
     * @param requestCode   the request code to be handled in the onRequestPermissionsResult method of the calling activity
     * @param permission    the requested permission
     * @param dialogTitle   the dialog title for the rationale
     * @param dialogMessage the dialog message for the rationale
     * @return true if permission is granted, false otherwise
     */
    public static boolean checkSinglePermission(@NonNull final Context context, final int requestCode, @NonNull final String permission, final String dialogTitle, final String dialogMessage, final boolean showRequestPermissionRationale) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                if (showRequestPermissionRationale && ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle(dialogTitle);
                    alertBuilder.setMessage(dialogMessage);
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, requestCode);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{permission}, requestCode);
                }
                return false;
            }
        }
        return true;

    }

    /**
     * This method creates a temporary file in the cache directory.
     *
     * @param context the context of the application
     * @param prefix  the prefix, e.g., crop
     * @param suffix  the suffix, e.g., .jpg
     * @return the created file, or null if an exception occurred
     */
    @Nullable
    public static File createTempChacheFile(Context context, String prefix, String suffix) {
        try {
            return File.createTempFile(prefix, suffix, context.getCacheDir());
        } catch (IOException e) {
            Log.e(TAG, "Failed to create a temporary file", e);
        }
        return null;
    }

    /**
     * This method returns the boolean value of an integer.
     *
     * @param input the input value
     * @return the boolean value corresponding to the input value
     */
    public static boolean intToBool(int input) {
        return input == 1;
    }

    /**
     * This method loads an image as a bitmap from the specific path.
     *
     * @param path the absolute path of the image file
     * @return the bitmap
     */
    @Nullable
    public static Bitmap loadImageFromStorage(String path) {
        try {
            File f = new File(path);
            return BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File was not found", e);
        }
        return null;
    }

    /**
     * This method is used in the host application in the onRequestPermissionsResult method in case if a PUSH feedback is triggered.
     *
     * @param requestCode   the request code to be handled in the onRequestPermissionsResult method of the calling activity
     * @param permissions   the permissions
     * @param grantResults  the granted results
     * @param activity      the activity from where the method is called
     * @param permission    the requested permission
     * @param dialogTitle   the dialog title for the rationale
     * @param dialogMessage the dialog message for the rationale
     */
    public static void onRequestPermissionsResultCase(final int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults,
                                                      @NonNull final Activity activity, @NonNull final String permission, final int dialogTitle,
                                                      final int dialogMessage, final long applicationId, @NonNull final String baseURL, @NonNull final String language) {
        final Intent intent = new Intent(activity, FeedbackActivity.class);
        intent.putExtra(EXTRA_KEY_APPLICATION_ID, applicationId);
        intent.putExtra(EXTRA_KEY_BASE_URL, baseURL);
        intent.putExtra(EXTRA_KEY_LANGUAGE, language);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission was already granted. Taking a screenshot of the current screen automatically and open the FeedbackActivityConstants from the feedback library
            startActivity(activity, intent, baseURL, true);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                // The user denied the permission without checking 'Never ask again'. Show the rationale
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);
                alertBuilder.setTitle(dialogTitle);
                alertBuilder.setMessage(dialogMessage);
                alertBuilder.setPositiveButton(R.string.supersede_feedbacklibrary_retry_string, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
                    }
                });
                alertBuilder.setNegativeButton(R.string.supersede_feedbacklibrary_not_now_text, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startActivity(activity, intent, baseURL, false);
                    }
                });
                alertBuilder.setCancelable(false);
                alertBuilder.show();
            } else {
                // Open the FeedbackActivityConstants from the feedback library without automatically taking a screenshot
                startActivity(activity, intent, baseURL, false);
            }
        }
    }

    /**
     * This method reads a specific file from the assets resource folder and returns it as a string.
     *
     * @param fileName     the file to read from
     * @param assetManager the asset manager
     * @return the file content as a string, or the empty string if an error occurred
     */
    public static String readFileAsString(String fileName, AssetManager assetManager) {
        String ret = "";

        try {
            InputStream inputStream = assetManager.open(fileName);

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
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    /**
     * This method saves the bitmap in a specific file.
     *
     * @param file        the file to store the bitmap in
     * @param bitmapImage the bitmap to store
     * @param format      the bitmap compress format
     * @param quality     the quality
     * @return true on success, false otherwise
     */
    public static boolean saveBitmapToFile(File file, Bitmap bitmapImage, Bitmap.CompressFormat format, int quality) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
            bitmapImage.compress(format, quality, fos);
            fos.flush();
            fos.close();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Failed to write the bitmap to the file", e);
        }
        return false;
    }

    /**
     * This method saves the bitmap to the internal storage.
     *
     * @param applicationContext the application context
     * @param dirName            the directory name, e.g., "imageDir"
     * @param imageName          the name of the image
     * @param bitmapImage        the image as a bitmap
     * @param mode               the mode, e.g., Context.MODE_PRIVATE
     * @param format             the format
     * @param quality            the quality
     * @return the absolute path to the directory where the image is stored
     */
    @NonNull
    public static String saveBitmapToInternalStorage(Context applicationContext, String dirName, String imageName, Bitmap bitmapImage, int mode, Bitmap.CompressFormat format, int quality) {
        File directory = applicationContext.getDir(dirName, mode);
        File myPath = new File(directory, imageName);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(myPath);
            bitmapImage.compress(format, quality, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            Log.e(TAG, "Failed to write the bitmap to the file", e);
        }
        return directory.getAbsolutePath();
    }

    /**
     * This method saves the string content to the internal storage.
     *
     * @param applicationContext the application context
     * @param dirName            the directory name, e.g., "configDir"
     * @param fileName           the name of the file
     * @param str                the file content as a string
     * @param mode               the mode, e.g., Context.MODE_PRIVATE
     * @return true on success, false otherwise
     */
    public static boolean saveStringContentToInternalStorage(Context applicationContext, String dirName, String fileName, String str, int mode) {
        File directory = applicationContext.getDir(dirName, mode);
        File myPath = new File(directory, fileName);
        FileWriter out = null;
        try {
            out = new FileWriter(myPath);
            out.write(str);
            out.flush();
            out.close();
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Failed to write the content to the file", e);
        }finally {
            if (out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    Log.e(TAG, "Failed to close the FileWriter.", e);
                }
            }
        }
        return false;
    }

    /**
     * This method scales the bitmap according to a maximum width and height keeping the aspect ratio.
     *
     * @param bitmap    the original bitmap
     * @param maxWidth  the maximum width to scale
     * @param maxHeight the minimum width to scale
     * @return the scaled bitmap
     */
    public static Bitmap scaleBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (width > height) {
            // Landscape
            float ratio = (float) width / maxWidth;
            width = maxWidth;
            height = (int) (height / ratio);
        } else if (height > width) {
            // Portrait
            float ratio = (float) height / maxHeight;
            height = maxHeight;
            width = (int) (width / ratio);
        } else {
            // Square
            height = maxHeight;
            width = maxWidth;
        }

        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    private static void startActivity(@NonNull final Activity activity, @NonNull final Intent intent,
                                      @NonNull String baseURL, final boolean isCapturingScreenshot) {
        Retrofit rtf = new Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).build();
        feedbackAPI fbAPI = rtf.create(feedbackAPI.class);
        Call<ResponseBody> checkUpAndRunning = fbAPI.pingOrchestrator();

        if (checkUpAndRunning != null) {
            checkUpAndRunning.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e(TAG, "Failed to ping the server. onFailure method called", t);
                    DialogUtils.showInformationDialog(activity, new String[]{activity.getResources().getString(R.string.supersede_feedbacklibrary_feedback_application_unavailable_text)}, true);
                }

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        if (isCapturingScreenshot) {
                            String defaultImagePath = captureScreenshot(activity);
                            intent.putExtra(DEFAULT_IMAGE_PATH, defaultImagePath);
                        }
                        activity.startActivity(intent);
                    } else {
                        Log.e(TAG, "The server is not up and running. Response code == " + response.code());
                        DialogUtils.showInformationDialog(activity, new String[]{activity.getResources().getString(R.string.supersede_feedbacklibrary_feedback_application_unavailable_text)}, true);
                    }
                }
            });
        } else {
            Log.e(TAG, "Failed to ping the server. Call<ResponseBody> checkUpAndRunning result is null");
            DialogUtils.showInformationDialog(activity, new String[]{activity.getResources().getString(R.string.supersede_feedbacklibrary_feedback_application_unavailable_text)}, true);
        }
    }

    /**
     * This method takes a screenshot of the current screen automatically and opens the FeedbackActivityConstants from the feedback library in case if a PUSH feedback is triggered.
     *
     * @param baseURL       the base URL
     * @param activity      the activity in which the method is called
     * @param applicationId the application id
     * @param language      the language
     */
    public static void startActivityWithScreenshotCapture(@NonNull final String baseURL, @NonNull final Activity activity, final long applicationId, @NonNull final String language) {
        Retrofit rtf = new Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).build();
        feedbackAPI fbAPI = rtf.create(feedbackAPI.class);
        Call<ResponseBody> checkUpAndRunning = fbAPI.pingOrchestrator();

        if (checkUpAndRunning != null) {
            checkUpAndRunning.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e(TAG, "Failed to ping the server. onFailure method called", t);
                    DialogUtils.showInformationDialog(activity, new String[]{activity.getResources().getString(R.string.supersede_feedbacklibrary_feedback_application_unavailable_text)}, true);
                }

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        Intent intent = new Intent(activity, FeedbackActivity.class);
                        String defaultImagePath = Utils.captureScreenshot(activity);
                        intent.putExtra(DEFAULT_IMAGE_PATH, defaultImagePath);
                        intent.putExtra(EXTRA_KEY_APPLICATION_ID, applicationId);
                        intent.putExtra(EXTRA_KEY_BASE_URL, baseURL);
                        intent.putExtra(EXTRA_KEY_LANGUAGE, language);
                        activity.startActivity(intent);
                    } else {
                        Log.e(TAG, "The server is not up and running. Response code == " + response.code());
                        DialogUtils.showInformationDialog(activity, new String[]{activity.getResources().getString(R.string.supersede_feedbacklibrary_feedback_application_unavailable_text)}, true);
                    }
                }
            });
        } else {
            Log.e(TAG, "Failed to ping the server. Call<ResponseBody> checkUpAndRunning result is null");
            DialogUtils.showInformationDialog(activity, new String[]{activity.getResources().getString(R.string.supersede_feedbacklibrary_feedback_application_unavailable_text)}, true);
        }
    }

    /**
     * This method opens the FeedbackActivityConstants from the feedback library in case if a PULL feedback is triggered with a random PULL configuration.
     *
     * @param baseURL       the base URL
     * @param activity      the activity in which the method is called
     * @param applicationId the application id
     * @param language      the language
     */
    public static void triggerRandomPullFeedback(@NonNull final String baseURL, @NonNull final Activity activity, final long applicationId, final @NonNull String language) {
        Retrofit rtf = new Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).build();
        feedbackAPI fbAPI = rtf.create(feedbackAPI.class);
        final Call<ResponseBody> checkUpAndRunning = fbAPI.pingOrchestrator();

        if (checkUpAndRunning != null) {
            checkUpAndRunning.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e(TAG, "Failed to ping the server. onFailure method called", t);
                }

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        Retrofit rtf = new Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).build();
                        feedbackAPI fbAPI = rtf.create(feedbackAPI.class);
                        Call<OrchestratorConfigurationItem> result = fbAPI.getConfiguration(language, applicationId);

                        // Asynchronous call
                        if (result != null) {
                            result.enqueue(new Callback<OrchestratorConfigurationItem>() {

                                @Override
                                public void onFailure(Call<OrchestratorConfigurationItem> call, Throwable t) {
                                    Log.e(TAG, "Failed to retrieve the configuration. onFailure method called", t);
                                    DialogUtils.showInformationDialog(activity, new String[]{activity.getResources().getString(R.string.supersede_feedbacklibrary_feedback_application_unavailable_text)}, true);
                                }

                                @Override
                                public void onResponse(Call<OrchestratorConfigurationItem> call, Response<OrchestratorConfigurationItem> response) {
                                    if (response.code() == 200) {
                                        Log.i(TAG, "Configuration successfully retrieved");
                                        OrchestratorConfigurationItem configuration = response.body();
                                        if (configuration != null) {
                                            List<ConfigurationItem> configurationItems = configuration.getConfigurationItems();
                                            List<Long> shuffleIds = new ArrayList<>();
                                            Map<Long, List<Map<String, Object>>> idParameters = new HashMap<>();
                                            for (ConfigurationItem configurationItem : configurationItems) {
                                                if (configurationItem.getType().equals("PULL")) {
                                                    shuffleIds.add(configurationItem.getId());
                                                    idParameters.put(configurationItem.getId(), configurationItem.getGeneralConfigurationItem().getParameters());
                                                }
                                            }

                                            Random rnd = new Random(System.nanoTime());
                                            Collections.shuffle(shuffleIds, rnd);
                                            for (int i = 0; i < shuffleIds.size(); ++i) {
                                                double likelihood = -1;
                                                // If no "showIntermediateDialog" is provided, show it
                                                boolean showIntermediateDialog = true;
                                                for (Map<String, Object> parameter : idParameters.get(shuffleIds.get(i))) {
                                                    String key = (String) parameter.get("key");
                                                    // Likelihood
                                                    if (key.equals("likelihood")) {
                                                        likelihood = Double.parseDouble((String) parameter.get("value"));
                                                    }
                                                    // Intermediate dialog
                                                    if (key.equals("showIntermediateDialog")) {
                                                        showIntermediateDialog = (Utils.intToBool(((Double) parameter.get("value")).intValue()));
                                                    }
                                                }

                                                if (!(rnd.nextDouble() > likelihood)) {
                                                    Intent intent = new Intent(activity, FeedbackActivity.class);
                                                    GsonBuilder gsonBuilder = new GsonBuilder();
                                                    gsonBuilder.setLenient();
                                                    Gson gson = gsonBuilder.create();
                                                    String jsonString = gson.toJson(configuration);
                                                    intent.putExtra(IS_PUSH_STRING, false);
                                                    intent.putExtra(JSON_CONFIGURATION_STRING, jsonString);
                                                    intent.putExtra(SELECTED_PULL_CONFIGURATION_INDEX_STRING, shuffleIds.get(i));
                                                    intent.putExtra(EXTRA_KEY_BASE_URL, baseURL);
                                                    intent.putExtra(EXTRA_KEY_LANGUAGE, language);
                                                    if (!showIntermediateDialog) {
                                                        // Start the feedback activity without asking the user
                                                        activity.startActivity(intent);
                                                        break;
                                                    } else {
                                                        // Ask the user if (s)he would like to give feedback or not
                                                        DialogUtils.PullFeedbackIntermediateDialog d = DialogUtils.PullFeedbackIntermediateDialog.newInstance(activity.getResources().getString(ch.uzh.supersede.feedbacklibrary.R.string.supersede_feedbacklibrary_pull_feedback_question_string), jsonString, shuffleIds.get(i), baseURL, language);
                                                        d.show(activity.getFragmentManager(), "feedbackPopupDialog");
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        Log.e(TAG, "Failed to retrieve the configuration. Response code == " + response.code());
                                    }
                                }
                            });
                        } else {
                            Log.e(TAG, "Failed to retrieve the configuration. Call<OrchestratorConfigurationItem> result is null");
                        }
                    } else {
                        Log.e(TAG, "The server is not up and running. Response code == " + response.code());
                    }
                }
            });
        } else {
            Log.e(TAG, "Failed to ping the server. Call<ResponseBody> checkUpAndRunning result is null");
        }
    }

    /**
     * This method opens the FeedbackActivityConstants from the feedback library in case if a PULL feedback is triggered with a specific PULL configuration.
     *
     * @param baseURL                the base URL
     * @param activity               the activity in which the method is called
     * @param applicationId          the application id
     * @param language               the language
     * @param pullConfigurationId    the pull configuration id
     * @param intermediateDialogText the text to show in the intermediate dialog
     */
    public static void triggerSpecificPullFeedback(@NonNull final String baseURL, @NonNull final Activity activity, final long applicationId, final @NonNull String language,
                                                   final long pullConfigurationId, final @NonNull String intermediateDialogText) {
        Retrofit rtf = new Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).build();
        feedbackAPI fbAPI = rtf.create(feedbackAPI.class);
        final Call<ResponseBody> checkUpAndRunning = fbAPI.pingOrchestrator();

        if (checkUpAndRunning != null) {
            checkUpAndRunning.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e(TAG, "Failed to ping the server. onFailure method called", t);
                }

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        Retrofit rtf = new Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).build();
                        feedbackAPI fbAPI = rtf.create(feedbackAPI.class);
                        Call<OrchestratorConfigurationItem> result = fbAPI.getConfiguration(language, applicationId);

                        // Asynchronous call
                        if (result != null) {
                            result.enqueue(new Callback<OrchestratorConfigurationItem>() {
                                @Override
                                public void onFailure(Call<OrchestratorConfigurationItem> call, Throwable t) {
                                    Log.e(TAG, "Failed to retrieve the configuration. onFailure method called", t);
                                    DialogUtils.showInformationDialog(activity, new String[]{activity.getResources().getString(R.string.supersede_feedbacklibrary_feedback_application_unavailable_text)}, true);
                                }

                                @Override
                                public void onResponse(Call<OrchestratorConfigurationItem> call, Response<OrchestratorConfigurationItem> response) {
                                    if (response.code() == 200) {
                                        Log.i(TAG, "Configuration successfully retrieved");
                                        OrchestratorConfigurationItem configuration = response.body();
                                        if (configuration != null) {
                                            List<ConfigurationItem> configurationItems = configuration.getConfigurationItems();
                                            long[] selectedPullConfigurationIndex = {-1L};
                                            ConfigurationItem selectedConfigurationItem = null;
                                            for (ConfigurationItem configurationItem : configurationItems) {
                                                if (configurationItem.getType().equals("PULL") && configurationItem.getId() == pullConfigurationId) {
                                                    selectedPullConfigurationIndex[0] = configurationItem.getId();
                                                    selectedConfigurationItem = configurationItem;
                                                    break;
                                                }
                                            }

                                            if (selectedPullConfigurationIndex[0] != -1 && selectedConfigurationItem != null) {
                                                // If no "showIntermediateDialog" is provided, show it
                                                boolean showIntermediateDialog = true;
                                                for (Map<String, Object> parameter : selectedConfigurationItem.getGeneralConfigurationItem().getParameters()) {
                                                    String key = (String) parameter.get("key");
                                                    // Intermediate dialog
                                                    if (key.equals("showIntermediateDialog")) {
                                                        showIntermediateDialog = (Utils.intToBool(((Double) parameter.get("value")).intValue()));
                                                    }
                                                }

                                                Intent intent = new Intent(activity, FeedbackActivity.class);
                                                GsonBuilder gsonBuilder = new GsonBuilder();
                                                gsonBuilder.setLenient();
                                                Gson gson = gsonBuilder.create();
                                                String jsonString = gson.toJson(configuration);
                                                intent.putExtra(IS_PUSH_STRING, false);
                                                intent.putExtra(JSON_CONFIGURATION_STRING, jsonString);
                                                intent.putExtra(SELECTED_PULL_CONFIGURATION_INDEX_STRING, selectedPullConfigurationIndex[0]);
                                                intent.putExtra(EXTRA_KEY_BASE_URL, baseURL);
                                                intent.putExtra(EXTRA_KEY_LANGUAGE, language);
                                                if (!showIntermediateDialog) {
                                                    // Start the feedback activity without asking the user
                                                    activity.startActivity(intent);
                                                } else {
                                                    // Ask the user if (s)he would like to give feedback or not
                                                    DialogUtils.PullFeedbackIntermediateDialog d = DialogUtils.PullFeedbackIntermediateDialog.newInstance(intermediateDialogText, jsonString, selectedPullConfigurationIndex[0], baseURL, language);
                                                    d.show(activity.getFragmentManager(), "feedbackPopupDialog");
                                                }
                                            } else {
                                                DialogUtils.showInformationDialog(activity, new String[]{activity.getResources().getString(R.string.supersede_feedbacklibrary_feedback_application_unavailable_text)}, true);
                                            }
                                        }
                                    } else {
                                        Log.e(TAG, "Failed to retrieve the configuration. Response code == " + response.code());
                                    }
                                }
                            });
                        } else {
                            Log.e(TAG, "Failed to retrieve the configuration. Call<OrchestratorConfigurationItem> result is null");
                        }
                    } else {
                        Log.e(TAG, "The server is not up and running. Response code == " + response.code());
                    }
                }
            });
        } else {
            Log.e(TAG, "Failed to ping the server. Call<ResponseBody> checkUpAndRunning result is null");
        }
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
