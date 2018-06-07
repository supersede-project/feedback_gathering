package ch.uzh.supersede.feedbacklibrary.components.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ch.uzh.supersede.feedbacklibrary.R;

import static android.graphics.Color.WHITE;
import static android.graphics.Typeface.BOLD;
import static android.view.Gravity.*;
import static ch.uzh.supersede.feedbacklibrary.utils.Constants.ViewsConstants.BUTTON_SIZE_DP;

public class TextAnnotationImageView extends AbstractAnnotationView {
    private TextView annotationNumber;
    private ImageView editAnnotation;

    // Text annotation
    private String annotationInputText;
    private String annotationInputTextHint;
    private String annotationInputTextLabel;
    private AlertDialog textAnnotationDialog;

    private OnTextAnnotationChangedListener onTextAnnotationChangedListener;

    public TextAnnotationImageView(Context context) {
        super(context);
    }

    public TextAnnotationImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextAnnotationImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public interface OnTextAnnotationChangedListener {
        void onTextAnnotationDelete();
    }

    public void setAnnotationInputText(String annotationInputText) {
        this.annotationInputText = annotationInputText;
    }

    public String getAnnotationInputText() {
        return annotationInputText;
    }

    public String getAnnotationInputTextHint() {
        return annotationInputTextHint;
    }

    public void setAnnotationInputTextHint(String annotationInputTextHint) {
        this.annotationInputTextHint = annotationInputTextHint;
    }

    public String getAnnotationInputTextLabel() {
        return annotationInputTextLabel;
    }

    public void setAnnotationInputTextLabel(String annotationInputTextLabel) {
        this.annotationInputTextLabel = annotationInputTextLabel;
    }

    public void setOnTextAnnotationChangedListener(OnTextAnnotationChangedListener onTextAnnotationChangedListener) {
        this.onTextAnnotationChangedListener = onTextAnnotationChangedListener;
    }

    public TextView getAnnotationNumber() {
        return annotationNumber;
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        final Context textAnnotationContext = context;

        annotationNumber = new TextView(context);
        editAnnotation = new ImageView(context);

        editAnnotation.setImageResource(R.drawable.ic_mode_edit_black_48dp);
        annotationNumber.setBackgroundResource(R.drawable.ic_lens_black_48dp);

        annotationNumber.setTypeface(null, BOLD);
        annotationNumber.setTextColor(WHITE);
        annotationNumber.setGravity(CENTER);

        annotationNumber.setTag("annotationNumber");
        editAnnotation.setTag("editAnnotation");

        int buttonSize = convertDpToPixel(BUTTON_SIZE_DP, getContext());

        LayoutParams annotationNumberParams = new LayoutParams(buttonSize, buttonSize);
        LayoutParams editAnnotationsParams = new LayoutParams(buttonSize, buttonSize);

        annotationNumberParams.gravity = BOTTOM | START;
        editAnnotationsParams.gravity = BOTTOM | END;

        addView(annotationNumber, annotationNumberParams);
        addView(editAnnotation, editAnnotationsParams);

        editAnnotation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showTextAnnotationDialog(textAnnotationContext);
            }
        });
    }

    @Override
    protected void execDeleteAnnotationOnClick() {
        super.execDeleteAnnotationOnClick();
        onTextAnnotationChangedListener.onTextAnnotationDelete();
    }

    @Override
    public void setViewsVisible(boolean isVisible) {
        super.setViewsVisible(isVisible);
        int visibility = isVisible ? VISIBLE : INVISIBLE;
        editAnnotation.setVisibility(visibility);
        annotationNumber.setVisibility(visibility);
    }

    private void showTextAnnotationDialog(Context context) {
        if (textAnnotationDialog != null) {
            textAnnotationDialog.show();
            return;
        }
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        // Inflating the custom layout
        LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(R.layout.text_feedback, null);

        final TextInputLayout textAnnotationLayout = (TextInputLayout) linearLayout.findViewById(R.id.supersede_feedbacklibrary_text_annotation_dialog_input_layout);
        final TextInputEditText textAnnotationText = (TextInputEditText) linearLayout.findViewById(R.id.supersede_feedbacklibrary_text_annotation_dialog_text);

        // Set the input text if the sticker is restored
        if (annotationInputText != null) {
            textAnnotationText.setText(getAnnotationInputText());
        }
        // Set the hint and enable it
        textAnnotationLayout.setHintEnabled(true);
        textAnnotationLayout.setHint(getAnnotationInputTextHint());
        //TODO somekind of listener here:
        textAnnotationText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    Log.d("focus","on");
                } else {
                    Log.d("focus","off");
                }
            }
        });
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
        dialog.setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setAnnotationInputText(textAnnotationText.getText().toString());
                if (emptyLayout != null) {
                    emptyLayout.requestFocus();
                }
                setViewsVisible(false);
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
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
        textAnnotationDialog = dialog.create();
        textAnnotationDialog.show();
    }
}
