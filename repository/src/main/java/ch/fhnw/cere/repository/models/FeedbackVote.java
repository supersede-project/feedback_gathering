package ch.fhnw.cere.repository.models;


import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;


@Entity
public class FeedbackVote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private int vote;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "voter_user_id")
    private AndroidUser voterUser;

    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "voted_user_id")
    private AndroidUser votedUser;

    public FeedbackVote() {
    }

    public FeedbackVote(int vote, AndroidUser voterUser, AndroidUser votedUser) {
        this.vote = vote;
        this.voterUser = voterUser;
        this.votedUser = votedUser;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public AndroidUser getVoterUser() {
        return voterUser;
    }

    public void setVoter_user(AndroidUser voterUser) {
        this.voterUser = voterUser;
    }

    public AndroidUser getVotedUser() {
        return votedUser;
    }

    public void setVotedUser(AndroidUser votedUser) {
        this.votedUser = votedUser;
    }


}