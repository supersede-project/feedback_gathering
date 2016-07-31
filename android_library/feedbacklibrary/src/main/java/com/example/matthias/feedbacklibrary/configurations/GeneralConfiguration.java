package com.example.matthias.feedbacklibrary.configurations;

import com.example.matthias.feedbacklibrary.utils.Utils;

import java.util.List;
import java.util.Map;

/**
 * General configuration.
 */
public class GeneralConfiguration {
    private String language;
    private boolean review;

    public GeneralConfiguration(List<Map<String, Object>> items) {
        initGeneralConfiguration(items);
    }

    public String getLanguage() {
        return language;
    }

    private void initGeneralConfiguration(List<Map<String, Object>> items) {
        for (Map<String, Object> param : items) {
            String key = (String) param.get("key");
            // Language
            if (key.equals("lang")) {
                setLanguage((String) param.get("value"));
            }
            // Review
            if (key.equals("review")) {
                setReview(Utils.intToBool(((Double) param.get("value")).intValue()));
            }
        }
    }

    public boolean isReview() {
        return review;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setReview(boolean review) {
        this.review = review;
    }
}
