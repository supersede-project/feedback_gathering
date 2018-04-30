package ch.fhnw.cere.repository.models;

import javax.persistence.*;

@Entity
public class FeedbackComment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String comment;




    public FeedbackComment() {

    }

}
