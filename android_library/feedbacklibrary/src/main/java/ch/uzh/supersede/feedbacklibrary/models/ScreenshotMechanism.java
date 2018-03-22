package ch.uzh.supersede.feedbacklibrary.models;

import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.uzh.supersede.feedbacklibrary.configurations.MechanismConfigurationItem;

import static ch.uzh.supersede.feedbacklibrary.utils.Constants.ScreenshotConstants.*;

/**
 * Screenshot mechanism model
 */
public class ScreenshotMechanism extends Mechanism {
    private String title;
    private String defaultPicture;
    private int maxNumberTextAnnotation = 0;
    private String imagePath;
    private Map<Integer, String> allTextAnnotations = null;

    public ScreenshotMechanism(MechanismConfigurationItem item) {
        super(SCREENSHOT_TYPE, item);
        initScreenshotMechanism(item);
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
                String split[] = entry.getValue().split(SEPARATOR);
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

    /**
     * This method returns all text annotations.
     *
     * @return all text annotations
     */
    public Map<Integer, String> getAllTextAnnotations() {
        return allTextAnnotations;
    }

    /**
     * This method returns the default picture.
     *
     * @return the default picture
     */
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

    /**
     * This method returns the maximum allowed number of text annotations.
     *
     * @return the maximum number of text annotations or 0 if no maximum is provided
     */
    public int getMaxNumberTextAnnotation() {
        return maxNumberTextAnnotation;
    }

    /**
     * This method returns the title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    private void initScreenshotMechanism(MechanismConfigurationItem item) {
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
            // Default picture
            else if (key.equals("defaultPicture")) {
                setDefaultPicture((String) value);
            }
            // Maximum number of text annotations
            else if (key.equals("maxTextAnnotation")) {
                setMaxNumberTextAnnotation(Integer.valueOf((String) value));
            }
        }
    }

    @Override
    public boolean isValid(List<String> errorMessage) {
        return true;
    }

    /**
     * This method sets all the text annotations.
     *
     * @param allTextAnnotations all text annotations
     */
    public void setAllTextAnnotations(Map<Integer, String> allTextAnnotations) {
        this.allTextAnnotations = allTextAnnotations;
    }

    /**
     * This method sets the default picture.
     *
     * @param defaultPicture the default picture
     */
    public void setDefaultPicture(String defaultPicture) {
        this.defaultPicture = defaultPicture;
    }

    /**
     * This method sets the path of the annotated image.
     *
     * @param imagePath the image path
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    /**
     * This method sets the maximum allowed number of text annotations.
     *
     * @param maxNumberTextAnnotation the maximum number of text annotations
     */
    public void setMaxNumberTextAnnotation(int maxNumberTextAnnotation) {
        this.maxNumberTextAnnotation = maxNumberTextAnnotation;
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
