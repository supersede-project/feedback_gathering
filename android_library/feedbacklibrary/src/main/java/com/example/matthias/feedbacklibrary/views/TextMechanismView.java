package com.example.matthias.feedbacklibrary.views;

import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;

import com.example.matthias.feedbacklibrary.R;
import com.example.matthias.feedbacklibrary.models.Mechanism;
import com.example.matthias.feedbacklibrary.models.TextMechanism;

/**
 * Created by Matthias on 24.04.2016.
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
        final TextInputLayout textInputLayout = ((TextInputLayout) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_text_feedback_input_layout));
        textInputLayout.setHintEnabled(true);
        textInputLayout.setHint(textMechanism.getHint());
        textInputLayout.setCounterEnabled(true);
        textInputLayout.setCounterMaxLength(textMechanism.getMaxLength());
        textInputLayout.setErrorEnabled(true);

        textInputLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 ) {
                    textInputLayout.setHint(textMechanism.getTitle());
                } else if (s.length() == 0) {
                    textInputLayout.setHint(textMechanism.getHint());
                }
                if (s.length() > textMechanism.getMaxLength()) {
                    textInputLayout.setError("Text cannot be longer than " + textMechanism.getMaxLength() + " characters");
                } else {
                    textInputLayout.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void updateModel() {
        textMechanism.setInputText(((TextInputEditText) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_text_feedback_text)).getText().toString());
    }


}
