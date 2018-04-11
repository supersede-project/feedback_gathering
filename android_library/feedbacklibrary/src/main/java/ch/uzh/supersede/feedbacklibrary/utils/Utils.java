package ch.uzh.supersede.feedbacklibrary.utils;

import android.Manifest;
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

    public static void storeScreenshotToIntent(final Activity activity, Intent intent) {
        View rootView = activity.getWindow().getDecorView().getRootView();
        rootView.setDrawingCacheEnabled(true);
        Bitmap imageBitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        intent.putExtra(EXTRA_KEY_CACHED_SCREENSHOT,stream.toByteArray());
    }

    public static void storeImageToDatabase(final Activity activity, Bitmap bitmap) {
        storeBitmap(activity.getApplicationContext(),bitmap,IMAGE_DATA_DB_KEY);    }

    public static void storeAnnotatedImageToDatabase(final Activity activity, Bitmap bitmap) {
        storeBitmap(activity.getApplicationContext(),bitmap,IMAGE_ANNOTATED_DATA_DB_KEY);
    }

    private static void storeBitmap(Context context, Bitmap bitmap, String dataKey){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        FeedbackDatabase.getInstance(context).writeByte(dataKey, stream.toByteArray());
    }

    public static void persistScreenshot(Context context, byte[] data){
        if (data != null){
            FeedbackDatabase.getInstance(context).writeByte(IMAGE_DATA_DB_KEY, data);
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


    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
