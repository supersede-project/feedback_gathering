package ch.uzh.supersede.feedbacklibrary.models;

import android.util.Log;

import ch.uzh.supersede.feedbacklibrary.configurations.MechanismConfigurationItem;
import ch.uzh.supersede.feedbacklibrary.utils.Utils;

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

    /**
     * This method returns all text annotation texts.
     *
     * @return all text annotation texts or an empty hash map if there are no text annotation texts
     */
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

    /**
     * This method returns all text annotations.
     *
     * @return all text annotations
     */
    public HashMap<Integer, String> getAllTextAnnotations() {
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

    /**
     * This method sets all the text annotations.
     *
     * @param allTextAnnotations all text annotations
     */
    public void setAllTextAnnotations(HashMap<Integer, String> allTextAnnotations) {
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
