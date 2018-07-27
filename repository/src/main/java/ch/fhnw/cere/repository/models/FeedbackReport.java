package ch.fhnw.cere.repository.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
public class FeedbackReport {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "reporter_id")
    private AndroidUser reporter;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.PERSIST})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

    private String reason;

    public FeedbackReport() {

    }

    public FeedbackReport(Feedback feedback, AndroidUser reporter, String reason) {
        this.feedback = feedback;
        this.reporter = reporter;
        this.reason = reason;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AndroidUser getReporter() {
        return reporter;
    }

    public void setReporter(AndroidUser reporter) {
        this.reporter = reporter;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
