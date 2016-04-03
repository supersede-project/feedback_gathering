package com.example.matthias.feedbacklibrary.models;

import android.widget.EditText;
import android.widget.TextView;

import com.example.matthias.feedbacklibrary.R;

import java.io.Serializable;

/**
 * Created by Matthias on 19.03.2016.
 */
public class TextMechanism extends Mechanism implements Serializable {
    private static final String TEXT_TYPE = "TEXT_MECHANISM";

    private String title;
    private String hint;
    private int maxLength;

    private String inputText;

    public TextMechanism(boolean canBeActivated, boolean isActive, int order) {
        super(TEXT_TYPE, canBeActivated, isActive, order);
    }

    public void updateView() {
        ((TextView) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_text_feedback_title)).setText(title);
        ((EditText) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_text_feedback_text)).setHint(hint);
    }

    public void updateModel() {
        this.inputText = ((EditText) getEnclosingLayout().findViewById(R.id.supersede_feedbacklibrary_text_feedback_text)).getText().toString();
    }

    public String getInputText() {
        return inputText;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
