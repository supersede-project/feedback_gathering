package ch.uzh.supersede.feedbacklibrary.models;

import android.graphics.Typeface;

import java.util.List;
import java.util.Map;

import ch.uzh.supersede.feedbacklibrary.configurations.MechanismConfigurationItem;
import ch.uzh.supersede.feedbacklibrary.utils.Utils;

/**
 * Text mechanism model
 */
public class TextMechanism extends Mechanism {
    private String title;
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

    /**
     * This method returns the input text.
     *
     * @return the input text, or null by default
     */
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
    public String getInputTextFontColor() {
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

    /**
     * This method returns the label.
     *
     * @return the label, or the empty string by default
     */
    public String getLabel() {
        return label;
    }

    /**
     * This method returns the mandatory reminder.
     *
     * @return the mandatory reminder
     */
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

    /**
     * This method returns the title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    private void initTextMechanism(MechanismConfigurationItem item) {
        for (Map<String, Object> param : item.getParameters()) {
            String key = (String) param.get("key");
            Object value = param.get("value");

            if (key == null){
                return;
            }
            // Title
            if (key.equals("title")) {
                setTitle((String) value);
            }
            // Hint
            if (key.equals("hint")) {
                setHint((String) value);
            }
            // Label
            if (key.equals("label")) {
                setLabel((String) value);
            }
            // Font color
            if (key.equals("textareaFontColor")) {
                setInputTextFontColor(((String) value));
            }
            // Font size
            if (key.equals("fieldFontSize")) {
                setInputTextFontSize(Float.parseFloat((String) value));
            }
            // Font type
            if (key.equals("fieldFontType")) {
                String fontType = (String) value;
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
                String alignment = (String) value;
                switch (alignment) {
                    case "center":
                        setInputTextAlignment("center");
                        break;
                    case "right":
                        setInputTextAlignment("right");
                        break;
                    case "left":
                        // No break, because text alignment left represents the default case
                    default:
                        setInputTextAlignment("left");
                        break;
                }
            }
            // Maximum length
            if (key.equals("maxLength")) {
                Double doubleValue = Double.parseDouble((String) value);
                setMaxLength(doubleValue.intValue());
            }
            if (key.equals("maxLengthVisible")) {
                if (value instanceof Boolean) {
                    setMaxLengthVisible((Boolean) value);
                } else {
                    Double doubleValue = Double.parseDouble((String) value);
                    setMaxLengthVisible(Utils.intToBool(doubleValue.intValue()));
                }
            }
            if (key.equals("textLengthVisible")) {
                if (value instanceof Boolean) {
                    setTextLengthVisible((Boolean) value);
                } else {
                    Double doubleValue = Double.parseDouble((String) value);
                    setTextLengthVisible(Utils.intToBool(doubleValue.intValue()));
                }
            }
            if (key.equals("mandatory")) {
                if (value instanceof Boolean) {
                    setMandatory((Boolean) value);
                } else {
                    Double doubleValue = Double.parseDouble((String) value);
                    setMandatory(Utils.intToBool(doubleValue.intValue()));
                }

                // If TI 11 is set, TI 11.1 should always be activated, i.e., the user always needs a reminder for a mandatory field
            }
            if (key.equals("mandatoryReminder")) {
                setMandatoryReminder((String) value);
            }
        }
    }

    /**
     * This method returns if entering a text is mandatory.
     *
     * @return true if it is mandatory, false otherwise
     */
    public boolean isMandatory() {
        return mandatory;
    }

    /**
     * This method returns if the maximum length of the input text is visible for the user.
     *
     * @return true if it is visible, false otherwise
     */
    public boolean isMaxLengthVisible() {
        return maxLengthVisible;
    }

    /**
     * This method returns if the length of the current input text is visible for the user.
     *
     * @return true if it is visible, false otherwise
     */
    public boolean isTextLengthVisible() {
        return textLengthVisible;
    }

    @Override
    public boolean isValid(List<String> errorMessage) {
        if (isMandatory() && inputText.length() <= 0) {
            errorMessage.add(mandatoryReminder);
            return false;
        }

        if (maxLength != null && inputText!= null && inputText.length() > maxLength) {
            errorMessage.add("Text has " + inputText.length() + " characters. Maximum allowed characters are " + maxLength);
            return false;
        }

        return true;
    }

    /**
     * This method sets the hint.
     *
     * @param hint the hint
     */
    public void setHint(String hint) {
        this.hint = hint;
    }

    /**
     * This method sets the input text.
     *
     * @param inputText the input text
     */
    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    /**
     * This method sets the alignment of the input text.
     *
     * @param inputTextAlignment the input text alignment
     */
    public void setInputTextAlignment(String inputTextAlignment) {
        this.inputTextAlignment = inputTextAlignment;
    }

    /**
     * This method sets the text color of the input text.
     *
     * @param inputTextFontColor the input text color
     */
    public void setInputTextFontColor(String inputTextFontColor) {
        this.inputTextFontColor = inputTextFontColor;
    }

    /**
     * This method sets the size of the input text.
     *
     * @param inputTextFontSize the input text size
     */
    public void setInputTextFontSize(float inputTextFontSize) {
        this.inputTextFontSize = inputTextFontSize;
    }

    /**
     * This method sets the font type of the input text.
     *
     * @param inputTextFontType the input text font type
     */
    public void setInputTextFontType(int inputTextFontType) {
        this.inputTextFontType = inputTextFontType;
    }

    /**
     * This method sets the label.
     *
     * @param label the label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * This method sets if entering a text is mandatory.
     *
     * @param mandatory true if it is mandatory, false otherwise
     */
    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    /**
     * This method sets the mandatory reminder.
     *
     * @param mandatoryReminder the mandatory reminder
     */
    public void setMandatoryReminder(String mandatoryReminder) {
        this.mandatoryReminder = mandatoryReminder;
    }

    /**
     * This method sets the maximum length of the input text.
     *
     * @param maxLength the maximum length
     */
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    /**
     * This method sets if the maximum length of the input text is visible for the user.
     *
     * @param maxLengthVisible true if it is visible, false otherwise
     */
    public void setMaxLengthVisible(boolean maxLengthVisible) {
        this.maxLengthVisible = maxLengthVisible;
    }

    /**
     * This method sets if the length of the current input text is visible for the user.
     *
     * @param textLengthVisible true if it is visible, false otherwise
     */
    public void setTextLengthVisible(boolean textLengthVisible) {
        this.textLengthVisible = textLengthVisible;
    }

    /**
     * This method sets the title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }
}
