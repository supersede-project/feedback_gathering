package com.example.matthias.feedbacklibrary.views;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;

import com.example.matthias.feedbacklibrary.R;
import com.example.matthias.feedbacklibrary.models.Mechanism;
import com.example.matthias.feedbacklibrary.models.TextMechanism;

/**
 * Text mechanism view
 */
public class TextMechanismView extends MechanismView {
    private TextMechanism textMechanism = null;

    public TextMechanismView(LayoutInflater layoutInflater, Mechanism mechanism) {
        super(layoutInflater);
        this.textMechanism = (TextMechanism) mechanism;
        setEnclosingLayout(getLayoutInflater().inflate(R.layout.text_feedback_layout, null));
        initView();
    }

    private void initView() {
        final TextInputLayout textInputLayout = (TextInputLayout) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_text_feedback_input_layout);
        final TextInputEditText textInputEditText = (TextInputEditText) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_text_feedback_text);

        // Set the hint and enable it
        textInputLayout.setHintEnabled(true);
        textInputLayout.setHint(textMechanism.getHint());

        // Only set the values if they are initialized (else use the values defined in the layout)
        if (textMechanism.getInputTextFontColor() != null) {
            textInputEditText.setTextColor(textMechanism.getInputTextFontColor());
        }
        if (textMechanism.getInputTextFontSize() != null) {
            textInputEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, textMechanism.getInputTextFontSize());
        }
        if (textMechanism.getInputTextFontType() != null) {
            textInputEditText.setTypeface(null, textMechanism.getInputTextFontType());
        }
        if (textMechanism.getInputTextAlignment() != null) {
            String alignment = textMechanism.getInputTextAlignment();
            switch (alignment) {
                case "center":
                    textInputEditText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    break;
                case "right":
                    textInputEditText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                    break;
                case "left":
                    textInputEditText.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                    break;
                default:
                    break;
            }
        }
        if (textMechanism.getMaxLength() != null) {
            textInputLayout.setCounterMaxLength(textMechanism.getMaxLength());
            textInputLayout.setErrorEnabled(true);
            // If TI 19 is set, TI 19.1 is a possible option
            if (textMechanism.isMaxLengthVisible()) {
                textInputLayout.setCounterEnabled(true);
            }
        }
        if (textMechanism.isMandatory()) {
            // If TI 11 is set, TI 11.1 should not be an option, i.e., the reminder should always be activated --> TI 11.1.1 and TI 11.1.2
            // TODO: Implement visual changes of a mandatory text field?
        }

        textInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    textInputLayout.setHint(textMechanism.getLabel());
                } else if (s.length() == 0) {
                    textInputLayout.setHint(textMechanism.getHint());
                }

                if (textMechanism.getMaxLength() != null && textInputLayout.isErrorEnabled()) {
                    if (s.length() > textMechanism.getMaxLength()) {
                        textInputLayout.setError("Text cannot be longer than " + textMechanism.getMaxLength() + " characters");
                    } else {
                        textInputLayout.setError(null);
                    }
                }
            }
        });
    }

    @Override
    public void updateModel() {
        textMechanism.setInputText(((TextInputEditText) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_text_feedback_text)).getText().toString());
    }
}
