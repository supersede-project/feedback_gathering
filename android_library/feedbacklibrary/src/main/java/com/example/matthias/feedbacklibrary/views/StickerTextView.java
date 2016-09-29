package com.example.matthias.feedbacklibrary.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class StickerTextView extends StickerView {
    private AutoResizeTextView autoResizeTextView;

    public StickerTextView(Context context) {
        super(context);
    }

    public StickerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StickerTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public static float pixelsToSp(Context context, float px) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px / scaledDensity;
    }

    @Override
    public View getMainView() {
        if (autoResizeTextView != null)
            return autoResizeTextView;

        autoResizeTextView = new AutoResizeTextView(getContext());
        //autoResizeTextView.setTextSize(22);
        autoResizeTextView.setTextColor(Color.WHITE);
        autoResizeTextView.setGravity(Gravity.CENTER);
        autoResizeTextView.setTextSize(400);
        autoResizeTextView.setShadowLayer(4, 0, 0, Color.BLACK);
        autoResizeTextView.setMaxLines(1);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        autoResizeTextView.setLayoutParams(params);
        if (getRotateImageView() != null)
            getRotateImageView().setVisibility(View.GONE);
        return autoResizeTextView;
    }

    /**
     * This method returns the text.
     *
     * @return the text or null if autoResizeTextView is null
     */
    public String getText() {
        if (autoResizeTextView != null)
            return autoResizeTextView.getText().toString();
        return null;
    }

    @Override
    protected void onScaling(boolean scaleUp) {
        super.onScaling(scaleUp);
    }

    /**
     * This method sets the text.
     *
     * @param text the text
     */
    public void setText(String text) {
        if (autoResizeTextView != null)
            autoResizeTextView.setText(text);
    }
}
