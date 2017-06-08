package ch.fhnw.cere.repository.models;


import ch.fhnw.cere.repository.models.orchestrator.Mechanism;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;


@Entity
public class AudioFeedback implements FileFeedback, MechanismFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

    private long mechanismId;
    private int duration;

    private String path;
    private  int size;
    @Transient
    private String part;
    private String fileExtension;

    @JsonIgnore
    @Transient
    private Mechanism mechanism;

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
        this.path = path;
        this.size = size;
        this.part = part;
        this.fileExtension = fileExtension;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

    public int getSize() {
        return size;
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    @Override
    public Mechanism getMechanism() {
        return mechanism;
    }

    @Override
    public void setMechanism(Mechanism mechanism) {
        this.mechanism = mechanism;
    }
}
