package com.example.matthias.feedbacklibrary.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
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
import android.view.View;

import com.example.matthias.feedbacklibrary.API.feedbackAPI;
import com.example.matthias.feedbacklibrary.FeedbackActivity;
import com.example.matthias.feedbacklibrary.R;
import com.example.matthias.feedbacklibrary.configurations.ConfigurationItem;
import com.example.matthias.feedbacklibrary.configurations.OrchestratorConfigurationItem;
import com.google.gson.Gson;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Class with various helper methods
 */
public class Utils {
    // Image annotation
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

    /**
     * @param input the input value
     * @return the integer value corresponding to the input value
     */
    public static int boolToInt(boolean input) {
        return input ? 1 : 0;
    }

    /**
     * This method takes a screenshot of the current screen and saves it in the 'Screenshots' folder.
     *
     * @return the path to the recently taken screenshot image
     */
    @NonNull
    public static String captureScreenshot(final Activity activity) {
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
            e.printStackTrace();
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
            e.printStackTrace();
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
     * @param permission    the needed permission
     * @param dialogTitle   the dialog title
     * @param dialogMessage the dialog message
     * @return true if permission is granted, false otherwise
     */
    public static boolean checkSinglePermission(@NonNull final Context context, final int requestCode, @NonNull final String permission, @NonNull final String dialogTitle, @NonNull final String dialogMessage) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
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
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    /**
     * This method creates a temporary file in the cache directory.
     *
     * @param context the context of the application
     * @param prefix  the prefix, e.g., crop
     * @param suffix  the suffix, e.g., .jpg
     * @return the created file, null if an exception occurred
     */
    @Nullable
    public static File createTempChacheFile(Context context, String prefix, String suffix) {
        try {
            return File.createTempFile(prefix, suffix, context.getCacheDir());
        } catch (IOException e) {
            // Error while creating file
        }
        return null;
    }

    /**
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
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method reads a specific file from the assets resource folder and returns it as a string.
     *
     * @param fileName     the file to read from
     * @param assetManager the asset manager
     * @return the file content as a string, the empty string if an error occurred
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
     * This method deletes the file or directory at the specific path if it exists.
     *
     * @param path the path of the file to delete
     * @return true if and only if the file or directory is successfully deleted, false otherwise
     */
    public static boolean removeDeleteFileFromInternalStorage(String path) {
        File toDelete = new File(path);
        return toDelete.exists() && toDelete.delete();
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
            e.printStackTrace();
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
        ContextWrapper cw = new ContextWrapper(applicationContext);
        File directory = cw.getDir(dirName, mode);
        File myPath = new File(directory, imageName);

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(myPath);
            bitmapImage.compress(format, quality, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
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
        ContextWrapper cw = new ContextWrapper(applicationContext);
        File directory = cw.getDir(dirName, mode);
        File myPath = new File(directory, fileName);
        try {
            FileWriter out = new FileWriter(myPath);
            out.write(str);
            out.flush();
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
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

    /**
     * This method triggers a pull feedback based on their respective likelihoods
     * and can be called anywhere in the main application where a pull feedback potentially needs to be triggered.
     *
     * @param activity the activity from where the feedback activity is launched
     */
    public static void triggerPotentialPullFeedback(final Activity activity) {
        Retrofit rtf = new Retrofit.Builder().baseUrl(feedbackAPI.endpoint).addConverterFactory(GsonConverterFactory.create()).build();
        feedbackAPI fbAPI = rtf.create(feedbackAPI.class);
        Call<OrchestratorConfigurationItem> result = fbAPI.getConfiguration("en", 8);

        // Asynchronous call
        if (result != null) {
            result.enqueue(new Callback<OrchestratorConfigurationItem>() {
                @Override
                public void onFailure(Call<OrchestratorConfigurationItem> call, Throwable t) {
                }

                @Override
                public void onResponse(Call<OrchestratorConfigurationItem> call, Response<OrchestratorConfigurationItem> response) {
                    OrchestratorConfigurationItem configuration = response.body();
                    if (configuration != null) {
                        List<ConfigurationItem> configurationItems = configuration.getConfigurationItems();
                        List<Long> shuffleIds = new ArrayList<>();
                        Map<Long, List<Map<String, Object>>> idParameters = new HashMap<>();
                        for (ConfigurationItem configurationItem : configurationItems) {
                            if (configurationItem.getType().equals("PULL")) {
                                shuffleIds.add(configuration.getId());
                                idParameters.put(configuration.getId(), configurationItem.getGeneralConfigurationItem().getParameters());
                            }
                        }

                        Random rnd = new Random(System.nanoTime());
                        Collections.shuffle(shuffleIds, rnd);
                        for (int i = 0; i < shuffleIds.size(); ++i) {
                            double likelihood = -1;
                            boolean showIntermediateDialog = true;
                            for (Map<String, Object> parameter : idParameters.get(shuffleIds.get(i))) {
                                String key = (String) parameter.get("key");
                                // Likelihood
                                if (key.equals("likelihood")) {
                                    likelihood = (((Double) parameter.get("value")).floatValue());
                                }
                                // Intermediate dialog
                                if (key.equals("showIntermediateDialog")) {
                                    showIntermediateDialog = (Utils.intToBool(((Double) parameter.get("value")).intValue()));
                                }
                            }

                            if (!(rnd.nextDouble() > likelihood)) {
                                Intent intent = new Intent(activity, FeedbackActivity.class);

                                String jsonString = new Gson().toJson(configuration);
                                intent.putExtra(FeedbackActivity.JSON_CONFIGURATION_STRING, jsonString);
                                intent.putExtra(FeedbackActivity.IS_PUSH_STRING, false);
                                intent.putExtra(FeedbackActivity.SELECTED_PULL_CONFIGURATION_INDEX_STRING, shuffleIds.get(i));
                                if (!showIntermediateDialog) {
                                    // Start the feedback activity without asking the user
                                    activity.startActivity(intent);
                                } else {
                                    // Ask the user if (s)he would like to give feedback or not
                                    DialogUtils.PullFeedbackIntermediateDialog d = DialogUtils.PullFeedbackIntermediateDialog.newInstance(activity.getResources().getString(R.string.supersede_feedbacklibrary_pull_feedback_question_string), jsonString, shuffleIds.get(i));
                                    d.show(activity.getFragmentManager(), "feedbackPopupDialog");
                                }
                            }
                        }
                    }
                }
            });
        } else {
            // Should never happen!
        }
    }
}
