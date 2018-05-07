package ch.uzh.supersede.feedbacklibrary.utils;

import android.graphics.*;
import android.support.annotation.NonNull;

import java.io.*;
import java.util.*;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.EXTRA_KEY_CACHED_SCREENSHOT;

public class ImageUtility {

    private ImageUtility() {
    }

    public static Integer[] calculateTopNColors(Bitmap bitmap, int nColors, int steps) {
        int pixelStep = 0;
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
                pixelStep++;
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

    public static boolean isDark(int color) {
        return ((Color.red(color) + Color.green(color) + Color.blue(color)) / 3d) < 122;
    }

    public final static String toHexString(int colour) throws NullPointerException {
        String hexColour = Integer.toHexString(colour & 0xffffff);
        if (hexColour.length() < 6) {
            hexColour = "000000".substring(0, 6 - hexColour.length()) + hexColour;
        }
        return hexColour;
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

    /**
     * Returns an average color-intensity, stepDensity defines the coverage of pixels
     *
     * @param bitmap
     * @param stepDensity
     * @return
     */
    public static double calculateAverageColorIntensity(Bitmap bitmap, double stepDensity) {
        double density = (stepDensity <= 0 || stepDensity > 0.5) ? 0.5 : stepDensity;
        int stepSize = (int) (1.0 / density);
        return calculateAverageColorIntensity(bitmap, stepSize);
    }

    /**
     * Returns an average color-intensity, stepSize defines the probing distance
     *
     * @param bitmap
     * @param stepSize
     * @return intensity
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
}
