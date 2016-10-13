package com.example.matthias.feedbacklibrary.models;

import android.util.Log;

import com.example.matthias.feedbacklibrary.configurations.MechanismConfigurationItem;
import com.example.matthias.feedbacklibrary.utils.Utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Screenshot mechanism model
 */
public class ScreenshotMechanism extends Mechanism {
    private String title;
    private String defaultPicture;
    private int maxNumberTextAnnotation = 0;
    private String imagePath;
    private HashMap<Integer, String> allTextAnnotations = null;

    public ScreenshotMechanism(MechanismConfigurationItem item) {
        super(SCREENSHOT_TYPE, item);
        initScreenshotMechanism(item);
    }

    public HashMap<Integer, String> getAllTextAnnotationTexts() {
        if (getAllTextAnnotations() != null) {
            HashMap<Integer, String> returnValue = new HashMap<>();
            for (Map.Entry<Integer, String> entry : getAllTextAnnotations().entrySet()) {
                String split[] = entry.getValue().split(Utils.SEPARATOR);
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

    public HashMap<Integer, String> getAllTextAnnotations() {
        return allTextAnnotations;
    }

    public String getDefaultPicture() {
        return defaultPicture;
    }

    /**
     * This method returns the path of the annotated image.
     *
     * @return the image path or null if there is no image
     */
    public String getImagePath() {
        return imagePath;
    }

    public int getMaxNumberTextAnnotation() {
        return maxNumberTextAnnotation;
    }

    public String getTitle() {
        return title;
    }

    private void initScreenshotMechanism(MechanismConfigurationItem item) {
        for (Map<String, Object> param : item.getParameters()) {
            String key = (String) param.get("key");
            // Title
            if (key.equals("title")) {
                setTitle((String) param.get("value"));
            }
            // Default picture
            if (key.equals("defaultPicture")) {
                setDefaultPicture((String) param.get("value"));
            }
            // Maximum number of text annotations
            if (key.equals("maxTextAnnotation")) {
                setMaxNumberTextAnnotation(((Double) param.get("value")).intValue());
            }
        }
    }

    @Override
    public boolean isValid(List<String> errorMessage) {
        return true;
    }

    public void setAllTextAnnotations(HashMap<Integer, String> allTextAnnotations) {
        this.allTextAnnotations = allTextAnnotations;
    }

    public void setDefaultPicture(String defaultPicture) {
        this.defaultPicture = defaultPicture;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setMaxNumberTextAnnotation(int maxNumberTextAnnotation) {
        this.maxNumberTextAnnotation = maxNumberTextAnnotation;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
