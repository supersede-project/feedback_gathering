package ch.fhnw.cere.repository.models;


import javax.persistence.*;


@Entity
public class AndroidUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(unique = true)
    private String name;
    private long applicationId;
    private boolean isDeveloper;
    private boolean isBlocked;
    @Transient
    private int karma;
    @Transient
    private VoteCount voteCount;
    @Transient
    private int feedbackCount;

    public AndroidUser() {
    }

    public AndroidUser(String name,  boolean isDeveloper) {
        this.name = name;
        this.isDeveloper = isDeveloper;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    public boolean isDeveloper() {
        return isDeveloper;
    }

    public void setDeveloper(boolean developer) {
        isDeveloper = developer;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public int getKarma() {
        return karma;
    }

    public void setKarma(int karma) {
        this.karma = karma;
    }

    public int getFeedbackCount() {
        return feedbackCount;
    }

    public void setFeedbackCount(int feedbackCount) {
        this.feedbackCount = feedbackCount;
    }

    public VoteCount getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(VoteCount voteCount) {
        this.voteCount = voteCount;
    }
}