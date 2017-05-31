package ch.fhnw.cere.repository.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;


@Entity
public class AttachmentFeedback extends FileFeedback {

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

    private long mechanismId;

    @Override
    public String toString() {
        return String.format(
                "AttachmentFeedback[id=%d, feedbackId='%d', mechanismId='%d', path='%s']",
                id, feedback.getId(), mechanismId, path);
    }

    public AttachmentFeedback() {
    }

    public AttachmentFeedback(Feedback feedback, long mechanismId) {
        this.feedback = feedback;
        this.mechanismId = mechanismId;
    }

    public AttachmentFeedback(String part, Feedback feedback, long mechanismId) {
        super(null, part, null);
        this.feedback = feedback;
        this.mechanismId = mechanismId;
    }

    public AttachmentFeedback(String path, int size, String part, String fileExtension, Feedback feedback, long mechanismId) {
        super(path, size, part, fileExtension);
        this.feedback = feedback;
        this.mechanismId = mechanismId;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public long getMechanismId() {
        return mechanismId;
    }

    public void setMechanismId(long mechanismId) {
        this.mechanismId = mechanismId;
    }
}
