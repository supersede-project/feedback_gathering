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
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.activities.FeedbackActivity;
import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;
import ch.uzh.supersede.feedbacklibrary.services.FeedbackService;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.FeedbackActivityConstants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.ScreenshotConstants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.ScreenshotConstants.TAG;

/**
 * Class with various helper methods
 */
public class Utils {

    private Utils() {
    }


    public static void wipeImages(final Context context) {
        FeedbackDatabase.getInstance(context).deleteData(IMAGE_DATA_DB_KEY);
        FeedbackDatabase.getInstance(context).deleteData(IMAGE_ANNOTATED_DATA_DB_KEY);
    }

    public static void storeScreenshotToDatabase(final Activity activity) {
        View rootView = activity.getWindow().getDecorView().getRootView();
        rootView.setDrawingCacheEnabled(true);
        Bitmap imageBitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);
        storeImageToDatabase(activity,imageBitmap);
    }

    public static void storeImageToDatabase(final Activity activity, Bitmap bitmap) {
        storeBitmap(activity.getApplicationContext(),bitmap,IMAGE_DATA_DB_KEY);    }

    public static void storeAnnotatedImageToDatabase(final Activity activity, Bitmap bitmap) {
        storeBitmap(activity.getApplicationContext(),bitmap,IMAGE_ANNOTATED_DATA_DB_KEY);
    }

    private static void storeBitmap(Context context, Bitmap bitmap, String dataKey){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        if (bitmap != null) {
            FeedbackDatabase.getInstance(context).writeByte(dataKey, stream.toByteArray());
        }
    }

    public static Bitmap loadImageFromDatabase(final Context context) {
        return loadImageFromDatabase(context,IMAGE_DATA_DB_KEY);
    }

    public static Bitmap loadAnnotatedImageFromDatabase(final Context context) {
        return loadImageFromDatabase(context,IMAGE_ANNOTATED_DATA_DB_KEY);
    }

    private static Bitmap loadImageFromDatabase(final Context context, String dataKey) {
        byte[] imageAsByte = FeedbackDatabase.getInstance(context).readBytes(dataKey);
        if (imageAsByte==null){
            return null;
        }
        return BitmapFactory.decodeByteArray(imageAsByte, 0, imageAsByte.length);
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
    public static boolean checkSinglePermission(@NonNull final Context context, final int requestCode, @NonNull final String permission, final String dialogTitle, final String dialogMessage, final
    boolean showRequestPermissionRationale) {
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
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
     * @param grantResults  the granted results
     * @param activity      the activity from where the method is called
     * @param permission    the requested permission
     * @param dialogTitle   the dialog title for the rationale
     * @param dialogMessage the dialog message for the rationale
     */
    public static void onRequestPermissionsResultCase(final int requestCode, @NonNull int[] grantResults, @NonNull final Activity activity, @NonNull final String permission, final int dialogTitle,
                                                      final int dialogMessage, final long applicationId, @NonNull final String baseURL, @NonNull final String language) {
        final Intent intent = new Intent(activity, FeedbackActivity.class);
        intent.putExtra(EXTRA_KEY_APPLICATION_ID, applicationId);
        intent.putExtra(EXTRA_KEY_BASE_URL, baseURL);
        intent.putExtra(EXTRA_KEY_LANGUAGE, language);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission was already granted. Taking a screenshot of the current screen automatically and open the FeedbackActivityConstants from the feedback library
            startActivity(activity, intent, true);
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
                        startActivity(activity, intent, false);
                    }
                });
                alertBuilder.setCancelable(false);
                alertBuilder.show();
            } else {
                // Open the FeedbackActivityConstants from the feedback library without automatically taking a screenshot
                startActivity(activity, intent, false);
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
            Log.e(TAG, e.getMessage(), e);
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
            Log.e(TAG, "Failed to write the bitmap to the file.", e);
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
            Log.e(TAG, "Failed to write the bitmap to the internal storage.", e);
        }
        return directory.getAbsolutePath();
    }

    /**
     * This method scales the bitmap according to a maximum width and height keeping the aspect ratio.
     */
    public static Bitmap scaleBitmap(Bitmap bitmap, int newWidth, int newHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (width > height) {
            // Landscape
            float ratio = (float) width / newWidth;
            width = newWidth;
            height = (int) (height / ratio);
        } else if (height > width) {
            // Portrait
            float ratio = (float) height / newHeight;
            height = newHeight;
            width = (int) (width / ratio);
        } else {
            // Square
            height = newHeight;
            width = newWidth;
        }

        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    private static void startActivity(@NonNull final Activity activity, @NonNull final Intent intent, final boolean isCapturingScreenshot) {
        FeedbackService.getInstance().pingOrchestrator();
        if (isCapturingScreenshot) {
            storeScreenshotToDatabase(activity);
        }
        activity.startActivity(intent);
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
