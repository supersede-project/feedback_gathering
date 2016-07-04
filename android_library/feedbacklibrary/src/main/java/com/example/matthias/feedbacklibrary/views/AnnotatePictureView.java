package com.example.matthias.feedbacklibrary.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Annotate picture view
 */
public class AnnotatePictureView extends ImageView {
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mPaint;
    private Paint mBitmapPaint;

    public AnnotatePictureView(Context c, Bitmap bitmap) {
        super(c);
        mBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        mCanvas = new Canvas();
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mBitmap = scaleBitmap(mBitmap, w, h);
        mCanvas.setBitmap(mBitmap);
        RelativeLayout relativeLayout = (RelativeLayout) getParent();
        ViewGroup.LayoutParams relativeLayoutLayoutParams = relativeLayout.getLayoutParams();
        if(relativeLayoutLayoutParams != null) {
            relativeLayoutLayoutParams.height = mBitmap.getHeight();
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0xFFAAAAAA);
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touch_up();
                invalidate();
                break;
        }
        return true;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setMPaint(Paint mPaint) {
        this.mPaint = mPaint;
    }

    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }
    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }
    }
    private void touch_up() {
        mPath.lineTo(mX, mY);
        mCanvas.drawPath(mPath, mPaint);
        mPath.reset();
    }

    /**
     * Scale the bitmap according to a maximum width and height keeping the aspect ratio
     *
     * @param bitmap the original bitmap
     * @param maxWidth the maximum width to scale
     * @param maxHeight the minimum width to scale
     * @return the scaled bitmap
     */
    private Bitmap scaleBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (width > height) {
            // landscape
            float ratio = (float) width / maxWidth;
            width = maxWidth;
            height = (int)(height / ratio);
        } else if (height > width) {
            // portrait
            float ratio = (float) height / maxHeight;
            height = maxHeight;
            width = (int)(width / ratio);
        } else {
            // square
            height = maxHeight;
            width = maxWidth;
        }

        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }
}
