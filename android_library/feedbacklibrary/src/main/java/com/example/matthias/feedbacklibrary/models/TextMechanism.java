package com.example.matthias.feedbacklibrary.models;

import android.graphics.Color;
import android.graphics.Typeface;

import com.example.matthias.feedbacklibrary.configurations.MechanismConfigurationItem;
import com.example.matthias.feedbacklibrary.utils.Utils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Text mechanism model
 */
public class TextMechanism extends Mechanism implements Serializable {
    private String title = "";
    private String hint = "";
    private String label = "";
    // Font color of the input text
    private Integer inputTextFontColor = null;
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
    // TODO: Positive or negative validation? What kind of RE? Also an error message with manual text possible?
    // Regex used for validation
    private String validationRegex = ".";
    private String validationRegexErrorMessage = "Default regex validation text!";
    // The text entered by the user
    private String inputText = null;

    public TextMechanism(MechanismConfigurationItem item) {
        super(TEXT_TYPE, item);
        initTextMechanism(item);
    }

    /**
     * This method returns the hint.
     *
     * @return the hint, or the empty string by default
     */
    public String getHint() {
        return hint;
    }

    public String getInputText() {
        return inputText;
    }

    /**
     * This method returns the alignment of the input text.
     *
     * @return the alignment, or null by default
     */
    public String getInputTextAlignment() {
        return inputTextAlignment;
    }

    /**
     * This method returns the font color of the input text.
     *
     * @return the font color, or null by default
     */
    public Integer getInputTextFontColor() {
        return inputTextFontColor;
    }

    /**
     * This method returns the font size of the input text.
     *
     * @return the font size, or null by default
     */
    public Float getInputTextFontSize() {
        return inputTextFontSize;
    }

    /**
     * This method returns the font type of the input text.
     *
     * @return the font type, or null by default
     */
    public Integer getInputTextFontType() {
        return inputTextFontType;
    }

    public String getLabel() {
        return label;
    }

    public String getMandatoryReminder() {
        return mandatoryReminder;
    }

    /**
     * This method returns the maximum length of the input text.
     *
     * @return the maximum length, or null by default
     */
    public Integer getMaxLength() {
        return maxLength;
    }

    public String getTitle() {
        return title;
    }

    public String getValidationRegex() {
        return validationRegex;
    }

    private void initTextMechanism(MechanismConfigurationItem item) {
        for (Map<String, Object> param : item.getParameters()) {
            String key = (String) param.get("key");
            // Title
            if (key.equals("title")) {
                setTitle((String) param.get("value"));
            }
            // Hint
            if (key.equals("hint")) {
                setHint((String) param.get("value"));
            }
            // Label
            if (key.equals("label")) {
                setLabel((String) param.get("value"));
            }
            // Font color
            if (key.equals("textareaFontColor")) {
                setInputTextFontColor(Color.parseColor((String) param.get("value")));
            }
            // Font size
            if (key.equals("fieldFontSize")) {
                setInputTextFontSize(((Double) param.get("value")).floatValue());
            }
            // Font type
            if (key.equals("fieldFontType")) {
                String fontType = (String) param.get("value");
                switch (fontType) {
                    case "italic":
                        setInputTextFontType(Typeface.ITALIC);
                        break;
                    case "bold":
                        setInputTextFontType(Typeface.BOLD);
                        break;
                    case "normal":
                        // No break, because font type normal represents the default case
                    default:
                        setInputTextFontType(Typeface.NORMAL);
                        break;
                }
            }
            // Alignment
            if (key.equals("fieldTextAlignment")) {
                String alignment = (String) param.get("value");
                switch (alignment) {
                    case "center":
                        setInputTextAlignment("center");
                        break;
                    case "right":
                        setInputTextAlignment("right");
                        break;
                    case "left":
                        setInputTextAlignment("left");
                    default:
                        break;
                }
            }
            // Maximum length
            if (key.equals("maxLength")) {
                setMaxLength(((Double) param.get("value")).intValue());
            }
            if (key.equals("maxLengthVisible")) {
                setMaxLengthVisible(Utils.intToBool(((Double) param.get("value")).intValue()));
            }
            if (key.equals("mandatory")) {
                setMandatory(Utils.intToBool(((Double) param.get("value")).intValue()));
                // If TI 11 is set, TI 11.1 should always be activated, i.e., the user always needs a reminder for a mandatory field
            }
            if (key.equals("mandatoryReminder")) {
                setMandatoryReminder((String) param.get("value"));
            }
            if (key.equals("validationRegex")) {
                setValidationRegex((String) param.get("value"));
            }
        }
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public boolean isMaxLengthVisible() {
        return maxLengthVisible;
    }

    @Override
    public boolean isValid(List<String> errorMessage) {
        String pattern;
        if (validationRegex.equals(".")) {
            // Default value, i.e., every character is allowed
            pattern = ".*";
        } else {
            pattern = validationRegex;
        }

        if (!inputText.matches(pattern)) {
            errorMessage.add(validationRegexErrorMessage);
            return false;
        }

        if (isMandatory() && !(inputText.length() > 0)) {
            errorMessage.add(mandatoryReminder);
            return false;
        }

        if (maxLength != null) {
            if (inputText.length() > maxLength) {
                errorMessage.add("Text has " + inputText.length() + " characters. Maximum allowed characters are " + maxLength);
                return false;
            }
        }

        return true;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public void setInputTextAlignment(String inputTextAlignment) {
        this.inputTextAlignment = inputTextAlignment;
    }

    public void setInputTextFontColor(int inputTextFontColor) {
        this.inputTextFontColor = inputTextFontColor;
    }

    public void setInputTextFontSize(float inputTextFontSize) {
        this.inputTextFontSize = inputTextFontSize;
    }

    public void setInputTextFontType(int inputTextFontType) {
        this.inputTextFontType = inputTextFontType;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public void setMandatoryReminder(String mandatoryReminder) {
        this.mandatoryReminder = mandatoryReminder;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public void setMaxLengthVisible(boolean maxLengthVisible) {
        this.maxLengthVisible = maxLengthVisible;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setValidationRegex(String validationRegex) {
        this.validationRegex = validationRegex;
    }
}
