package ch.uzh.supersede.feedbacklibrary.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public abstract class AbstractFeedbackPart implements Serializable {

    @SerializedName("mechanismId")
    private long feedbackPartId;
    private long id;
    private int order;

    public AbstractFeedbackPart() {
    }

    public AbstractFeedbackPart(int order) {
        this.order = order;
    }

    public boolean isValid(List<String> errorMessages){
        return true;
    }

    public long getFeedbackPartId() {
        return feedbackPartId;
    }

    public long getId() {
        return id;
    }

    public int getOrder() {
        return order;
    }
}
