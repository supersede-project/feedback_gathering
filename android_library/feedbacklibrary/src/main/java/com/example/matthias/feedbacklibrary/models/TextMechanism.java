package com.example.matthias.feedbacklibrary.models;

import java.io.Serializable;

/**
 * Created by Matthias on 19.03.2016.
 */
public class TextMechanism extends Mechanism implements Serializable {
    private static final String TEXT_TYPE = "TEXT_MECHANISM";

    private String title;
    private String hint;
    private int maxLength;

    public TextMechanism(boolean canBeActivated, boolean isActive, int order) {
        super(TEXT_TYPE, canBeActivated, isActive, order);
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
