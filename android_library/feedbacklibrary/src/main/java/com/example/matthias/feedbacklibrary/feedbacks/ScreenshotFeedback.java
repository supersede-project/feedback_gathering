package com.example.matthias.feedbacklibrary.feedbacks;

import com.example.matthias.feedbacklibrary.models.ScreenshotMechanism;
import com.google.gson.annotations.Expose;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Screenshot feedback.
 */
public class ScreenshotFeedback extends PartFeedback implements Serializable {
    private String fileName;
    private String imagePath;
    @Expose
    private List<HashMap<String, Object>> textAnnotations;

    public ScreenshotFeedback(ScreenshotMechanism screenshotMechanism, int partId) {
        super(screenshotMechanism, screenshotMechanism.getImagePath(), partId);
        initScreenshotFeedback(screenshotMechanism);
    }

    public String getFileName() {
        return new File(imagePath).getName();
    }

    public String getImagePath() {
        return imagePath;
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

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
