package ch.uzh.supersede.feedbacklibrary.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import ch.uzh.supersede.feedbacklibrary.R;

import static android.view.Gravity.*;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public abstract class AbstractAnnotationView extends FrameLayout {
    public static final int BUTTON_SIZE_DP = 30;
    public static final int SELF_SIZE_DP = 100;

    // Sticker border
    private BorderView borderView;
    private ImageView deleteImageView;
    private ImageView checkImageView;

    // Scaling
    private float scaleOrgX = -1F;
    private float scaleOrgY = -1F;

    // Movement
    private float moveOrgX = -1F;
    private float moveOrgY = -1F;

    private OnTouchListener onTouchListener;

    public AbstractAnnotationView(Context context) {
        super(context);
        init(context);
    }

    public AbstractAnnotationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AbstractAnnotationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    protected abstract View getMainView();

    protected abstract void initOnTouchListener();

    public BorderView getBorderView() {
        return borderView;
    }

    public ImageView getDeleteImageView() {
        return deleteImageView;
    }

    public ImageView getCheckImageView() {
        return checkImageView;
    }

    public float getScaleOrgX() {
        return scaleOrgX;
    }

    public float getScaleOrgY() {
        return scaleOrgY;
    }

    public void setScaleOrgX(float scaleOrgX) {
        this.scaleOrgX = scaleOrgX;
    }

    public void setScaleOrgY(float scaleOrgY) {
        this.scaleOrgY = scaleOrgY;
    }

    public float getMoveOrgX() {
        return moveOrgX;
    }

    public float getMoveOrgY() {
        return moveOrgY;
    }

    public OnTouchListener getOnTouchListener() {
        return onTouchListener;
    }

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }

    public void setControlItemsInvisible(boolean isControlItemsInvisible) {
        int visibility = isControlItemsInvisible ? View.INVISIBLE : View.VISIBLE;
        getBorderView().setVisibility(visibility);
        getDeleteImageView().setVisibility(visibility);
        getCheckImageView().setVisibility(visibility);
    }

    protected double getLength(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));
    }

    protected int convertDpToPixel(float dp, Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) (dp * (metrics.densityDpi / 160f));
    }

    protected void init(Context context) {
        initOnTouchListener();

        this.borderView = new BorderView(context);
        this.deleteImageView = new ImageView(context);
        this.checkImageView = new ImageView(context);

        getDeleteImageView().setImageResource(R.drawable.ic_delete_black_48dp);
        getCheckImageView().setImageResource(R.drawable.ic_check_circle_black_48dp);

        getBorderView().setTag("borderView");
        getDeleteImageView().setTag("deleteImageView");
        getCheckImageView().setTag("checkImageView");

        int fullMargin = convertDpToPixel(BUTTON_SIZE_DP, getContext());
        int margin = fullMargin / 2;
        int size = convertDpToPixel(SELF_SIZE_DP, getContext());

        // Sticker view
        LayoutParams params = new LayoutParams(size, size);
        params.gravity = CENTER;
        // Main view
        LayoutParams mainViewParams = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
        mainViewParams.setMargins(margin, margin, margin, margin);
        // Border view
        LayoutParams borderViewParams = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
        borderViewParams.setMargins(margin, margin, margin, margin);

        // Delete image view
        LayoutParams deleteImageViewParams = new LayoutParams(fullMargin, fullMargin);
        deleteImageViewParams.gravity = TOP | START;
        // Check image view
        LayoutParams checkImageViewParams = new LayoutParams(fullMargin, fullMargin);
        checkImageViewParams.gravity = TOP | END;

        setLayoutParams(params);
        addView(getMainView(), mainViewParams);
        addView(getBorderView(), borderViewParams);
        addView(getDeleteImageView(), deleteImageViewParams);
        addView(getCheckImageView(), checkImageViewParams);

        getCheckImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setControlItemsInvisible(true);
            }
        });
    }

    protected void initDraggableViewGroup(MotionEvent event) {
        setControlItemsInvisible(false);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.moveOrgX = event.getRawX();
                this.moveOrgY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float offsetX = event.getRawX() - getMoveOrgX();
                float offsetY = event.getRawY() - getMoveOrgY();
                setX(getX() + offsetX);
                setY(getY() + offsetY);
                this.moveOrgX = event.getRawX();
                this.moveOrgY = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
        }
    }
}
