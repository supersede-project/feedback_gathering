package ch.uzh.supersede.feedbacklibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.*;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import java.io.*;
import java.util.*;

import ch.uzh.supersede.feedbacklibrary.database.FeedbackDatabase;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.UtilsConstants.*;

public final class ImageUtility {

    private ImageUtility() {
    }

    public static Integer[] calculateTopNColors(Bitmap bitmap, int nColors, int steps) {
        HashMap<Integer, ColorMapEntry> colorMap = new HashMap<>();
        int xStep = (int) (bitmap.getWidth() / (double) steps);
        int yStep = (int) (bitmap.getHeight() / (double) steps);
        for (int y = 0; y < bitmap.getHeight(); y = y + yStep) {
            for (int x = 0; x < bitmap.getWidth(); x = x + xStep) {
                int c = bitmap.getPixel(x, y);
                if (colorMap.containsKey(c)) {
                    colorMap.get(c).increment();
                } else {
                    colorMap.put(c, new ColorMapEntry(c));
                }
            }
        }
        ArrayList<ColorMapEntry> sortedColorMap = new ArrayList<>();
        ArrayList<Integer> finalColorList = new ArrayList<>();
        for (Map.Entry<Integer, ColorMapEntry> e : colorMap.entrySet()) {
            sortedColorMap.add(e.getValue());
        }
        Collections.sort(sortedColorMap);
        for (ColorMapEntry i : sortedColorMap) {
            finalColorList.add(i.getColor());
        }
        return CollectionUtility.subArray(Integer.class, finalColorList, 0, nColors);
    }

    /**
     * Returns an average color-intensity, stepDensity defines the coverage of pixels
     */
    public static double calculateAverageColorIntensity(Bitmap bitmap, double stepDensity) {
        double density = (stepDensity <= 0 || stepDensity > 0.5) ? 0.5 : stepDensity;
        int stepSize = (int) (1.0 / density);
        return calculateAverageColorIntensity(bitmap, stepSize);
    }

    /**
     * Returns an average color-intensity, stepSize defines the probing distance
     */
    private static double calculateAverageColorIntensity(Bitmap bitmap, int stepSize) {
        long redBucket = 0;
        long greenBucket = 0;
        long blueBucket = 0;
        double pixelCount = 0;
        int step = (stepSize <= 0 || stepSize >= (bitmap.getHeight() > bitmap.getWidth() ? bitmap.getWidth() : bitmap.getHeight())) ? 1 : stepSize;
        for (int y = 0; y < bitmap.getHeight(); y = y + step) {
            for (int x = 0; x < bitmap.getWidth(); x = x + step) {
                int c = bitmap.getPixel(x, y);

                pixelCount++;
                redBucket += Color.red(c);
                greenBucket += Color.green(c);
                blueBucket += Color.blue(c);
            }
        }
        if (pixelCount > 0) {
            double avgRed = redBucket / pixelCount;
            double avgGreen = greenBucket / pixelCount;
            double avgBlue = blueBucket / pixelCount;
            return (avgRed + avgGreen + avgBlue) / 3;
        }
        return 0;
    }

    public static byte[] imageToBytes(Bitmap bitmap) {
        if (bitmap == null) {
            return new byte[0];
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public static Bitmap bytesToImage(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static int sizeOf(Bitmap data) {
        if (data != null) {
            return data.getRowBytes() * data.getHeight();
        }
        return 0;
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
    public static File createTempCacheFile(Context context, String prefix, String suffix) {
        try {
            return File.createTempFile(prefix, suffix, context.getCacheDir());
        } catch (IOException e) {
            Log.e(UTILS_TAG, "Failed to create a temporary file", e);
        }
        return null;
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

    public static class ColorMapEntry implements Comparable {
        private int color;
        private int count;

        public ColorMapEntry(int color) {
            this.color = color;
            this.count = 1;
        }

        public void increment() {
            this.count++;
        }

        public int getColor() {
            return color;
        }

        public int getCount() {
            return count;
        }

        @Override
        public int compareTo(@NonNull Object o) {
            if (o instanceof ColorMapEntry) {
                int comparedCount = ((ColorMapEntry) o).getCount();
                return comparedCount > getCount() ? 1 : comparedCount == getCount() ? 0 : -1;
            }
            return 0;
        }
    }
}
