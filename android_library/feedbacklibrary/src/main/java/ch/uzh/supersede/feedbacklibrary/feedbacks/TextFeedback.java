package ch.uzh.supersede.feedbacklibrary.feedbacks;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

import ch.uzh.supersede.feedbacklibrary.models.TextMechanism;

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

    public void setMechanismId(long mechanismId) {
        this.mechanismId = mechanismId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
