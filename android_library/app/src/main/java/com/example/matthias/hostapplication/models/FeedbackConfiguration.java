package com.example.matthias.hostapplication.models;

import java.util.List;
import java.util.Map;

/**
 * Created by Matthias on 22.03.2016.
 */
public class FeedbackConfiguration {
    private String type;
    private List<Map<String, Object>> parameters;

    public FeedbackConfiguration() {}

    public List<Map<String, Object>> getParameters() {
        return parameters;
    }
    public void setParameters(List<Map<String, Object>> parameters) {
        this.parameters = parameters;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
