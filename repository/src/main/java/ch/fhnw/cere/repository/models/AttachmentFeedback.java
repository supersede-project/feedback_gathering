package ch.fhnw.cere.repository.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;


@Entity
public class AttachmentFeedback implements FileFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected long id;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

    private long mechanismId;

    private String path;
    private  int size;
    @Transient
    private String part;
    private String fileExtension;


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
        this.part = part;
        this.feedback = feedback;
        this.mechanismId = mechanismId;
    }

    public AttachmentFeedback(String path, int size, String part, String fileExtension, Feedback feedback, long mechanismId) {
        this.path = path;
        this.size = size;
        this.part = part;
        this.fileExtension = fileExtension;
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
}
