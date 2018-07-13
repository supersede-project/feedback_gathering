package ch.uzh.supersede.feedbacklibrary.models;

import java.util.ArrayList;
import java.util.List;

public class LabelFeedback extends AbstractFeedbackPart {
    private List<String> selectedLabels = new ArrayList<>();
    private List<String> labels = new ArrayList<>();

    public LabelFeedback() {
    }

    public List<String> getLabels() {
        return labels;
    }

    public List<String> getSelectedLabels() {
        return selectedLabels;
    }
}
