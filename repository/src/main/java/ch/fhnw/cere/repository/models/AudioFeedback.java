package ch.fhnw.cere.repository.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;


@Entity
public class AudioFeedback extends FileFeedback {

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

    private long mechanismId;
    private int duration;

    @Override
    public String toString() {
        return String.format(
                "AudioFeedback[id=%d, feedbackId='%d']",
                id, feedback.getId());
    }

    public AudioFeedback() {
    }

    public AudioFeedback(Feedback feedback, long mechanismId, int duration) {
        this.feedback = feedback;
        this.mechanismId = mechanismId;
        this.duration = duration;
    }

    public AudioFeedback(String path, int size, String part, String fileExtension, Feedback feedback, long mechanismId, int duration) {
        super(path, size, part, fileExtension);
        this.feedback = feedback;
        this.mechanismId = mechanismId;
        this.duration = duration;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
