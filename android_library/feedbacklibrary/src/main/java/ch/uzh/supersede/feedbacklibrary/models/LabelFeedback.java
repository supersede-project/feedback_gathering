package ch.uzh.supersede.feedbacklibrary.models;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.supersede.feedbacklibrary.beans.LocalConfigurationBean;

public class LabelFeedback extends AbstractFeedbackPart {
    private List<String> selectedLabels = new ArrayList<>();
    private int maxCount;
    private int minCount;
    @Expose
    private List<String> labels = new ArrayList<>();

    public LabelFeedback(long labelFeedbackId, LocalConfigurationBean configuration) {
        super(labelFeedbackId, configuration.getLabelOrder());
        this.maxCount = configuration.getLabelMaxCount();
        this.minCount = configuration.getLabelMinCount();
        labels.addAll(selectedLabels);
    }

    @Override
    public boolean isValid(List<String> errorMessage) {
        return (getMinCount() > 0 && getSelectedLabels().isEmpty());
    }

    public List<String> getLabels() {
        return labels;
    }

    public List<String> getSelectedLabels() {
        return selectedLabels;
    }

    public void setSelectedLabels(List<String> selectedLabels) {
        this.selectedLabels = selectedLabels;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public int getMinCount() {
        return minCount;
    }
}
