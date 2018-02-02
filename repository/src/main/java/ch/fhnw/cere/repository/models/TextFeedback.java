package ch.fhnw.cere.repository.models;


import ch.fhnw.cere.repository.models.orchestrator.Mechanism;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Type;

import javax.persistence.*;


@Entity
public class TextFeedback implements MechanismFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonIgnoreProperties({"title","userIdentification","createdAt","updatedAt",
            "applicationId","configurationId","language","commentCount","likeCount","dislikeCount",
            "blocked","iconPath","unreadCommentCount","visibility","published","application",
            "contextInformation","attachmentFeedbacks","audioFeedbacks","categoryFeedbacks",
            "ratingFeedbacks","screenshotFeedbacks","textFeedbacks"})
    @ManyToOne(cascade = {CascadeType.PERSIST})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

    @Type(type="text")
    private String text;
    private long mechanismId;

    @JsonIgnore
    @Transient
    private Mechanism mechanism;

    public TextFeedback() {

    }

    public TextFeedback(Feedback feedback, String text, long mechanismId) {
        this.feedback = feedback;
        this.text = text;
        this.mechanismId = mechanismId;
    }

    @Override
    public String toString() {
        return String.format(
                "TextFeedback[id=%d, feedbackId='%d', text='%s', mechanismId='%d']",
                id, feedback.getId(), text, mechanismId);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getMechanismId() {
        return mechanismId;
    }

    public void setMechanismId(long mechanismId) {
        this.mechanismId = mechanismId;
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
