package ch.fhnw.cere.repository.models;


import ch.fhnw.cere.repository.models.orchestrator.Mechanism;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;


@Entity
public class RatingFeedback implements MechanismFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.PERSIST})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

    private String title;
    private int rating;
    private long mechanismId;

    @JsonIgnore
    @Transient
    private Mechanism mechanism;

    @Override
    public String toString() {
        return String.format(
                "RatingFeedback[id=%d]",
                id);
    }

    public RatingFeedback() {
    }

    public RatingFeedback(Feedback feedback, String title, int rating, long mechanismId) {
        this.feedback = feedback;
        this.title = title;
        this.rating = rating;
        this.mechanismId = mechanismId;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
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
