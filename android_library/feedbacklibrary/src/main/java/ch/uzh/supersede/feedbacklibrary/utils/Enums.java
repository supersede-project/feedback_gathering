package ch.uzh.supersede.feedbacklibrary.utils;

import android.graphics.Color;

public class Enums {

    @SuppressWarnings("squid:UnusedPrivateMethod")
    public enum FEEDBACK_STATUS {
        OPEN("Open", Color.rgb(0, 150, 255)),
        IN_PROGRESS("In Progress", Color.rgb(222, 222, 0)),
        REJECTED("Rejected", Color.rgb(255, 150, 0)),
        DUPLICATE("Duplicate", Color.rgb(150, 150, 150)),
        CLOSED("Closed", Color.rgb(0, 222, 100));

        FEEDBACK_STATUS(String label, int color) {
            this.label = label;
            this.color = color;
        }

        private int color;
        private String label;

        public int getColor() {
            return color;
        }

        public String getLabel() {
            return label;
        }
    }
}
