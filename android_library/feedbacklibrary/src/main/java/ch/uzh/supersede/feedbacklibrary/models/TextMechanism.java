package ch.uzh.supersede.feedbacklibrary.models;

import com.google.gson.annotations.Expose;

import java.util.List;

public class TextMechanism extends AbstractMechanism {
    private String hint = "";
    private String label = "";
    // Font color of the input text
    private String inputTextFontColor = null;
    // Font size of the input text
    private Float inputTextFontSize = null;
    // Font type of the input text (normal, italic or bold)
    private Integer inputTextFontType = null;
    // Alignment of the input text (left, right, center)
    private String inputTextAlignment = null;
    // Maximum length of the input text
    private Integer maxLength = null;
    private boolean maxLengthVisible = false;
    private boolean mandatory = false;
    private String mandatoryReminder = "Default mandatory reminder text!";
    private boolean textLengthVisible = true;
    // The text entered by the user
    @Expose
    private String text = null;

    public TextMechanism(long mechanismId, int order) {
        super(mechanismId, order);
        //TODO [jfo]: title, hint, label, textareaFontColor, fieldFontSize, fieldFontType(italic, bold, normal), fieldTextAlignment(center, right, left), maxLength, maxLengthVisible,
        // textLengthVisible, mandatory, mandatoryReminder
    }

    @Override
    public boolean isValid(List<String> errorMessage) {
        if (isMandatory() && text.length() <= 0) {
            errorMessage.add(getMandatoryReminder());
            return false;
        }

        if (maxLength != null && text != null && text.length() > maxLength) {
            errorMessage.add("Text has " + text.length() + " characters. Maximum allowed characters are " + maxLength);
            return false;
        }

        return true;
    }

    public String getHint() {
        return hint;
    }

    public String getLabel() {
        return label;
    }

    public String getInputTextFontColor() {
        return inputTextFontColor;
    }

    public Float getInputTextFontSize() {
        return inputTextFontSize;
    }

    public Integer getInputTextFontType() {
        return inputTextFontType;
    }

    public String getInputTextAlignment() {
        return inputTextAlignment;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public boolean isMaxLengthVisible() {
        return maxLengthVisible;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public String getMandatoryReminder() {
        return mandatoryReminder;
    }

    public boolean isTextLengthVisible() {
        return textLengthVisible;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
