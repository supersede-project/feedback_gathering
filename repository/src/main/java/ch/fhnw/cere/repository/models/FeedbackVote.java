package ch.fhnw.cere.repository.models;


import javax.persistence.*;


@Entity
public class FeedbackVote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;


    public FeedbackVote() {
    }

    public FeedbackVote(String name,  boolean isDeveloper) {

    }
}