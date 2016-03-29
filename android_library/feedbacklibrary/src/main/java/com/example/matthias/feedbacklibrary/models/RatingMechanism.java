package com.example.matthias.feedbacklibrary.models;

import java.io.Serializable;

/**
 * Created by Matthias on 19.03.2016.
 */
public class RatingMechanism extends Mechanism implements Serializable {
    private static final String RATING_TYPE = "RATING_MECHANISM";

    public RatingMechanism(boolean canBeActivated, boolean isActive, int order) {
        super(RATING_TYPE, canBeActivated, isActive, order);
    }
}
