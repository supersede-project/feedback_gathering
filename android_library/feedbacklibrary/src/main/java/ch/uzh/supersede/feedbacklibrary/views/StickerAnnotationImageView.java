package ch.uzh.supersede.feedbacklibrary.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class StickerAnnotationImageView extends StickerAnnotationView {
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

    /**
     * This method returns the id of the image resource.
     *
     * @return the image resource id
     */
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

    /**
     * This method sets the id of the image resource.
     *
     * @param resId the image resource id
     */
    public void setImageResource(int resId) {
        imageResourceId = resId;
        mainView.setImageResource(resId);
    }
}
