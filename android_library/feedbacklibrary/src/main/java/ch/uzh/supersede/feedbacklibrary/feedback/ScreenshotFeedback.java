package ch.uzh.supersede.feedbacklibrary.feedback;

import com.google.gson.annotations.Expose;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.uzh.supersede.feedbacklibrary.models.ScreenshotMechanism;

public class ScreenshotFeedback extends AbstractPartFeedback implements Serializable {
    private String imagePath;
    @Expose
    private List<HashMap<String, Object>> textAnnotations;

    public ScreenshotFeedback(ScreenshotMechanism screenshotMechanism, int partId) {
        super(screenshotMechanism, screenshotMechanism.getImagePath(), partId);
        initScreenshotFeedback(screenshotMechanism);
    }

    @Override
    public String getPartString() {
        return "screenshot";
    }

    private void initScreenshotFeedback(ScreenshotMechanism screenshotMechanism) {
        imagePath = screenshotMechanism.getImagePath();
        if (screenshotMechanism.getAllTextAnnotationTexts() != null) {
            for (Map.Entry<Integer, String> textAnnotation : screenshotMechanism.getAllTextAnnotationTexts().entrySet()) {
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

    public String getFileName() {
        return new File(imagePath).getName();
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public List<HashMap<String, Object>> getTextAnnotations() {
        return textAnnotations;
    }

    public void setTextAnnotations(List<HashMap<String, Object>> textAnnotations) {
        this.textAnnotations = textAnnotations;
    }
}
