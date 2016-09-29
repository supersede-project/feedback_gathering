package com.example.matthias.feedbacklibrary.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class StickerAnnotationImageView extends StickerAnnotationView {
    private String ownerId;
    private ImageView mainView;
    private int imageResourceId;

    public StickerAnnotationImageView(Context context) {
        super(context);
    }

    public StickerAnnotationImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StickerAnnotationImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public Bitmap getImageBitmap() {
        return ((BitmapDrawable) mainView.getDrawable()).getBitmap();
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    @Override
    public View getMainView() {
        if (mainView == null) {
            mainView = new ImageView(getContext());
            mainView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        return mainView;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setImageBitmap(Bitmap bmp) {
        mainView.setImageBitmap(bmp);
    }

    public void setImageDrawable(Drawable drawable) {
        mainView.setImageDrawable(drawable);
    }

    public void setImageResource(int resId) {
        imageResourceId = resId;
        mainView.setImageResource(resId);
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

}
