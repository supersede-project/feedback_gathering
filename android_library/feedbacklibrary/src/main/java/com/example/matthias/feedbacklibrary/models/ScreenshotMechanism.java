package com.example.matthias.feedbacklibrary.models;

import com.example.matthias.feedbacklibrary.configurations.MechanismConfigurationItem;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Screenshot mechanism model
 */
public class ScreenshotMechanism extends Mechanism implements Serializable {
    private String title;
    private String defaultPicture;

    private String imagePath;
    private HashMap<Integer, String> allTextAnnotations = null;

    public ScreenshotMechanism(MechanismConfigurationItem item) {
        super(SCREENSHOT_TYPE, item);
        initScreenshotMechanism(item);
    }

    public HashMap<Integer, String> getAllTextAnnotations() {
        return allTextAnnotations;
    }

    public String getDefaultPicture() {
        return defaultPicture;
    }

    public String getImagePath() {
        return imagePath;
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

    public void setTitle(String title) {
        this.title = title;
    }
}
