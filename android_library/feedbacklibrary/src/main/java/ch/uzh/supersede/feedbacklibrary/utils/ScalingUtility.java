package ch.uzh.supersede.feedbacklibrary.utils;

import android.app.Activity;
import android.graphics.*;
import android.util.DisplayMetrics;
import android.widget.*;

public final class ScalingUtility {
    private static ScalingUtility instance = null;
    private static float density = 0.0f;
    private static int screenHeight = 0;
    private static int screenWidth = 0;

    public static ScalingUtility getInstance(){
        return instance;
    }

    public static ScalingUtility init(Activity activity){
        if (instance == null) {
            instance = new ScalingUtility();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            ScalingUtility.screenHeight = displayMetrics.heightPixels;
            ScalingUtility.screenWidth = displayMetrics.widthPixels;
            ScalingUtility.density = displayMetrics.density;
        }
        return instance;
    }

    private ScalingUtility() {

    }

    public float getMinTextSizeScaledForWidth(float preferredSize, float additionalPixels, double maxPercentScreenWidthUsed, String... texts){
        float minSize = preferredSize;
        for (String text : texts==null?new String[0]:texts){
            float size = getTextSizeScaledForWidth(text,preferredSize,additionalPixels,maxPercentScreenWidthUsed);
            minSize = (minSize<size?minSize:size);
        }
        return minSize;
    }

    public float getTextSizeScaledForWidth(String text, TextView textView, float additionalPixels, double maxPercentScreenWidthUsed){
        return getTextSizeScaledForWidth(text,textView.getTextSize(),additionalPixels,maxPercentScreenWidthUsed);
    }
    public float getTextSizeScaledForWidth(String text, float preferredSize, float additionalPixels, double maxPercentScreenWidthUsed){
        float size = preferredSize;
        int allowedTextWidth = NumberUtility.multiply(screenWidth,maxPercentScreenWidthUsed);
        for (;size>1;size--){
            float newSize = measureTextWidth(text,size)+additionalPixels;
            if (newSize <= allowedTextWidth){
                return size;
            }
        }
        return size;
    }

    private static float measureTextWidth(String text, float size) {
        Paint paint = new Paint();
        Rect bounds = new Rect();
        float scaledPx = size * density;

        paint.setTypeface(Typeface.DEFAULT);
        paint.setTextSize(scaledPx);

        paint.getTextBounds(text, 0, text.length(), bounds);
        return paint.measureText(text);
    }

    public float getTextSizeScaledForHeight(String text, float preferredSize, float additionalPixels , double maxPercentScreenHeightUsed){
        if (text==null){
            return 0;
        }
        float size = preferredSize;
        int allowedTextHeight = NumberUtility.multiply(screenWidth,maxPercentScreenHeightUsed);
        for (;size>1;size--){
            float newSize = measureTextHeight(text,size)+additionalPixels;
            if (newSize <= allowedTextHeight){
                return size;
            }
        }
        return size;
    }

    public void measureAndAdjustTextHeight(String text, TextView textView) {
        textView.setText(text);
        int textHeight = (int)measureTextHeight(text, textView.getTextSize());
        textView.setHeight(textHeight);
    }
    private float measureTextHeight(String text, float size) {
        if (text==null){
            return 0;
        }
        float scaledPx = size * density;
        int lines = text.split("\n").length;
        return scaledPx*lines;
    }

    public void updateEditText(EditText editText) {
        editText.measure(0,0);
        float textSize = (1f/(density/(screenHeight/1200f)))*getTextSizeScaledForHeight(editText.getText().toString(),editText.getTextSize(),0,1f/screenHeight*editText.getMeasuredHeight());
        editText.setTextSize(textSize);
    }

    public void updateButtonText(Button button) {
        button.measure(0,0);
        float textSize = (1f/density)*getTextSizeScaledForHeight(button.getText().toString(),button.getTextSize(),0,1f/screenHeight*button.getMeasuredHeight());
        button.setTextSize(textSize);
    }
}
