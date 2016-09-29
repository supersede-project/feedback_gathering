package com.example.matthias.feedbacklibrary.feedbacks;

import com.example.matthias.feedbacklibrary.models.TextMechanism;
import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Text feedback.
 */
public class TextFeedback implements Serializable {
    @Expose
    private long mechanismId;
    @Expose
    private String text;

    public TextFeedback(TextMechanism textMechanism) {
        setMechanismId(textMechanism.getId());
        setText(textMechanism.getInputText());
    }

    public long getMechanismId() {
        return mechanismId;
    }

    public String getText() {
        return text;
    }

    public void setMechanismId(long mechanismId) {
        this.mechanismId = mechanismId;
    }

    public void setText(String text) {
        this.text = text;
    }
}
