package ch.uzh.supersede.feedbacklibrary.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public abstract class AbstractFeedbackPart implements Serializable {

    @Expose
    @SerializedName("mechanismId")
    private long feedbackPartId;
    private int order;

    public AbstractFeedbackPart() {
    }

    public AbstractFeedbackPart(long feedbackPartId, int order) {
        this.feedbackPartId = feedbackPartId;
        this.order = order;
    }

    public abstract boolean isValid(List<String> errorMessage);

    public long getFeedbackPartId() {
        return feedbackPartId;
    }

    public int getOrder() {
        return order;
    }
}
