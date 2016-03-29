package com.example.matthias.feedbacklibrary.models;

import java.io.Serializable;

/**
 * Created by Matthias on 19.03.2016.
 */
public class TextMechanism extends Mechanism implements Serializable {
    private static final String TEXT_TYPE = "TEXT_MECHANISM";

    public TextMechanism(boolean canBeActivated, boolean isActive, int order) {
        super(TEXT_TYPE, canBeActivated, isActive, order);
    }
}
