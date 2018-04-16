package ch.uzh.supersede.feedbacklibrary.models;

import android.graphics.Typeface;

import java.util.List;
import java.util.Map;

import ch.uzh.supersede.feedbacklibrary.configurations.MechanismConfigurationItem;
import ch.uzh.supersede.feedbacklibrary.utils.Utils;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.MechanismConstants.TEXT_TYPE;

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
    private String inputText = null;

    public TextMechanism(MechanismConfigurationItem item) {
        super(TEXT_TYPE, item);
        initTextMechanism(item);
    }

    private void initTextMechanism(MechanismConfigurationItem item) {
        for (Map<String, Object> param : item.getParameters()) {
            String key = (String) param.get("key");
            Object value = param.get("value");

            if (key == null) {
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

    @Override
    public boolean isValid(List<String> errorMessage) {
        if (isMandatory() && inputText.length() <= 0) {
            errorMessage.add(mandatoryReminder);
            return false;
        }

        if (maxLength != null && inputText != null && inputText.length() > maxLength) {
            errorMessage.add("Text has " + inputText.length() + " characters. Maximum allowed characters are " + maxLength);
            return false;
        }

        return true;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getInputTextFontColor() {
        return inputTextFontColor;
    }

    public void setInputTextFontColor(String inputTextFontColor) {
        this.inputTextFontColor = inputTextFontColor;
    }

    public Float getInputTextFontSize() {
        return inputTextFontSize;
    }

    public void setInputTextFontSize(Float inputTextFontSize) {
        this.inputTextFontSize = inputTextFontSize;
    }

    public Integer getInputTextFontType() {
        return inputTextFontType;
    }

    public void setInputTextFontType(Integer inputTextFontType) {
        this.inputTextFontType = inputTextFontType;
    }

    public String getInputTextAlignment() {
        return inputTextAlignment;
    }

    public void setInputTextAlignment(String inputTextAlignment) {
        this.inputTextAlignment = inputTextAlignment;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public boolean isMaxLengthVisible() {
        return maxLengthVisible;
    }

    public void setMaxLengthVisible(boolean maxLengthVisible) {
        this.maxLengthVisible = maxLengthVisible;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public String getMandatoryReminder() {
        return mandatoryReminder;
    }

    public void setMandatoryReminder(String mandatoryReminder) {
        this.mandatoryReminder = mandatoryReminder;
    }

    public boolean isTextLengthVisible() {
        return textLengthVisible;
    }

    public void setTextLengthVisible(boolean textLengthVisible) {
        this.textLengthVisible = textLengthVisible;
    }

    public String getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }
}
