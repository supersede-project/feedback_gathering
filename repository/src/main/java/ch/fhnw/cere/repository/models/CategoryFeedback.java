package ch.fhnw.cere.repository.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;


@Entity
public class CategoryFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

    private long mechanismId;

    private Long parameterId;

    private String text;

    @Override
    public String toString() {
        return String.format(
                "CategoryFeedback[id=%d]",
                id);
    }

    public CategoryFeedback() {
    }

    public CategoryFeedback(Feedback feedback, long mechanismId, Long parameterId, String text) {
        this.feedback = feedback;
        this.mechanismId = mechanismId;
        this.parameterId = parameterId;
        this.text = text;
    }

    public CategoryFeedback(Feedback feedback, long mechanismId, String text) {
        this.feedback = feedback;
        this.mechanismId = mechanismId;
        this.text = text;
    }

    public CategoryFeedback(Feedback feedback, long mechanismId, Long parameterId) {
        this.feedback = feedback;
        this.mechanismId = mechanismId;
        this.parameterId = parameterId;
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

    public long getMechanismId() {
        return mechanismId;
    }

    public void setMechanismId(long mechanismId) {
        this.mechanismId = mechanismId;
    }

    public Long getParameterId() {
        return parameterId;
    }

    public void setParameterId(Long parameterId) {
        this.parameterId = parameterId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
