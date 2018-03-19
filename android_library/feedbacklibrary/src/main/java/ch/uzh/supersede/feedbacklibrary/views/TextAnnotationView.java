package ch.uzh.supersede.feedbacklibrary.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.uzh.supersede.feedbacklibrary.R;

import static android.view.Gravity.*;

/**
 * Sticker view
 */
public abstract class TextAnnotationView extends AbstractAnnotationView {

    private ImageView editImageView;
    // Text annotation
    private String annotationInputTextHint;
    private String annotationInputTextLabel;
    private String annotationInputText;
    private AlertDialog textAnnotationDialog;
    // Sticker border
    private TextView annotationNumberView;

    // Touch listener
    private OnTextAnnotationChangedListener onTextAnnotationChangedListener;

    public TextAnnotationView(Context context) {
        super(context);
    }

    public TextAnnotationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextAnnotationView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public AlertDialog getTextAnnotationDialog() {
        return textAnnotationDialog;
    }

    public OnTextAnnotationChangedListener getOnTextAnnotationChangedListener() {
        return onTextAnnotationChangedListener;
    }

    public ImageView getEditImageView() {
        return editImageView;
    }

    public String getAnnotationInputText() {
        return annotationInputText;
    }

    public String getAnnotationInputTextHint() {
        return annotationInputTextHint;
    }

    public String getAnnotationInputTextLabel() {
        return annotationInputTextLabel;
    }

    public TextView getAnnotationNumberView() {
        return annotationNumberView;
    }

    public void setAnnotationInputText(String annotationInputText) {
        this.annotationInputText = annotationInputText;
    }

    public void setAnnotationInputTextHint(String annotationInputTextHint) {
        this.annotationInputTextHint = annotationInputTextHint;
    }

    public void setAnnotationInputTextLabel(String annotationInputTextLabel) {
        this.annotationInputTextLabel = annotationInputTextLabel;
    }

    public void setOnTextAnnotationChangedListener(OnTextAnnotationChangedListener onTextAnnotationChangedListener) {
        this.onTextAnnotationChangedListener = onTextAnnotationChangedListener;
    }

    @Override
    public void setControlItemsInvisible(boolean isControlItemsInvisible) {
        super.setControlItemsInvisible(isControlItemsInvisible);
        getEditImageView().setVisibility(isControlItemsInvisible ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    protected void init(final Context context) {
        super.init(context);

        this.editImageView = new ImageView(context);
        this.textAnnotationDialog = initTextAnnotationDialog(context);
        this.annotationNumberView = new TextView(context);

        getEditImageView().setImageResource(R.drawable.ic_mode_edit_black_48dp);
        getAnnotationNumberView().setBackgroundResource(R.drawable.ic_lens_black_48dp);
        getAnnotationNumberView().setTypeface(null, Typeface.BOLD);
        getAnnotationNumberView().setTextColor(Color.WHITE);
        getAnnotationNumberView().setGravity(CENTER);

        setTag("DraggableViewGroup");
        getEditImageView().setTag("editImageView");
        getAnnotationNumberView().setTag("annotationNumberView");

        int size = convertDpToPixel(BUTTON_SIZE_DP, getContext());

        // Annotation number view
        LayoutParams ivAnnotationNumberViewParams = new LayoutParams(size, size);
        ivAnnotationNumberViewParams.gravity = BOTTOM | START;

        // Edit image view
        LayoutParams ivEditParams = new LayoutParams(size, size);
        ivEditParams.gravity = BOTTOM | END;

        addView(getAnnotationNumberView(), ivAnnotationNumberViewParams);
        addView(getEditImageView(), ivEditParams);
        setControlItemsInvisible(false);

        setOnTouchListener(getOnTouchListener());
        getDeleteImageView().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextAnnotationView.this.getParent() != null) {
                    ViewGroup myCanvas = ((ViewGroup) TextAnnotationView.this.getParent());
                    myCanvas.removeView(TextAnnotationView.this);
                    getOnTextAnnotationChangedListener().onTextAnnotationDelete();
                }
            }
        });
        getEditImageView().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getTextAnnotationDialog().show();
            }
        });
    }

    private AlertDialog initTextAnnotationDialog(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        // Inflating the custom layout
        LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.text_annotation_dialog_layout, null);

        final TextInputLayout textAnnotationLayout = (TextInputLayout) linearLayout.findViewById(R.id.supersede_feedbacklibrary_text_annotation_dialog_input_layout);
        final TextInputEditText textAnnotationText = (TextInputEditText) linearLayout.findViewById(R.id.supersede_feedbacklibrary_text_annotation_dialog_text);

        // Set the input text if the sticker is restored
        if (annotationInputText != null) {
            textAnnotationText.setText(getAnnotationInputText());
        }
        // Set the hint and enable it
        textAnnotationLayout.setHintEnabled(true);
        textAnnotationLayout.setHint(getAnnotationInputTextHint());
        textAnnotationText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                //nop
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //nop
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    textAnnotationLayout.setHint(getAnnotationInputTextLabel());
                } else if (s.length() == 0) {
                    textAnnotationLayout.setHint(getAnnotationInputTextHint());
                }
            }
        });

        final LinearLayout emptyLayout = (LinearLayout) linearLayout.findViewById(R.id.supersede_feedbacklibrary_text_annotation_dialog_empty_layout);

        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setPositiveButton(getResources().getString(R.string.supersede_feedbacklibrary_ok_string), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setAnnotationInputText(textAnnotationText.getText().toString());
                if (emptyLayout != null) {
                    emptyLayout.requestFocus();
                }
                setControlItemsInvisible(true);
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton(getResources().getString(R.string.supersede_feedbacklibrary_cancel_string), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (emptyLayout != null) {
                    emptyLayout.requestFocus();
                }
                dialog.dismiss();
            }
        });
        dialog.setView(linearLayout);
        dialog.setCancelable(false);
        return dialog.create();
    }

    public interface OnTextAnnotationChangedListener {
        void onTextAnnotationDelete();
    }

    @Override
    protected void initOnTouchListener(){
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (view.getTag().equals("DraggableViewGroup")) {
                    initDraggableViewGroup(event);
                }
                return true;
            }
        });
    }
}
