package com.example.matthias.feedbacklibrary.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Class with various helper methods
 */
public class Utils {
    public static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final String SCREENSHOTS_DIR_NAME = "Screenshots";

    /**
     * @param input the input value
     * @return the integer value corresponding to the input value
     */
    public static int boolToInt(boolean input) {
        return input ? 1 : 0;
    }

    /**
     * Takes a screenshot of the current screen and saves it in the 'Screenshots' folder
     *
     * @return the path to the recently taken screenshot image
     */
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
     * Check READ_EXTERNAL_STORAGE permission at runtime required for Android versions 6 (API version 23) and higher
     *
     * @param context     the context
     * @param requestCode the request code to be handled in the onRequestPermissionsResult method of the calling activity
     * @return true if permission is granted, false otherwise
     */
    public static boolean checkPermission_READ_EXTERNAL_STORAGE(final Context context, final int requestCode) {
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
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, requestCode);
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
     * @param input the input value
     * @return the boolean value corresponding to the input value
     */
    public static boolean intToBool(int input) {
        if (input == 1) {
            return true;
        }
        return false;
    }

    /**
     * Dialog with a cancel button for displaying a simple string message, e.g., if a given input field is not valid
     */
    public static class DataDialog extends DialogFragment {
        public static DataDialog newInstance(ArrayList<String> messages) {
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
