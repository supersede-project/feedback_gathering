/**
 * @license CanvasView
 * Android Application Library
 * https://github.com/Korilakkuma/CanvasView
 * <p/>
 * The MIT License
 * <p/>
 * Copyright (c) 2014 Tomohiro IKEDA (Korilakkuma)
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ch.uzh.supersede.feedbacklibrary.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import ch.uzh.supersede.feedbacklibrary.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for the image annotation.
 * <p/>
 * This class is a modification of:
 * CanvasView (see license at the top)
 * Android Application Library
 * https://github.com/Korilakkuma/CanvasView
 */
public class AnnotateImageView extends AppCompatImageView {
    private final Paint emptyPaint = new Paint();
    private Bitmap bitmap = null;
    private List<Path> pathLists = new ArrayList<>();
    private List<Paint> paintLists = new ArrayList<>();
    // Eraser
    private int baseColor = Color.WHITE;
    // Undo, Redo
    private int historyPointer = 0;
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
    private Typeface fontFamily = Typeface.DEFAULT;
    private float fontSize = 32F;
    private Paint.Align textAlign = Paint.Align.RIGHT;
    private Paint textPaint = new Paint();
    private float textX = 0F;
    private float textY = 0F;
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
    // Cropped image history
    private List<File> croppedImageLists = new ArrayList<>();
    private List<Integer> startHistoryPointerLists = new ArrayList<>();
    private boolean isCroppedImageAdded = false;
    private int croppedImagePointer;
    private int startHistoryPointer;
    private int initW;
    private int initH;

    public AnnotateImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setup();
    }

    public AnnotateImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public AnnotateImageView(Context context) {
        super(context);
        setup();
    }

    /**
     * This method adds a cropped image to the cropped image history.
     *
     * @param file the file
     */
    public void addCroppedImage(File file) {
        if (!isCroppedImageAdded) {
            croppedImageLists.add(file);
            croppedImagePointer = 1;
            startHistoryPointer = 0;
            startHistoryPointerLists.add(historyPointer);
            isCroppedImageAdded = !isCroppedImageAdded;
        }
    }

    private Paint createPaint() {
        Paint paint = new Paint();

        paint.setAntiAlias(true);
        paint.setStyle(paintStyle);
        paint.setStrokeWidth(paintStrokeWidth);
        paint.setStrokeCap(lineCap);
        paint.setStrokeJoin(lineJoin);

        // Text
        if (mode == Mode.TEXT) {
            paint.setTypeface(fontFamily);
            paint.setTextSize(fontSize);
            paint.setTextAlign(textAlign);
            paint.setStrokeWidth(0F);
        }

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

    /**
     * This method draws the designated bitmap to the canvas.
     *
     * @param bitmap the bitmap
     */
    public void drawBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        invalidate();
    }

    private void drawText(Canvas canvas) {
        if (text.length() <= 0) {
            return;
        }

        if (mode == Mode.TEXT) {
            this.textX = startX;
            this.textY = startY;

            textPaint = createPaint();
        }

        float canvasTextX = this.textX;
        float canvasTextY = this.textY;

        Paint paintForMeasureText = new Paint();

        // Line break automatically
        float textLength = paintForMeasureText.measureText(text);
        float lengthOfChar = textLength / (float) text.length();
        // Text-align : right
        float restWidth = canvas.getWidth() - canvasTextX;
        // The number of characters at 1 line
        int numChars = (lengthOfChar <= 0) ? 1 : (int) Math.floor((double) (restWidth / lengthOfChar));
        int modNumChars = (numChars < 1) ? 1 : numChars;
        float y = canvasTextY;

        for (int i = 0, len = text.length(); i < len; i += modNumChars) {
            String substring;

            if ((i + modNumChars) < len) {
                substring = text.substring(i, (i + modNumChars));
            } else {
                substring = text.substring(i, len);
            }

            y += fontSize;

            canvas.drawText(substring, canvasTextX, y, textPaint);
        }
    }

    private void enableUndoDisableRedo() {
        undoButton.setEnabled(true);
        undoButton.setAlpha(1.0F);
        redoButton.setEnabled(false);
        redoButton.setAlpha(0.4F);
        noActionExecuted = false;
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

        // Adjust width and height
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

    public int getBitmapHeight() {
        return bitmap.getHeight();
    }

    public int getBitmapWidth() {
        return bitmap.getWidth();
    }

    public float getBlur() {
        return blur;
    }

    public List<File> getCroppedImageLists() {
        return croppedImageLists;
    }

    private Path getCurrentPath() {
        return pathLists.get(historyPointer - 1);
    }

    public int getPaintFillColor() {
        return paintFillColor;
    }

    public int getPaintStrokeColor() {
        return paintStrokeColor;
    }

    public Paint.Style getPaintStyle() {
        return paintStyle;
    }

    public String getText() {
        return text;
    }

    /**
     * This method indicates if a redo operation is possible.
     *
     * @return true if a redo operation is possible, false otherwise
     */
    public boolean isRedoable() {
        return (historyPointer < pathLists.size() || startHistoryPointer < startHistoryPointerLists.size() - 1);
    }

    /**
     * This method indicates if an undo operation is possible.
     *
     * @return true if an undo operation is possible, false otherwise
     */
    public boolean isUndoable() {
        return (historyPointer > 1 || startHistoryPointer > 0);
    }

    private void onActionDown(MotionEvent event) {
        switch (mode) {
            case DRAW:
            case ERASER:
                if ((drawer != Drawer.QUADRATIC_BEZIER) && (drawer != Drawer.QUBIC_BEZIER)) {
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
            case TEXT:
                startX = event.getX();
                startY = event.getY();

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
                if ((drawer != Drawer.QUADRATIC_BEZIER) && (drawer != Drawer.QUBIC_BEZIER)) {
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
            case TEXT:
                startX = x;
                startY = y;

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

        for (int i = startHistoryPointerLists.get(startHistoryPointer); i < historyPointer; i++) {
            Path path = pathLists.get(i);
            Paint paint = paintLists.get(i);

            canvas.drawPath(path, paint);
        }

        drawText(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        // Only adjust the relativeLayout when the AnnotateImageView has not been laid out yet
        if (oldw == 0 && oldh == 0) {
            initW = w;
            initH = h;
            bitmap = Utils.scaleBitmap(bitmap, w, h);
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

    /**
     * This method draws the canvas again for Redo.
     *
     * @return true if Redo is enabled, false otherwise
     */
    public boolean redo() {
        if (historyPointer < pathLists.size()) {
            if ((startHistoryPointer < startHistoryPointerLists.size() - 1) && startHistoryPointerLists.get
                    (startHistoryPointer + 1) == historyPointer) {
                startHistoryPointer++;
                croppedImagePointer++;
                updateCroppedImage();
            } else {
                historyPointer++;
            }

            invalidate();

            return true;
        } else if ((startHistoryPointer < startHistoryPointerLists.size() - 1)) {
            startHistoryPointer++;
            croppedImagePointer++;
            updateCroppedImage();

            invalidate();

            return true;
        }

        return false;
    }

    /**
     * This method sets the canvas background color.
     *
     * @param color the background color
     */
    public void setBaseColor(int color) {
        this.baseColor = color;
    }

    /**
     * This method sets the amount of blur.
     *
     * @param blur the blur
     */
    public void setBlur(float blur) {
        if (blur >= 0) {
            this.blur = blur;
        } else {
            this.blur = 0F;
        }
    }

    /**
     * This method sets the drawer.
     *
     * @param drawer the drawer (PEN, LINE, RECTANGLE, CIRCLE, ELLIPSE, QUADRATIC_BEZIER or QUBIC_BEZIER)
     */
    public void setDrawer(Drawer drawer) {
        this.drawer = drawer;
    }

    /**
     * This method sets font-family of the text to be drawn.
     *
     * @param face the face
     */
    public void setFontFamily(Typeface face) {
        fontFamily = face;
    }

    /**
     * This method sets the font size of the text to be drawn.
     *
     * @param size the font size
     */
    public void setFontSize(float size) {
        if (size >= 0F) {
            fontSize = size;
        } else {
            fontSize = 32F;
        }
    }

    /**
     * This method sets the line cap.
     *
     * @param cap the cap
     */
    public void setLineCap(Paint.Cap cap) {
        lineCap = cap;
    }

    /**
     * This method sets the line join.
     *
     * @param lineJoin the line join
     */
    public void setLineJoin(Paint.Join lineJoin) {
        this.lineJoin = lineJoin;
    }

    /**
     * This method sets the mode.
     *
     * @param mode the mode (DRAW, ERASER or TEXT)
     */
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    /**
     * This method sets if no action has been executed yet.
     *
     * @param noActionExecuted true if no action has been executed yet, false otherwise
     */
    public void setNoActionExecuted(boolean noActionExecuted) {
        this.noActionExecuted = noActionExecuted;
    }

    /**
     * This method sets the alpha value.
     * It must be between 0 and 255.
     *
     * @param opacity the opacity
     */
    public void setOpacity(int opacity) {
        if ((opacity >= 0) && (opacity <= 255)) {
            this.opacity = opacity;
        } else {
            this.opacity = 255;
        }
    }

    /**
     * This method sets the fill color.
     * But, current Android API cannot set fill color (?).
     *
     * @param color the fill color
     */
    public void setPaintFillColor(int color) {
        paintFillColor = color;
    }

    /**
     * This method sets the stroke color.
     *
     * @param color the stroke color
     */
    public void setPaintStrokeColor(int color) {
        paintStrokeColor = color;
    }

    /**
     * This method sets the stroke width.
     *
     * @param width the width
     */
    public void setPaintStrokeWidth(float width) {
        if (width >= 0) {
            paintStrokeWidth = width;
        } else {
            paintStrokeWidth = 3F;
        }
    }

    /**
     * This method sets the paint style.
     *
     * @param style the style (stroke or fill)
     */
    public void setPaintStyle(Paint.Style style) {
        this.paintStyle = style;
    }

    /**
     * This method sets the redo button.
     *
     * @param redoButton the redo button
     */
    public void setRedoButton(ImageButton redoButton) {
        this.redoButton = redoButton;
    }

    /**
     * This method sets the text to be drawn.
     *
     * @param text the text to be drawn
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * This method sets the undo button.
     *
     * @param undoButton the undo button
     */
    public void setUndoButton(ImageButton undoButton) {
        this.undoButton = undoButton;
    }

    private void setup() {
        pathLists.add(new Path());
        paintLists.add(createPaint());
        historyPointer++;

        textPaint.setARGB(0, 255, 255, 255);
    }

    /**
     * This method draws the canvas again for Undo.
     *
     * @return true if Undo is enabled, false otherwise
     */
    public boolean undo() {
        if (historyPointer > 1) {
            if (startHistoryPointerLists.get(startHistoryPointer) == historyPointer && startHistoryPointer > 0) {
                startHistoryPointer--;
                croppedImagePointer--;
                updateCroppedImage();
            } else {
                historyPointer--;
            }

            invalidate();

            return true;
        } else if (startHistoryPointer > 0) {
            startHistoryPointer--;
            croppedImagePointer--;
            updateCroppedImage();

            invalidate();

            return true;
        }

        return false;
    }

    private void updateCroppedImage() {
        String imagePath = croppedImageLists
                .get(croppedImagePointer - 1)
                .getAbsolutePath();
        Bitmap unscaledBitmap = BitmapFactory.decodeFile(imagePath);
        if (croppedImagePointer > 1) {
            this.bitmap = unscaledBitmap;
        } else {
            this.bitmap = Utils.scaleBitmap(unscaledBitmap, initW, initH);
        }
    }

    /**
     * This method updates the image history after a cropping operation.
     *
     * @param file the cropped image
     */
    public void updateCroppedImageHistory(File file) {
        if (croppedImagePointer == croppedImageLists.size()) {
            croppedImageLists.add(file);
            croppedImagePointer++;
            startHistoryPointerLists.add(historyPointer);
            startHistoryPointer++;
        } else {
            croppedImageLists.set(croppedImagePointer, file);
            croppedImagePointer++;

            for (int i = croppedImagePointer, size = croppedImageLists.size(); i < size; i++) {
                if (croppedImageLists
                        .get(croppedImagePointer)
                        .delete()) {
                    croppedImageLists.remove(croppedImagePointer);
                } else {
                    Log.w("AnnotateImageView", "Could not delete croppedImagePointer.");
                }
            }

            for (int i = historyPointer, size = paintLists.size(); i < size; i++) {
                pathLists.remove(historyPointer);
                paintLists.remove(historyPointer);
            }

            startHistoryPointer++;
            for (int i = startHistoryPointer, size = startHistoryPointerLists.size(); i < size; i++) {
                startHistoryPointerLists.remove(startHistoryPointer);
            }
            startHistoryPointerLists.add(historyPointer);
        }
        enableUndoDisableRedo();
        updateCroppedImage();
    }

    private void updateHistory(Path path) {
        if (noActionExecuted) {
            enableUndoDisableRedo();
        }

        if (historyPointer == pathLists.size()) {
            pathLists.add(path);
            paintLists.add(createPaint());
            historyPointer++;
        } else {
            // On the way of Undo or Redo
            pathLists.set(historyPointer, path);
            paintLists.set(historyPointer, createPaint());
            historyPointer++;

            for (int i = historyPointer, size = paintLists.size(); i < size; i++) {
                pathLists.remove(historyPointer);
                paintLists.remove(historyPointer);
            }
        }
    }

    // Enumeration for Mode
    public enum Mode {
        DRAW, TEXT, ERASER
    }

    // Enumeration for Drawer
    public enum Drawer {
        PEN, LINE, ARROW, RECTANGLE, CIRCLE, ELLIPSE, QUADRATIC_BEZIER, QUBIC_BEZIER
    }
}
