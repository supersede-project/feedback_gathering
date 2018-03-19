package ch.uzh.supersede.feedbacklibrary.views;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

public class TextAnnotationImageView extends TextAnnotationView {
    private ImageView mainView;
    private int imageResourceId;

    public TextAnnotationImageView(Context context) {
        super(context);
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResource(int resId) {
        imageResourceId = resId;
        mainView.setImageResource(resId);
    }

    @Override
    public View getMainView() {
        if (mainView == null) {
            mainView = new ImageView(getContext());
            mainView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        return mainView;
    }
}
