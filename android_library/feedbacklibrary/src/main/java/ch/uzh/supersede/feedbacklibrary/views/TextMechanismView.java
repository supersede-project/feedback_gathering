package ch.uzh.supersede.feedbacklibrary.views;

import android.graphics.Color;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;

import ch.uzh.supersede.feedbacklibrary.R;
import ch.uzh.supersede.feedbacklibrary.models.Mechanism;
import ch.uzh.supersede.feedbacklibrary.models.TextMechanism;

/**
 * Text mechanism view
 */
public class TextMechanismView extends MechanismView {
    private TextMechanism textMechanism = null;

    public TextMechanismView(LayoutInflater layoutInflater, Mechanism mechanism) {
        super(layoutInflater);
        this.textMechanism = (TextMechanism) mechanism;
        setEnclosingLayout(getLayoutInflater().inflate(R.layout.mechanism_text_enclosing, null));
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
            textInputEditText.setTextColor(Color.parseColor(textMechanism.getInputTextFontColor()));
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
        if (textMechanism.isTextLengthVisible()) {
            textInputLayout.setCounterEnabled(true);
        }
        if (textMechanism.getMaxLength() != null && textMechanism.isMaxLengthVisible()) {
            textInputLayout.setCounterMaxLength(textMechanism.getMaxLength());
            textInputLayout.setErrorEnabled(true);
            // If there is a maximum length and this maximum length is visible, then the counter must be visible anyways independent of isTextLengthVisible
            textInputLayout.setCounterEnabled(true);
        }

        textInputEditText.addTextChangedListener(new TextWatcher() {
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
                    textInputLayout.setHint(textMechanism.getLabel());
                } else if (s.length() == 0) {
                    textInputLayout.setHint(textMechanism.getHint());
                }

                if (textMechanism.getMaxLength() != null && textMechanism.isMaxLengthVisible() && textInputLayout.isErrorEnabled()) {
                    if (s.length() > textMechanism.getMaxLength()) {
                        textInputLayout.setError(getEnclosingLayout().getResources().getString(R.string.supersede_feedbacklibrary_maximum_character_hint, textMechanism.getMaxLength()));
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
