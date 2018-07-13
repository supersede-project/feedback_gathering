package ch.uzh.supersede.feedbacklibrary.models;

import com.google.gson.annotations.Expose;

import java.util.List;

import ch.uzh.supersede.feedbacklibrary.beans.LocalConfigurationBean;
import ch.uzh.supersede.feedbacklibrary.utils.StringUtility;

public class TextFeedback extends AbstractFeedbackPart {
    private String hint;
    private String label;
    private Integer maxLength;
    private Integer minLength;
    @Expose
    private String text;

    public TextFeedback(LocalConfigurationBean configuration) {
        super(configuration.getTextOrder());
        this.hint = configuration.getTextHint();
        this.label = configuration.getTextLabel();
        this.maxLength = configuration.getMaxTextLength();
        this.minLength = configuration.getMinTextLength();
    }

    @Override
    public boolean isValid(List<String> errorMessages) {
        if (StringUtility.hasText(text)) {
            if (text.length() < getMinLength()) {
                errorMessages.add("Feedback-Text too short!");
            } else if (text.length() > maxLength) {
                errorMessages.add("Feedback-Text too long!");
            } else {
                return true;
            }
            return false;
        }
        errorMessages.add("Feedback-Text is empty!");
        return false;
    }

    public String getHint() {
        return hint;
    }

    public String getLabel() {
        return label;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
