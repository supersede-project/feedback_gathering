package ch.fhnw.cere.repository.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Entity
public class ScreenshotFeedback extends FileFeedback {

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

    private long mechanismId;

    @OneToMany(mappedBy = "screenshotFeedback", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<TextAnnotation> textAnnotations;

    @Override
    public String toString() {
        return String.format(
                "ScreenshotFeedback[id=%d, feedbackId='%d', mechanismId='%d', path='%s', fileExtension='%s']",
                id, feedback.getId(), mechanismId, path, fileExtension);
    }

    public ScreenshotFeedback() {
    }

    public ScreenshotFeedback(Feedback feedback, long mechanismId, List<TextAnnotation> textAnnotations) {
        this.feedback = feedback;
        this.mechanismId = mechanismId;
        this.textAnnotations = textAnnotations;
    }

    public ScreenshotFeedback(String path, int size, String part, String fileExtension, Feedback feedback, long mechanismId, List<TextAnnotation> textAnnotations) {
        super(path, size, part, fileExtension);
        this.feedback = feedback;
        this.mechanismId = mechanismId;
        this.textAnnotations = textAnnotations;
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

    public List<TextAnnotation> getTextAnnotations() {
        return textAnnotations;
    }

    public void setTextAnnotations(List<TextAnnotation> textAnnotations) {
        this.textAnnotations = textAnnotations;
    }
}
