package ch.uzh.supersede.feedbacklibrary.models;

import com.google.gson.annotations.Expose;

import java.util.List;

import ch.uzh.supersede.feedbacklibrary.beans.LocalConfigurationBean;

public class TextFeedback extends AbstractFeedbackPart {
    private String hint;
    private String label;
    private Integer maxLength;
    private Integer minLength;
    @Expose
    private String text;

    public TextFeedback(long mechanismId, LocalConfigurationBean configuration) {
        super(mechanismId, configuration.getTextOrder());
        this.hint = configuration.getTextHint();
        this.label = configuration.getTextLabel();
        this.maxLength = configuration.getTextMaxLength();
        this.minLength = configuration.getTextMinLength();
    }

    @Override
    public boolean isValid(List<String> errorMessage) {
        return (getMinLength() < text.length() || text.length() > maxLength);
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
