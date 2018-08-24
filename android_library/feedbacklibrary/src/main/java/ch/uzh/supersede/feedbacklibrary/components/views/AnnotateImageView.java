package ch.uzh.supersede.feedbacklibrary.components.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.*;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.utils.ImageUtility;

public final class AnnotateImageView extends AppCompatImageView {

    private final Paint emptyPaint = new Paint();
    private Bitmap bitmap = null;
    private Bitmap bitmapInitial = null;
    // Eraser
    private int baseColor = Color.WHITE;
    // Undo, Redo
    private int historyPointer = 0;
    private List<Path> pathList = new ArrayList<>();
    private List<Paint> paintList = new ArrayList<>();
    // Flags
    private Mode mode = Mode.DRAW;
    private Drawer drawer = Drawer.PEN;
    private boolean isDown = false;
    // Paint
    private Paint.Style paintStyle = Paint.Style.STROKE;
    private int paintStrokeColor = Color.RED;
    private int paintFillColor = Color.RED;
    private float paintStrokeWidth = 12F;
    private int opacity = 255;
    private float blur = 0F;
    private Paint.Cap lineCap = Paint.Cap.ROUND;
    private Paint.Join lineJoin = Paint.Join.ROUND;
    // Text
    private String text = "";
    private Paint textPaint = new Paint();
    // Drawer
    private float startX = 0F;
    private float startY = 0F;
    private float controlX = 0F;
    private float controlY = 0F;
    private PointF startPoint;
    private PointF endPoint;
    // Parent activity
    private boolean noActionExecuted = true;
    private ImageButton undoButton = null;
    private ImageButton redoButton = null;
    private Context context;
    public AnnotateImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        setup();
    }
    public AnnotateImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setup();
    }

    public AnnotateImageView(Context context) {
        super(context);
        this.context = context;
        setup();
    }

    private Paint createPaint() {
        Paint paint = new Paint();

        paint.setAntiAlias(true);
        paint.setStyle(paintStyle);
        paint.setStrokeWidth(paintStrokeWidth);
        paint.setStrokeCap(lineCap);
        paint.setStrokeJoin(lineJoin);

        // Eraser
        if (mode == Mode.ERASER) {
            paint.setColor(baseColor);
            paint.setShadowLayer(blur, 0F, 0F, baseColor);
        } else {
            // Otherwise
            paint.setColor(paintStrokeColor);
            paint.setShadowLayer(blur, 0F, 0F, paintStrokeColor);
            paint.setAlpha(opacity);
        }

        return paint;
    }

    private Path createPath(MotionEvent event) {
        Path path = new Path();

        // Save for ACTION_MOVE
        startX = event.getX();
        startY = event.getY();

        path.moveTo(startX, startY);

        if (drawer == Drawer.ARROW) {
            startPoint = new PointF(startX, startY);
            endPoint = new PointF();
        }

        return path;
    }

    private void enableUndoDisableRedo() {
        undoButton.setEnabled(true);
        undoButton.setAlpha(1.0F);
        redoButton.setEnabled(false);
        redoButton.setAlpha(0.4F);
        noActionExecuted = false;
    }

    public void onCroppedRefresh(Context context) {
        setBitmap(ImageUtility.loadAnnotatedImageFromDatabase(context));
        resetHistory();
    }

    private void resetHistory() {
        undoButton = (ImageButton) ((Activity) context).findViewById(R.id.supersede_feedbacklibrary_undo_btn);
        redoButton = (ImageButton) ((Activity) context).findViewById(R.id.supersede_feedbacklibrary_redo_btn);

        if (undoButton != null && redoButton != null) {
            undoButton.setEnabled(false);
            undoButton.setAlpha(0.4F);
            undoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isUndoable()) {
                        redoButton.setEnabled(undo());
                        redoButton.setAlpha(1.0F);
                    }
                    if (!isUndoable()) {
                        undoButton.setEnabled(false);
                        undoButton.setAlpha(0.4F);
                        setNoActionExecuted(true);
                    }
                }
            });
            redoButton.setEnabled(false);
            redoButton.setAlpha(0.4F);
            redoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isRedoable()) {
                        undoButton.setEnabled(redo());
                        undoButton.setAlpha(1.0F);
                    }
                    if (!isRedoable()) {
                        redoButton.setEnabled(false);
                        redoButton.setAlpha(0.4F);
                    }
                }
            });
        }
        paintList = new ArrayList<>();
        pathList = new ArrayList<>();
        pathList.add(new Path());
        paintList.add(createPaint());
        noActionExecuted = true;
        historyPointer = 1;
    }

    /**
     * This method returns the part of the current canvas which represents the image, i.e., the 'bitmap part' of the
     * whole view as a bitmap.
     *
     * @return This is returned as bitmap.
     */
    public Bitmap getBitmap() {
        setDrawingCacheEnabled(false);
        setDrawingCacheEnabled(true);

        // Adjust screenWidth and screenHeight
        Bitmap drawingCache = getDrawingCache();
        int width;
        int height;
        if (getBitmapWidth() > drawingCache.getWidth()) {
            width = drawingCache.getWidth();
        } else {
            width = getBitmapWidth();
        }
        if (getBitmapHeight() > drawingCache.getHeight()) {
            height = drawingCache.getHeight();
        } else {
            height = getBitmapHeight();
        }

        return Bitmap.createBitmap(getDrawingCache(), 0, 0, width, height);
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.bitmapInitial = bitmap;
        invalidate();
    }

    public int getBitmapHeight() {
        return bitmap.getHeight();
    }

    public int getBitmapWidth() {
        return bitmap.getWidth();
    }

    public float getBlur() {
        return blur;
    }

    public void setBlur(float blur) {
        if (blur >= 0) {
            this.blur = blur;
        } else {
            this.blur = 0F;
        }
    }

    private Path getCurrentPath() {
        return pathList.get(historyPointer - 1);
    }

    public int getPaintFillColor() {
        return paintFillColor;
    }

    public void setPaintFillColor(int color) {
        paintFillColor = color;
    }

    public int getPaintStrokeColor() {
        return paintStrokeColor;
    }

    public void setPaintStrokeColor(int color) {
        paintStrokeColor = color;
    }

    public Paint.Style getPaintStyle() {
        return paintStyle;
    }

    public void setPaintStyle(Paint.Style style) {
        this.paintStyle = style;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isRedoable() {
        return (historyPointer < pathList.size());
    }

    public boolean isUndoable() {
        return (historyPointer > 1);
    }

    private void onActionDown(MotionEvent event) {
        switch (mode) {
            case DRAW:
            case ERASER:
                if ((drawer != Drawer.QUADRATIC_BEZIER) && (drawer != Drawer.CUBIC_BEZIER)) {
                    // Otherwise
                    updateHistory(createPath(event));
                    isDown = true;
                } else {
                    // Bezier
                    if ((startX == 0F) && (startY == 0F)) {
                        // The 1st tap
                        updateHistory(createPath(event));
                    } else {
                        // The 2nd tap
                        controlX = event.getX();
                        controlY = event.getY();

                        isDown = true;
                    }
                }

                break;
            default:
                break;
        }
    }

    private void onActionMove(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (mode) {
            case DRAW:
            case ERASER:
                if ((drawer != Drawer.QUADRATIC_BEZIER) && (drawer != Drawer.CUBIC_BEZIER)) {
                    if (!isDown) {
                        return;
                    }
                    Path path = getCurrentPath();
                    switch (this.drawer) {
                        case PEN:
                            path.lineTo(x, y);
                            break;
                        case LINE:
                            path.reset();
                            path.moveTo(startX, startY);
                            path.lineTo(x, y);
                            break;
                        case ARROW:
                            path.reset();
                            path.moveTo(startX, startY);
                            path.lineTo(x, y);

                            this.endPoint.x = x;
                            this.endPoint.y = y;

                            float deltaX = endPoint.x - startPoint.x;
                            float deltaY = endPoint.y - startPoint.y;
                            float fracture = (float) 0.1;
                            float pointX1 = startPoint.x + ((1 - fracture) * deltaX + fracture * deltaY);
                            float pointY1 = startPoint.y + ((1 - fracture) * deltaY - fracture * deltaX);
                            float pointX2 = endPoint.x;
                            float pointY2 = endPoint.y;
                            float pointX3 = startPoint.x + ((1 - fracture) * deltaX - fracture * deltaY);
                            float pointY3 = startPoint.y + ((1 - fracture) * deltaY + fracture * deltaX);

                            path.moveTo(pointX1, pointY1);
                            path.lineTo(pointX2, pointY2);
                            path.lineTo(pointX3, pointY3);
                            break;
                        case RECTANGLE:
                            path.reset();
                            path.addRect(startX, startY, x, y, Path.Direction.CCW);
                            break;
                        case CIRCLE:
                            double distanceX = Math.abs((double) (startX - x));
                            double distanceY = Math.abs((double) (startX - y));
                            double radius = Math.sqrt(Math.pow(distanceX, 2.0) + Math.pow(distanceY, 2.0));

                            path.reset();
                            path.addCircle(startX, startY, (float) radius, Path.Direction.CCW);
                            break;
                        case ELLIPSE:
                            RectF rect = new RectF(startX, startY, x, y);

                            path.reset();
                            path.addOval(rect, Path.Direction.CCW);
                            break;
                        default:
                            break;
                    }
                } else {
                    if (!isDown) {
                        return;
                    }

                    Path path = getCurrentPath();

                    path.reset();
                    path.moveTo(startX, startY);
                    path.quadTo(controlX, controlY, x, y);
                }

                break;
            default:
                break;
        }
    }

    private void onActionUp() {
        if (isDown) {
            startX = 0F;
            startY = 0F;
            isDown = false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Before "drawPath"
        canvas.drawColor(baseColor);

        if (bitmap != null) {
            canvas.drawBitmap(bitmap, 0F, 0F, emptyPaint);
        }

        for (int i = 0; i < historyPointer; i++) {
            Path path = pathList.get(i);
            Paint paint = paintList.get(i);

            canvas.drawPath(path, paint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // Only adjust the relativeLayout when the AnnotateImageView has not been laid out yet
        if (oldw == 0 && oldh == 0) {
            bitmap = ImageUtility.scaleBitmap(bitmap, w, h);
            bitmapInitial = ImageUtility.scaleBitmap(bitmapInitial, w, h);
            RelativeLayout relativeLayout = (RelativeLayout) getParent();
            ViewGroup.LayoutParams relativeLayoutLayoutParams = relativeLayout.getLayoutParams();
            if (relativeLayoutLayoutParams != null) {
                relativeLayoutLayoutParams.width = w;
                relativeLayoutLayoutParams.height = h;
            }
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                onActionDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                onActionMove(event);
                break;
            case MotionEvent.ACTION_UP:
                onActionUp();
                break;
            default:
                break;
        }

        // Redraw
        invalidate();
        return true;
    }

    public boolean redo() {
        if (historyPointer < pathList.size()) {
            historyPointer++;
            invalidate();
            return true;
        }
        return false;
    }

    public boolean undo() {
        if (historyPointer > 1) {
            historyPointer--;
            invalidate();
            return true;
        }
        return false;
    }

    public void setBaseColor(int color) {
        this.baseColor = color;
    }

    public void setDrawer(Drawer drawer) {
        this.drawer = drawer;
    }

    public void setLineCap(Paint.Cap cap) {
        lineCap = cap;
    }

    public void setLineJoin(Paint.Join lineJoin) {
        this.lineJoin = lineJoin;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setNoActionExecuted(boolean noActionExecuted) {
        this.noActionExecuted = noActionExecuted;
    }

    public void setOpacity(int opacity) {
        if ((opacity >= 0) && (opacity <= 255)) {
            this.opacity = opacity;
        } else {
            this.opacity = 255;
        }
    }

    public void setPaintStrokeWidth(float width) {
        if (width >= 0) {
            paintStrokeWidth = width;
        } else {
            paintStrokeWidth = 3F;
        }
    }

    public void setRedoButton(ImageButton redoButton) {
        this.redoButton = redoButton;
    }

    public void setUndoButton(ImageButton undoButton) {
        this.undoButton = undoButton;
    }

    private void setup() {
        resetHistory();
        textPaint.setARGB(0, 0, 0, 0);
    }

    private void updateHistory(Path path) {
        if (noActionExecuted) {
            enableUndoDisableRedo();
        }

        if (historyPointer == pathList.size()) {
            pathList.add(path);
            paintList.add(createPaint());
            historyPointer++;
        } else {
            // On the way of Undo or Redo
            pathList.set(historyPointer, path);
            paintList.set(historyPointer, createPaint());
            historyPointer++;

            for (int i = historyPointer, size = paintList.size(); i < size; i++) {
                pathList.remove(historyPointer);
                paintList.remove(historyPointer);
            }
        }
    }

    public enum Mode {
        DRAW, ERASER
    }

    public enum Drawer {
        PEN, LINE, ARROW, RECTANGLE, CIRCLE, ELLIPSE, QUADRATIC_BEZIER, CUBIC_BEZIER
    }
}
