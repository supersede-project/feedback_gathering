package ch.uzh.supersede.feedbacklibrary.components.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class BorderView extends View {
    private Rect rect;
    private Paint paint;

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

        // Draw sticker rect
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
        if (rect == null) {
            rect = new Rect();
        }
        rect.left = getLeft() - params.leftMargin;
        rect.top = getTop() - params.topMargin;
        rect.right = getRight() - params.rightMargin;
        rect.bottom = getBottom() - params.bottomMargin;
        if (paint == null) {
            paint = new Paint();
        }
        paint.setStrokeWidth(6);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(rect, paint);

    }
}
