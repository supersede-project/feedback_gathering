package ch.uzh.supersede.feedbacklibrary.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ch.uzh.supersede.feedbacklibrary.R;

import static android.view.Gravity.*;

/**
 * Sticker view
 */
public abstract class StickerAnnotationView extends AbstractAnnotationView {

    private ImageView scaleImageView;
    private ImageView rotateImageView;

    private double centerX;
    private double centerY;

    public StickerAnnotationView(Context context) {
        super(context);
    }

    public StickerAnnotationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StickerAnnotationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ImageView getScaleImageView() {
        return scaleImageView;
    }

    public ImageView getRotateImageView() {
        return rotateImageView;
    }

    public double getCenterX() {
        return centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    @Override
    public void setControlItemsInvisible(boolean isControlItemsInvisible) {
        super.setControlItemsInvisible(isControlItemsInvisible);
        int visibility = isControlItemsInvisible ? View.INVISIBLE : View.VISIBLE;
        getRotateImageView().setVisibility(visibility);
        getScaleImageView().setVisibility(visibility);
    }


    @Override
    protected void init(Context context) {
        super.init(context);

        this.scaleImageView = new ImageView(context);
        this.rotateImageView = new ImageView(context);

        getScaleImageView().setImageResource(R.drawable.icon_zoominout);
        getRotateImageView().setImageResource(R.drawable.ic_rotate_90_degrees_ccw_black_48dp);

        setTag("DraggableViewGroup");
        getScaleImageView().setTag("scaleImageView");
        getRotateImageView().setTag("rotateImageView");

        int size = convertDpToPixel(BUTTON_SIZE_DP, getContext());

        // Scale image view
        LayoutParams scaleImageParams = new LayoutParams(size, size);
        scaleImageParams.gravity = BOTTOM | END;

        // Flip image view
        LayoutParams rotateImageParams = new LayoutParams(size, size);
        rotateImageParams.gravity = BOTTOM | START;

        addView(getScaleImageView(), scaleImageParams);
        addView(getRotateImageView(), rotateImageParams);
        setControlItemsInvisible(false);

        setOnTouchListener(getOnTouchListener());
        getScaleImageView().setOnTouchListener(getOnTouchListener());

        getDeleteImageView().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StickerAnnotationView.this.getParent() != null) {
                    ViewGroup myCanvas = ((ViewGroup) StickerAnnotationView.this.getParent());
                    myCanvas.removeView(StickerAnnotationView.this);
                }
            }
        });
        getRotateImageView().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                View mainView = getMainView();
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

    @Override
    protected void initOnTouchListener() {
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                Log.i("OnTouchListener.onTouch", view.getTag().toString());
                if (view.getTag().equals("DraggableViewGroup")) {
                    initDraggableViewGroup(event);
                } else if (view.getTag().equals("scaleImageView")) {
                    initScaleImageView(event);
                }
                return true;
            }
        });
    }

    private void initScaleImageView(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                setScaleOrgX(event.getRawX());
                setScaleOrgY(event.getRawY());
                this.centerX = getX() + ((View) getParent()).getX() + (float) getWidth() / 2;

                int result = 0;
                int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    result = getResources().getDimensionPixelSize(resourceId);
                }
                double statusBarHeight = result;
                this.centerY = getY() + ((View) getParent()).getY() + statusBarHeight + (float) getHeight() / 2;

                break;
            case MotionEvent.ACTION_MOVE:
                double angleDiff = Math.abs(Math.atan2(event.getRawY() - getScaleOrgY(), event.getRawX() - getScaleOrgX()) - Math.atan2(getScaleOrgY() - getCenterY(), getScaleOrgX() - getCenterX())
                ) * 180 / Math.PI;
                double length1 = getLength(getCenterX(), getCenterY(), getScaleOrgX(), getScaleOrgY());
                double length2 = getLength(getCenterX(), getCenterY(), event.getRawX(), event.getRawY());

                int size = convertDpToPixel(SELF_SIZE_DP, getContext());
                if (length2 > length1 && (angleDiff < 25 || Math.abs(angleDiff - 180) < 25)) {
                    // Scale up
                    double offsetX = Math.abs(event.getRawX() - getScaleOrgX());
                    double offsetY = Math.abs(event.getRawY() - getScaleOrgY());
                    double offset = Math.max(offsetX, offsetY);
                    offset = Math.round(offset);
                    getLayoutParams().width += offset;
                    getLayoutParams().height += offset;
                } else if (length2 < length1 && (angleDiff < 25 || Math.abs(angleDiff - 180) < 25) && getLayoutParams().width > size / 2 && getLayoutParams().height > size / 2) {
                    // Scale down
                    double offsetX = Math.abs(event.getRawX() - getScaleOrgX());
                    double offsetY = Math.abs(event.getRawY() - getScaleOrgY());
                    double offset = Math.max(offsetX, offsetY);
                    offset = Math.round(offset);
                    getLayoutParams().width -= offset;
                    getLayoutParams().height -= offset;
                }

                // Rotate
                double angle = Math.atan2(event.getRawY() - getCenterY(), event.getRawX() - getCenterX()) * 180 / Math.PI;
                setRotation((float) angle - 45F);

                setScaleOrgX(event.getRawX());
                setScaleOrgY(event.getRawY());

                postInvalidate();
                requestLayout();
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
        }
    }
}
