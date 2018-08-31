package ch.uzh.supersede.feedbacklibrary.components.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.*;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

import ch.uzh.supersede.feedbacklibrary.R;

import static android.view.Gravity.*;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.ViewsConstants.*;

public abstract class AbstractAnnotationView extends FrameLayout implements OnTouchListener {
    // Sticker border
    private BorderView borderView;
    private ImageView deleteAnnotation;
    private ImageView saveAnnotation;

    // Movement
    private float moveOrgX;
    private float moveOrgY;

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

    protected int convertDpToPixel(float dp, Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return (int) (dp * (metrics.densityDpi / 160f));
    }

    protected void init(Context context) {
        borderView = new BorderView(context);
        deleteAnnotation = new ImageView(context);
        saveAnnotation = new ImageView(context);

        deleteAnnotation.setImageResource(R.drawable.ic_delete_black_48dp);
        saveAnnotation.setImageResource(R.drawable.ic_check_circle_black_48dp);

        setTag("draggableViewGroup");
        borderView.setTag("borderView");
        deleteAnnotation.setTag("deleteAnnotation");
        saveAnnotation.setTag("saveAnnotation");

        int buttonSize = convertDpToPixel(BUTTON_SIZE_DP, getContext());
        int margin = buttonSize / 2;
        int size = convertDpToPixel(SELF_SIZE_DP, getContext());

        LayoutParams stickerViewParams = new LayoutParams(size, size);
        LayoutParams borderViewParams = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
        LayoutParams deleteViewParams = new LayoutParams(buttonSize, buttonSize);
        LayoutParams saveViewParams = new LayoutParams(buttonSize, buttonSize);

        borderViewParams.setMargins(margin, margin, margin, margin);

        stickerViewParams.gravity = CENTER;
        deleteViewParams.gravity = TOP | START;
        saveViewParams.gravity = TOP | END;

        setLayoutParams(stickerViewParams);
        addView(borderView, borderViewParams);
        addView(deleteAnnotation, deleteViewParams);
        addView(saveAnnotation, saveViewParams);

        deleteAnnotation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                execDeleteAnnotationOnClick();
            }
        });
        saveAnnotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setViewsVisible(false);
            }
        });

        setOnTouchListener(this);
    }

    protected void execDeleteAnnotationOnClick() {
        if (getParent() != null) {
            ViewGroup myCanvas = ((ViewGroup) getParent());
            myCanvas.removeView(AbstractAnnotationView.this);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (view.getTag().equals("draggableViewGroup")) {
            execDragStickerImageView(event);
        }
        return true;
    }

    private void execDragStickerImageView(MotionEvent event) {
        setViewsVisible(true);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                moveOrgX = event.getRawX();
                moveOrgY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float offsetX = event.getRawX() - moveOrgX;
                float offsetY = event.getRawY() - moveOrgY;
                setX(getX() + offsetX);
                setY(getY() + offsetY);
                moveOrgX = event.getRawX();
                moveOrgY = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
        }
    }

    public void setViewsVisible(boolean isVisible) {
        int visibility = isVisible ? VISIBLE : INVISIBLE;
        borderView.setVisibility(visibility);
        deleteAnnotation.setVisibility(visibility);
        saveAnnotation.setVisibility(visibility);
    }
}