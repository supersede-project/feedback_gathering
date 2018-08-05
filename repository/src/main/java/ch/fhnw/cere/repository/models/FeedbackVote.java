package ch.fhnw.cere.repository.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


import javax.persistence.*;


@Entity
public class FeedbackVote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private int vote;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.PERSIST})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "voter_user_id")
    private AndroidUser voterUser;


    public FeedbackVote() {
    }

    public FeedbackVote(Feedback feedback, AndroidUser voterUser, int vote) {
        this.feedback = feedback;
        this.voterUser = voterUser;
        this.vote = vote;
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



}