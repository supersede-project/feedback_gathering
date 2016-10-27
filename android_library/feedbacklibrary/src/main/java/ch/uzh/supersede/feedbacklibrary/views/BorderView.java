package ch.uzh.supersede.feedbacklibrary.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Border view
 */
public class BorderView extends View {
    private Rect border;
    private Paint borderPaint;

    public BorderView(Context context) {
        super(context);
    }

    public BorderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BorderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw sticker border
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
        if (border == null) {
            border = new Rect();
        }
        border.left = getLeft() - params.leftMargin;
        border.top = getTop() - params.topMargin;
        border.right = getRight() - params.rightMargin;
        border.bottom = getBottom() - params.bottomMargin;
        if (borderPaint == null) {
            borderPaint = new Paint();
        }
        borderPaint.setStrokeWidth(6);
        borderPaint.setColor(Color.WHITE);
        borderPaint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(border, borderPaint);

    }
}
