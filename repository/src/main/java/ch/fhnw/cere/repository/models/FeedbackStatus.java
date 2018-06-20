package ch.fhnw.cere.repository.models;

import javax.persistence.*;

@Entity
public class FeedbackStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String status;

    public FeedbackStatus() {

    }
    public FeedbackStatus(long id, String status) {
        this.id = id;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
