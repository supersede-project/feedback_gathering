package ch.fhnw.cere.repository.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.stream.Collectors;


@Entity
public class TextAnnotation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "screenshot_feedback_id")
    private ScreenshotFeedback screenshotFeedback;
    private Integer referenceNumber;
    private String text;

    public TextAnnotation() {
    }

    public TextAnnotation(ScreenshotFeedback screenshotFeedback, Integer referenceNumber, String text) {
        this.screenshotFeedback = screenshotFeedback;
        this.referenceNumber = referenceNumber;
        this.text = text;
    }

    @Override
    public String toString() {
        return String.format(
                "TextAnnotation[id=%d, screenshotFeedbackId='%d', referenceNumber='%d', text='%s']",
                id, screenshotFeedback.getId(), referenceNumber, text);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ScreenshotFeedback getScreenshotFeedback() {
        return screenshotFeedback;
    }

    public void setScreenshotFeedback(ScreenshotFeedback screenshotFeedback) {
        this.screenshotFeedback = screenshotFeedback;
    }

    public Integer getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(Integer referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
