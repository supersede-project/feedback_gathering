package com.example.matthias.feedbacklibrary.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.matthias.feedbacklibrary.R;

/**
 * Sticker view
 */
public abstract class StickerAnnotationView extends FrameLayout {
    private final static int BUTTON_SIZE_DP = 30;
    private final static int SELF_SIZE_DP = 100;
    // Sticker border
    private BorderView borderView;
    private ImageView scaleImageView;
    private ImageView deleteImageView;
    private ImageView rotateImageView;
    private ImageView checkImageView;
    private boolean controlItemsHidden;
    // Scaling
    private float scaleOrgX = -1F;
    private float scaleOrgY = -1F;
    // Sticker rotation
    private float rotateOrgX = -1F;
    private float rotateOrgY = -1F;
    private float rotateNewX = -1F;
    private float rotateNewY = -1F;
    // Movement
    private float moveOrgX = -1F;
    private float moveOrgY = -1F;
    private double centerX;
    private double centerY;
    // Touch listener
    private OnTouchListener onTouchListener;

    public StickerAnnotationView(Context context) {
        super(context);
        init(context);
    }

    public StickerAnnotationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StickerAnnotationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private int convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }

    private double getLength(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));
    }

    protected abstract View getMainView();

    private float[] getRelativePos(float absX, float absY) {
        return new float[]{
                absX - ((View) this.getParent()).getX(),
                absY - ((View) this.getParent()).getY()
        };
    }

    private void init(Context context) {
        initOnTouchListener();

        borderView = new BorderView(context);
        scaleImageView = new ImageView(context);
        deleteImageView = new ImageView(context);
        rotateImageView = new ImageView(context);
        checkImageView = new ImageView(context);

        scaleImageView.setImageResource(R.drawable.icon_zoominout);
        deleteImageView.setImageResource(R.drawable.ic_delete_black_48dp);
        rotateImageView.setImageResource(R.drawable.ic_rotate_90_degrees_ccw_black_48dp);
        checkImageView.setImageResource(R.drawable.ic_check_circle_black_48dp);

        setTag("DraggableViewGroup");
        borderView.setTag("borderView");
        scaleImageView.setTag("scaleImageView");
        deleteImageView.setTag("deleteImageView");
        rotateImageView.setTag("rotateImageView");
        checkImageView.setTag("checkImageView");

        int margin = convertDpToPixel(BUTTON_SIZE_DP, getContext()) / 2;
        int size = convertDpToPixel(SELF_SIZE_DP, getContext());

        // Sticker view
        LayoutParams thisParams = new LayoutParams(size, size);
        thisParams.gravity = Gravity.CENTER;
        // Main view
        LayoutParams ivMainParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ivMainParams.setMargins(margin, margin, margin, margin);
        // Border view
        LayoutParams ivBorderParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ivBorderParams.setMargins(margin, margin, margin, margin);
        // Scale image view
        LayoutParams ivScaleParams = new LayoutParams(convertDpToPixel(BUTTON_SIZE_DP, getContext()), convertDpToPixel(BUTTON_SIZE_DP, getContext()));
        ivScaleParams.gravity = Gravity.BOTTOM | Gravity.END;
        // Delete image view
        LayoutParams ivDeleteParams = new LayoutParams(convertDpToPixel(BUTTON_SIZE_DP, getContext()), convertDpToPixel(BUTTON_SIZE_DP, getContext()));
        ivDeleteParams.gravity = Gravity.TOP | Gravity.START;
        // Flip image view
        LayoutParams ivFlipParams = new LayoutParams(convertDpToPixel(BUTTON_SIZE_DP, getContext()), convertDpToPixel(BUTTON_SIZE_DP, getContext()));
        ivFlipParams.gravity = Gravity.BOTTOM | Gravity.START;
        // Check image view
        LayoutParams ivCheckParams = new LayoutParams(convertDpToPixel(BUTTON_SIZE_DP, getContext()), convertDpToPixel(BUTTON_SIZE_DP, getContext()));
        ivCheckParams.gravity = Gravity.TOP | Gravity.END;

        setLayoutParams(thisParams);
        addView(getMainView(), ivMainParams);
        addView(borderView, ivBorderParams);
        addView(scaleImageView, ivScaleParams);
        addView(deleteImageView, ivDeleteParams);
        addView(rotateImageView, ivFlipParams);
        addView(checkImageView, ivCheckParams);
        setControlItemsHidden(false);

        setOnTouchListener(onTouchListener);
        scaleImageView.setOnTouchListener(onTouchListener);
        deleteImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StickerAnnotationView.this.getParent() != null) {
                    ViewGroup myCanvas = ((ViewGroup) StickerAnnotationView.this.getParent());
                    myCanvas.removeView(StickerAnnotationView.this);
                }
            }
        });
        rotateImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                View mainView = getMainView();
                float currentRotation = mainView.getRotation();
                if (!(currentRotation < -270F) && !(currentRotation > -270F)) {
                    mainView.setRotation(0F);
                } else {
                    mainView.setRotation(currentRotation - 90F);
                }
                mainView.invalidate();
                requestLayout();
            }
        });
        checkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setControlItemsHidden(true);
            }
        });
    }

    /**
     * This method initializes the onTouchListener.
     */
    private void initOnTouchListener() {
        onTouchListener = new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (view.getTag().equals("DraggableViewGroup")) {
                    setControlItemsHidden(false);
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            moveOrgX = event.getRawX();
                            moveOrgY = event.getRawY();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            float offsetX = event.getRawX() - moveOrgX;
                            float offsetY = event.getRawY() - moveOrgY;
                            StickerAnnotationView.this.setX(StickerAnnotationView.this.getX() + offsetX);
                            StickerAnnotationView.this.setY(StickerAnnotationView.this.getY() + offsetY);
                            moveOrgX = event.getRawX();
                            moveOrgY = event.getRawY();
                            break;
                        case MotionEvent.ACTION_UP:
                            break;
                    }
                } else if (view.getTag().equals("scaleImageView")) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            scaleOrgX = event.getRawX();
                            scaleOrgY = event.getRawY();

                            rotateOrgX = event.getRawX();
                            rotateOrgY = event.getRawY();

                            centerX = StickerAnnotationView.this.getX() + ((View) StickerAnnotationView.this.getParent()).getX() + (float) StickerAnnotationView.this.getWidth() / 2;

                            int result = 0;
                            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
                            if (resourceId > 0) {
                                result = getResources().getDimensionPixelSize(resourceId);
                            }
                            double statusBarHeight = result;
                            centerY = StickerAnnotationView.this.getY() + ((View) StickerAnnotationView.this.getParent()).getY() + statusBarHeight + (float) StickerAnnotationView.this.getHeight() / 2;

                            break;
                        case MotionEvent.ACTION_MOVE:
                            rotateNewX = event.getRawX();
                            rotateNewY = event.getRawY();

                            double angle_diff = Math.abs(Math.atan2(event.getRawY() - scaleOrgY, event.getRawX() - scaleOrgX) - Math.atan2(scaleOrgY - centerY, scaleOrgX - centerX)) * 180 / Math.PI;
                            double length1 = getLength(centerX, centerY, scaleOrgX, scaleOrgY);
                            double length2 = getLength(centerX, centerY, event.getRawX(), event.getRawY());

                            int size = convertDpToPixel(SELF_SIZE_DP, getContext());
                            if (length2 > length1 && (angle_diff < 25 || Math.abs(angle_diff - 180) < 25)) {
                                // Scale up
                                double offsetX = Math.abs(event.getRawX() - scaleOrgX);
                                double offsetY = Math.abs(event.getRawY() - scaleOrgY);
                                double offset = Math.max(offsetX, offsetY);
                                offset = Math.round(offset);
                                StickerAnnotationView.this.getLayoutParams().width += offset;
                                StickerAnnotationView.this.getLayoutParams().height += offset;
                                onScaling(true);
                            } else if (length2 < length1 && (angle_diff < 25 || Math.abs(angle_diff - 180) < 25) && StickerAnnotationView.this.getLayoutParams().width > size / 2 && StickerAnnotationView.this.getLayoutParams().height > size / 2) {
                                // Scale down
                                double offsetX = Math.abs(event.getRawX() - scaleOrgX);
                                double offsetY = Math.abs(event.getRawY() - scaleOrgY);
                                double offset = Math.max(offsetX, offsetY);
                                offset = Math.round(offset);
                                StickerAnnotationView.this.getLayoutParams().width -= offset;
                                StickerAnnotationView.this.getLayoutParams().height -= offset;
                                onScaling(false);
                            }

                            // Rotate
                            double angle = Math.atan2(event.getRawY() - centerY, event.getRawX() - centerX) * 180 / Math.PI;
                            setRotation((float) angle - 45F);
                            onRotating();

                            rotateOrgX = rotateNewX;
                            rotateOrgY = rotateNewY;

                            scaleOrgX = event.getRawX();
                            scaleOrgY = event.getRawY();

                            postInvalidate();
                            requestLayout();
                            break;
                        case MotionEvent.ACTION_UP:
                            break;
                    }
                }
                return true;
            }
        };
    }

    /**
     * Returns if the control items are hidden.
     *
     * @return true if the control items are hidden, false otherwise
     */
    public boolean isControlItemsHidden() {
        return controlItemsHidden;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    protected void onRotating() {
    }

    protected void onScaling(boolean scaleUp) {
    }

    /**
     * This method sets the value of controlItemsHidden and hides or shows the control items depending on the set value.
     *
     * @param controlItemsHidden true to hide, false to show the control items
     */
    public void setControlItemsHidden(boolean controlItemsHidden) {
        this.controlItemsHidden = controlItemsHidden;
        if (controlItemsHidden) {
            borderView.setVisibility(View.INVISIBLE);
            scaleImageView.setVisibility(View.INVISIBLE);
            deleteImageView.setVisibility(View.INVISIBLE);
            rotateImageView.setVisibility(View.INVISIBLE);
            checkImageView.setVisibility(View.INVISIBLE);
        } else {
            borderView.setVisibility(View.VISIBLE);
            scaleImageView.setVisibility(View.VISIBLE);
            deleteImageView.setVisibility(View.VISIBLE);
            rotateImageView.setVisibility(View.VISIBLE);
            checkImageView.setVisibility(View.VISIBLE);
        }
    }
}
