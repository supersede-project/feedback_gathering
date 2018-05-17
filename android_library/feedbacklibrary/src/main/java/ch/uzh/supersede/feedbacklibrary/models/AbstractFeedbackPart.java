package ch.uzh.supersede.feedbacklibrary.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

public abstract class AbstractFeedbackPart implements Serializable {
    @Expose
    private long mechanismId;
    private int order;

    public AbstractFeedbackPart() {
    }

    public AbstractFeedbackPart(long mechanismId, int order) {
        this.mechanismId = mechanismId;
        this.order = order;
    }

    public abstract boolean isValid(List<String> errorMessage);

    public long getMechanismId() {
        return mechanismId;
    }

    public int getOrder() {
        return order;
    }
}
