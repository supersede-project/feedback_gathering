package com.example.matthias.feedbacklibrary.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.matthias.feedbacklibrary.R;

/**
 * Sticker view
 */
public abstract class TextAnnotationView extends FrameLayout {
    private final static int BUTTON_SIZE_DP = 25;
    private final static int SELF_SIZE_DP = 90;
    // Text annotation
    private String annotationInputTextHint;
    private String annotationInputTextLabel;
    private String annotationInputText;
    private AlertDialog textAnnotationDialog;
    // Sticker border
    private BorderView borderView;
    private TextView annotationNumberView;
    private ImageView deleteImageView;
    private ImageView editImageView;
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
    private OnTextAnnotationChangedListener onTextAnnotationChangedListener;

    public TextAnnotationView(Context context) {
        super(context);
        init(context);
    }

    public TextAnnotationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TextAnnotationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private int convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }

    /**
     * This method returns the input text of the text annotation.
     *
     * @return the annotation input text
     */
    public String getAnnotationInputText() {
        return annotationInputText;
    }

    /**
     * This method returns the hint of the text annotation.
     *
     * @return the annotation hint
     */
    public String getAnnotationInputTextHint() {
        return annotationInputTextHint;
    }

    /**
     * This method returns the label of the text annotation.
     *
     * @return the annotation label
     */
    public String getAnnotationInputTextLabel() {
        return annotationInputTextLabel;
    }

    /**
     * This method returns the annotation number view.
     *
     * @return the annotation number view
     */
    public TextView getAnnotationNumberView() {
        return annotationNumberView;
    }

    private double getLength(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));
    }

    protected abstract View getMainView();

    private void init(final Context context) {
        initOnTouchListener();

        borderView = new BorderView(context);
        annotationNumberView = new TextView(context);
        annotationNumberView.setBackgroundResource(R.drawable.ic_lens_black_48dp);
        annotationNumberView.setTypeface(null, Typeface.BOLD);
        annotationNumberView.setTextColor(Color.WHITE);
        annotationNumberView.setGravity(Gravity.CENTER);
        deleteImageView = new ImageView(context);
        editImageView = new ImageView(context);
        checkImageView = new ImageView(context);

        deleteImageView.setImageResource(R.drawable.ic_delete_black_48dp);
        editImageView.setImageResource(R.drawable.ic_mode_edit_black_48dp);
        checkImageView.setImageResource(R.drawable.ic_check_circle_black_48dp);

        setTag("DraggableViewGroup");
        borderView.setTag("borderView");
        annotationNumberView.setTag("annotationNumberView");
        deleteImageView.setTag("deleteImageView");
        editImageView.setTag("editImageView");
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
        // Annotation number view
        LayoutParams ivAnnotationNumberViewParams = new LayoutParams(convertDpToPixel(BUTTON_SIZE_DP, getContext()), convertDpToPixel(BUTTON_SIZE_DP, getContext()));
        ivAnnotationNumberViewParams.gravity = Gravity.BOTTOM | Gravity.START;
        // Delete image view
        LayoutParams ivDeleteParams = new LayoutParams(convertDpToPixel(BUTTON_SIZE_DP, getContext()), convertDpToPixel(BUTTON_SIZE_DP, getContext()));
        ivDeleteParams.gravity = Gravity.TOP | Gravity.START;
        // Edit image view
        LayoutParams ivEditParams = new LayoutParams(convertDpToPixel(BUTTON_SIZE_DP, getContext()), convertDpToPixel(BUTTON_SIZE_DP, getContext()));
        ivEditParams.gravity = Gravity.BOTTOM | Gravity.END;
        // Check image view
        LayoutParams ivCheckParams = new LayoutParams(convertDpToPixel(BUTTON_SIZE_DP, getContext()), convertDpToPixel(BUTTON_SIZE_DP, getContext()));
        ivCheckParams.gravity = Gravity.TOP | Gravity.END;

        setLayoutParams(thisParams);
        addView(getMainView(), ivMainParams);
        addView(borderView, ivBorderParams);
        addView(annotationNumberView, ivAnnotationNumberViewParams);
        addView(deleteImageView, ivDeleteParams);
        addView(editImageView, ivEditParams);
        addView(checkImageView, ivCheckParams);
        setControlItemsHidden(false);

        setOnTouchListener(onTouchListener);
        deleteImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextAnnotationView.this.getParent() != null) {
                    ViewGroup myCanvas = ((ViewGroup) TextAnnotationView.this.getParent());
                    myCanvas.removeView(TextAnnotationView.this);
                    onTextAnnotationChangedListener.onTextAnnotationDelete();
                }
            }
        });
        editImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showTextAnnotationDialog(context);
            }
        });
        checkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setControlItemsHidden(true);
            }
        });
    }

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
                            TextAnnotationView.this.setX(TextAnnotationView.this.getX() + offsetX);
                            TextAnnotationView.this.setY(TextAnnotationView.this.getY() + offsetY);
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

                            centerX = TextAnnotationView.this.getX() + ((View) TextAnnotationView.this.getParent()).getX() + (float) TextAnnotationView.this.getWidth() / 2;

                            int result = 0;
                            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
                            if (resourceId > 0) {
                                result = getResources().getDimensionPixelSize(resourceId);
                            }
                            double statusBarHeight = result;
                            centerY = TextAnnotationView.this.getY() + ((View) TextAnnotationView.this.getParent()).getY() + statusBarHeight + (float) TextAnnotationView.this.getHeight() / 2;

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
                                TextAnnotationView.this.getLayoutParams().width += offset;
                                TextAnnotationView.this.getLayoutParams().height += offset;
                                onScaling(true);
                            } else if (length2 < length1 && (angle_diff < 25 || Math.abs(angle_diff - 180) < 25) && TextAnnotationView.this.getLayoutParams().width > size / 2 && TextAnnotationView.this.getLayoutParams().height > size / 2) {
                                // Scale down
                                double offsetX = Math.abs(event.getRawX() - scaleOrgX);
                                double offsetY = Math.abs(event.getRawY() - scaleOrgY);
                                double offset = Math.max(offsetX, offsetY);
                                offset = Math.round(offset);
                                TextAnnotationView.this.getLayoutParams().width -= offset;
                                TextAnnotationView.this.getLayoutParams().height -= offset;
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
     * This method sets the input text of the text annotation.
     *
     * @param annotationInputText the annotation input text
     */
    public void setAnnotationInputText(String annotationInputText) {
        this.annotationInputText = annotationInputText;
    }

    /**
     * This method sets the hint of the text annotation.
     *
     * @param annotationInputTextHint the annotation hint
     */
    public void setAnnotationInputTextHint(String annotationInputTextHint) {
        this.annotationInputTextHint = annotationInputTextHint;
    }

    /**
     * This method sets the label of the text annotation.
     *
     * @param annotationInputTextLabel the annotation label
     */
    public void setAnnotationInputTextLabel(String annotationInputTextLabel) {
        this.annotationInputTextLabel = annotationInputTextLabel;
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
            deleteImageView.setVisibility(View.INVISIBLE);
            editImageView.setVisibility(View.INVISIBLE);
            checkImageView.setVisibility(View.INVISIBLE);
        } else {
            borderView.setVisibility(View.VISIBLE);
            deleteImageView.setVisibility(View.VISIBLE);
            editImageView.setVisibility(View.VISIBLE);
            checkImageView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * This method sets the listener for text annotation changes.
     *
     * @param onTextAnnotationChangedListener the listener
     */
    public void setOnTextAnnotationChangedListener(OnTextAnnotationChangedListener onTextAnnotationChangedListener) {
        this.onTextAnnotationChangedListener = onTextAnnotationChangedListener;
    }

    private void showTextAnnotationDialog(Context context) {
        if (textAnnotationDialog == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            // Inflating the custom layout
            LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.text_annotation_dialog_layout, null);

            final TextInputLayout textAnnotationDialogInputLayout = (TextInputLayout) linearLayout.findViewById(R.id.supersede_feedbacklibrary_text_annotation_dialog_input_layout);
            final TextInputEditText textAnnotationDialogInputEditText = (TextInputEditText) linearLayout.findViewById(R.id.supersede_feedbacklibrary_text_annotation_dialog_text);

            // Set the input text if the sticker is restored
            if (annotationInputText != null) {
                textAnnotationDialogInputEditText.setText(getAnnotationInputText());
            }
            // Set the hint and enable it
            textAnnotationDialogInputLayout.setHintEnabled(true);
            textAnnotationDialogInputLayout.setHint(getAnnotationInputTextHint());
            textAnnotationDialogInputEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() > 0) {
                        textAnnotationDialogInputLayout.setHint(getAnnotationInputTextLabel());
                    } else if (s.length() == 0) {
                        textAnnotationDialogInputLayout.setHint(getAnnotationInputTextHint());
                    }
                }
            });

            final LinearLayout emptyLayout = (LinearLayout) linearLayout.findViewById(R.id.supersede_feedbacklibrary_text_annotation_dialog_empty_layout);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setPositiveButton(getResources().getString(R.string.supersede_feedbacklibrary_ok_string), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setAnnotationInputText(textAnnotationDialogInputEditText.getText().toString());
                    if (emptyLayout != null) {
                        emptyLayout.requestFocus();
                    }
                    setControlItemsHidden(true);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton(getResources().getString(R.string.supersede_feedbacklibrary_cancel_string), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (emptyLayout != null) {
                        emptyLayout.requestFocus();
                    }
                    dialog.dismiss();
                }
            });
            builder.setView(linearLayout);
            builder.setCancelable(false);
            textAnnotationDialog = builder.create();
        }
        textAnnotationDialog.show();
    }

    public interface OnTextAnnotationChangedListener {
        void onTextAnnotationDelete();
    }
}
