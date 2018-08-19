package ch.uzh.supersede.feedbacklibrary.components.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import ch.uzh.supersede.feedbacklibrary.R;

import static android.view.Gravity.*;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.ViewsConstants.*;

public final class StickerAnnotationImageView extends AbstractAnnotationView {
    private float scaleOrgX;
    private float scaleOrgY;
    private double centerX;
    private double centerY;
    private int imageResourceId;

    private ImageView stickerImageView;
    private ImageView scaleAnnotation;
    private ImageView rotateAnnotation;

    public StickerAnnotationImageView(Context context) {
        super(context);
    }

    public StickerAnnotationImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StickerAnnotationImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResource(int resId) {
        this.imageResourceId = resId;
        this.stickerImageView.setImageResource(resId);
    }

    public View getStickerImageView() {
        if (stickerImageView == null) {
            stickerImageView = new ImageView(getContext());
            stickerImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        return stickerImageView;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        super.onTouch(view, event);
        if (view.getTag().equals("scaleAnnotation")) {
            execScaleStickerImageView(event);
        }
        return true;
    }

    @Override
    public void setViewsVisible(boolean isVisible) {
        super.setViewsVisible(isVisible);
        int visibility = isVisible ? VISIBLE : INVISIBLE;
        rotateAnnotation.setVisibility(visibility);
        scaleAnnotation.setVisibility(visibility);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        scaleAnnotation = new ImageView(context);
        rotateAnnotation = new ImageView(context);

        scaleAnnotation.setImageResource(R.drawable.icon_zoominout);
        rotateAnnotation.setImageResource(R.drawable.ic_rotate_90_degrees_ccw_black_48dp);

        scaleAnnotation.setTag("scaleAnnotation");
        rotateAnnotation.setTag("rotateAnnotation");

        int buttonSize = convertDpToPixel(BUTTON_SIZE_DP, getContext());
        int margin = buttonSize / 2;

        LayoutParams mainViewParams = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
        LayoutParams scaleViewParams = new LayoutParams(buttonSize, buttonSize);
        LayoutParams rotateViewParams = new LayoutParams(buttonSize, buttonSize);

        mainViewParams.setMargins(margin, margin, margin, margin);

        scaleViewParams.gravity = BOTTOM | END;
        rotateViewParams.gravity = BOTTOM | START;

        addView(getStickerImageView(), mainViewParams);
        addView(scaleAnnotation, scaleViewParams);
        addView(rotateAnnotation, rotateViewParams);

        scaleAnnotation.setOnTouchListener(this);
        rotateAnnotation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                View mainView = getStickerImageView();
                float currentRotation = mainView.getRotation();
                if (currentRotation == -270F) {
                    mainView.setRotation(0F);
                } else {
                    mainView.setRotation(currentRotation - 90F);
                }
                mainView.invalidate();
                requestLayout();
            }
        });
    }

    private double getLength(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));
    }

    private void execScaleStickerImageView(MotionEvent event) {
        float rawX = event.getRawX();
        float rawY = event.getRawY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                scaleOrgX = rawX;
                scaleOrgY = rawY;

                double statusBarHeight = 0;
                int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    statusBarHeight = getResources().getDimensionPixelSize(resourceId);
                }

                centerX = getX() + ((View) getParent()).getX() + (float) getWidth() / 2;
                centerY = getY() + ((View) getParent()).getY() + statusBarHeight + (float) getHeight() / 2;

                break;
            case MotionEvent.ACTION_MOVE:
                double angleDiff = Math.abs(Math.atan2(rawY - scaleOrgY, rawX - scaleOrgX) - Math.atan2(scaleOrgY - centerY, scaleOrgX - centerX)) * 180 / Math.PI;
                boolean isScaleUp = getLength(centerX, centerY, rawX, rawY) > getLength(centerX, centerY, scaleOrgX, scaleOrgY);
                boolean isSufficientAngleChange = angleDiff < 25 || Math.abs(angleDiff - 180) < 25;

                int size = convertDpToPixel(SELF_SIZE_DP, getContext());
                boolean hasMinimumSize = getLayoutParams().width > size / 2 && getLayoutParams().height > size / 2;

                double offsetX = Math.abs(rawX - scaleOrgX);
                double offsetY = Math.abs(rawY - scaleOrgY);
                double offset = Math.round(Math.max(offsetX, offsetY));

                if (isScaleUp && isSufficientAngleChange) {
                    getLayoutParams().width += offset;
                    getLayoutParams().height += offset;
                } else if (!isScaleUp && isSufficientAngleChange && hasMinimumSize) {
                    getLayoutParams().width -= offset;
                    getLayoutParams().height -= offset;
                }

                // Rotate
                double angle = Math.atan2(rawY - centerY, rawX - centerX) * 180 / Math.PI;
                setRotation((float) angle - 45F);

                scaleOrgX = rawX;
                scaleOrgY = rawY;

                postInvalidate();
                requestLayout();
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
        }
    }
}
