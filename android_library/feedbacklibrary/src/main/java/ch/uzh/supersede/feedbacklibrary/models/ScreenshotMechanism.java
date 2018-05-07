package ch.uzh.supersede.feedbacklibrary.models;

import android.util.Log;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.uzh.supersede.feedbacklibrary.configurations.MechanismConfigurationItem;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.*;

public class ScreenshotMechanism extends AbstractPartMechanism {
    private String defaultPicture;
    private int maxNumberTextAnnotation = 0;
    private String imagePath;
    private Map<Integer, String> allTextAnnotations = null;

    @Expose
    private List<HashMap<String, Object>> textAnnotations;

    public ScreenshotMechanism(MechanismConfigurationItem item) {
        super(SCREENSHOT_TYPE, item, 0); //TODO [jfo] set part id
        initScreenshotFeedback();
    }

    @Override
    public void handleMechanismParameter(String key, Object value) {
        super.handleMechanismParameter(key, value);
        if (key.equals("defaultPicture")) {
            setDefaultPicture((String) value);
        } else if (key.equals("maxTextAnnotation")) {
            setMaxNumberTextAnnotation(Integer.parseInt((String) value));
        }
    }

    /**
     * This method returns all text annotation texts.
     *
     * @return all text annotation texts or an empty hash map if there are no text annotation texts
     */
    public Map<Integer, String> getAllTextAnnotationTexts() {
        if (getAllTextAnnotations() != null) {
            Map<Integer, String> returnValue = new HashMap<>();
            for (Map.Entry<Integer, String> entry : getAllTextAnnotations().entrySet()) {
                String[] split = entry.getValue().split(SEPARATOR);
                if (split.length > 0) {
                    returnValue.put(entry.getKey(), split[0]);
                } else {
                    Log.e("ScreenshotMechanism", "Failed to split. split.length smaller than 1");
                }
            }
            return returnValue;
        }
        return null;
    }

    private void initScreenshotFeedback() {
        if (getAllTextAnnotationTexts() != null) {
            for (Map.Entry<Integer, String> textAnnotation : getAllTextAnnotationTexts().entrySet()) {
                if (textAnnotations == null) {
                    textAnnotations = new ArrayList<>();
                }
                HashMap<String, Object> values = new HashMap<>();
                values.put("text", textAnnotation.getValue());
                values.put("referenceNumber", textAnnotation.getKey());
                textAnnotations.add(values);
            }
        }
    }

    @Override
    public boolean isValid(List<String> errorMessage) {
        return true;
    }

    @Override
    public String getPartString() {
        return "screenshot";
    }

    @Override
    public String getFilePath() {
        return imagePath;
    }

    public String getDefaultPicture() {
        return defaultPicture;
    }

    public void setDefaultPicture(String defaultPicture) {
        this.defaultPicture = defaultPicture;
    }

    public int getMaxNumberTextAnnotation() {
        return maxNumberTextAnnotation;
    }

    public void setMaxNumberTextAnnotation(int maxNumberTextAnnotation) {
        this.maxNumberTextAnnotation = maxNumberTextAnnotation;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Map<Integer, String> getAllTextAnnotations() {
        return allTextAnnotations;
    }

    public void setAllTextAnnotations(Map<Integer, String> allTextAnnotations) {
        this.allTextAnnotations = allTextAnnotations;
    }

    public List<HashMap<String, Object>> getTextAnnotations() {
        return textAnnotations;
    }

    public void setTextAnnotations(List<HashMap<String, Object>> textAnnotations) {
        this.textAnnotations = textAnnotations;
    }
}
