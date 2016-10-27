package ch.uzh.supersede.feedbacklibrary.feedbacks;

import ch.uzh.supersede.feedbacklibrary.models.TextMechanism;
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

    /**
     * This method returns the mechanism id.
     *
     * @return the mechanism id
     */
    public long getMechanismId() {
        return mechanismId;
    }

    /**
     * This method returns the text.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * This method sets the mechanism id.
     *
     * @param mechanismId the mechanism id
     */
    public void setMechanismId(long mechanismId) {
        this.mechanismId = mechanismId;
    }

    /**
     * This method sets the text.
     *
     * @param text the text
     */
    public void setText(String text) {
        this.text = text;
    }
}
