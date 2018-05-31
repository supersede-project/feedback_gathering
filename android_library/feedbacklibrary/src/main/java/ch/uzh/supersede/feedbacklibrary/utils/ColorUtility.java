package ch.uzh.supersede.feedbacklibrary.utils;

import android.graphics.Color;
import android.graphics.drawable.*;
import android.view.View;

public class ColorUtility {

    private ColorUtility() {
    }

    public static int percentToColor(float percent) {
        return Color.rgb(255 - NumberUtility.multiply(255, percent), NumberUtility.multiply(255, percent), 0);
    }

    public static boolean isDark(int color) {
        return ((Color.red(color) + Color.green(color) + Color.blue(color)) / 3d) < 122;
    }


    /**
     * Calculates the differences between Color a and b
     * Returns a value between 0 (identical) and 1 (complete different colors)
     *
     * @param a
     * @param b
     * @return
     */
    public static float getColorDifferences(int a, int b) {
        return 1f / 765f * (
                Math.abs(Color.red(a) - Color.red(b)) +
                        Math.abs(Color.green(a) - Color.green(b)) +
                        Math.abs(Color.blue(a) - Color.blue(b)));

    }

    public static int adjustColorToBackground(int backgroundColor, int color, double differenceThreshold) {
        int newColor = color;
        while (getColorDifferences(backgroundColor,newColor)<differenceThreshold){
            newColor = adjustColorToBrightness(backgroundColor,newColor);
        }
        return newColor;
    }

    private static int adjustColorToBrightness(int backgroundColor, int color) {
        int step;
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        if (isDark(backgroundColor)){
            step = 10;
        }else{
            step = -10;
        }
        red=((red+step)>255?255:(red+step)<0?0:red+step);
        green=((green+step)>255?255:(green+step)<0?0:green+step);
        blue=((blue+step)>255?255:(blue+step)<0?0:blue+step);
        return Color.rgb(red,green,blue);
    }

    public static String toHexString(int color) {
        String hexColour = Integer.toHexString(color & 0xffffff);
        if (hexColour.length() < 6) {
            hexColour = "000000".substring(0, 6 - hexColour.length()) + hexColour;
        }
        return hexColour;
    }
    public static int fromHexString(String color) {
        color = color.replace("#","");
        return Integer.valueOf(color, 16);
    }

    public static int getBackgroundColorOfView(View v){
        int color = Color.TRANSPARENT;
        Drawable background = v.getBackground();
        if (background instanceof ColorDrawable)
            color = ((ColorDrawable) background).getColor();
        return color;
    }
}
