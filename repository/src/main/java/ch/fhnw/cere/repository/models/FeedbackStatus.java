package ch.fhnw.cere.repository.models;

import javax.persistence.*;

@Entity
public class FeedbackStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String statusType;

    public FeedbackStatus() {

    }
    public FeedbackStatus(String statusType) {
        this.statusType = statusType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }
}
