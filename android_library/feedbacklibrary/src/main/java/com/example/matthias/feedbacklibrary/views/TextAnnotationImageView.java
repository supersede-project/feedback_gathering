package com.example.matthias.feedbacklibrary.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class TextAnnotationImageView extends TextAnnotationView {
    private String ownerId;
    private ImageView mainView;

    public TextAnnotationImageView(Context context) {
        super(context);
    }

    public TextAnnotationImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextAnnotationImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public Bitmap getImageBitmap() {
        return ((BitmapDrawable) mainView.getDrawable()).getBitmap();
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

    public void setImageResource(int res_id) {
        mainView.setImageResource(res_id);
    }

    public void setOwnerId(String owner_id) {
        ownerId = owner_id;
    }

}
