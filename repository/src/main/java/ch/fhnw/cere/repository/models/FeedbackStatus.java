package ch.fhnw.cere.repository.models;

import javax.persistence.*;

@Entity
public class FeedbackStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private FeedbackStatusType statusType;

    public FeedbackStatus() {

    }
    public FeedbackStatus(FeedbackStatusType statusType) {
        this.statusType = statusType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public FeedbackStatusType getStatusType() {
        return statusType;
    }

    public void setStatusType(FeedbackStatusType statusType) {
        this.statusType = statusType;
    }
}
