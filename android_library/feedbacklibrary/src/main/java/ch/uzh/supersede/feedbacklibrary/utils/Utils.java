package ch.uzh.supersede.feedbacklibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.UtilsConstants.*;

public class Utils {
    private Utils() {
    }

    public static String[] splitFileNameExtension(String fileName) {
        String[] filePath = new String[2];
        if (fileName == null) {
            return filePath;
        }

        filePath[0] = fileName;
        filePath[1] = "";

        int i = fileName.lastIndexOf(".");
        if (i > 0) {
            filePath[0] = fileName.substring(0, i);
            filePath[1] = fileName.substring(i + 1);
        }
        return filePath;
    }

    public static void wipeImages(final Context context) {
        FeedbackDatabase.getInstance(context).deleteData(IMAGE_DATA_DB_KEY);
        FeedbackDatabase.getInstance(context).deleteData(IMAGE_ANNOTATED_DATA_DB_KEY);
    }

    public static Bitmap storeScreenshotToDatabase(final Activity activity) {
        View rootView = activity.getWindow().getDecorView().getRootView();
        rootView.setDrawingCacheEnabled(true);
        Bitmap imageBitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);
        storeImageToDatabase(activity, imageBitmap);
        return imageBitmap;
    }

    public static Bitmap storeScreenshotToIntent(final Activity activity, Intent intent) {
        View rootView = activity.getWindow().getDecorView().getRootView();
        rootView.setDrawingCacheEnabled(true);
        Bitmap imageBitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);
        intent.putExtra(EXTRA_KEY_CACHED_SCREENSHOT, ImageUtility.imageToBytes(imageBitmap));
        return imageBitmap;
    }

    public static void storeImageToDatabase(final Activity activity, Bitmap bitmap) {
        storeBitmap(activity.getApplicationContext(), bitmap, IMAGE_DATA_DB_KEY);
    }

    public static void storeAnnotatedImageToDatabase(final Activity activity, Bitmap bitmap) {
        storeBitmap(activity.getApplicationContext(), bitmap, IMAGE_ANNOTATED_DATA_DB_KEY);
    }

    private static void storeBitmap(Context context, Bitmap bitmap, String dataKey) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        FeedbackDatabase.getInstance(context).writeByte(dataKey, stream.toByteArray());
    }

    public static void persistScreenshot(Context context, byte[] data) {
        if (data != null) {
            FeedbackDatabase.getInstance(context).writeByte(IMAGE_DATA_DB_KEY, data);
        }
    }

    public static Bitmap loadImageFromDatabase(final Context context) {
        return loadImageFromDatabase(context, IMAGE_DATA_DB_KEY);
    }

    public static Bitmap loadAnnotatedImageFromDatabase(final Context context) {
        return loadImageFromDatabase(context, IMAGE_ANNOTATED_DATA_DB_KEY);
    }

    private static Bitmap loadImageFromDatabase(final Context context, String dataKey) {
        byte[] imageAsByte = FeedbackDatabase.getInstance(context).readBytes(dataKey);
        if (imageAsByte == null) {
            return null;
        }
        return ImageUtility.bytesToImage(imageAsByte);
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
            Log.e(UTILS_TAG, "Failed to create a temporary file", e);
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
            Log.e(UTILS_TAG, "Failed to write the bitmap to the file.", e);
        }
        return false;
    }

    /**
     * This method scales the bitmap according to a maximum screenWidth and screenHeight keeping the aspect ratio.
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
}
