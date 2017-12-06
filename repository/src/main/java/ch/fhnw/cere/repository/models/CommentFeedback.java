package ch.fhnw.cere.repository.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Entity
public class CommentFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "feedback_id", nullable = false)
    private Feedback feedback;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private EndUser user;

    private String commentText;
    private Date createdAt;
    private Date updatedAt;
    private Boolean bool_is_developer;
    private Boolean activeStatus;

    public String getCommentText(){ return commentText;}
    public Boolean check_is_developer() { return bool_is_developer;}
    public Boolean getActiveStatus() { return activeStatus; }
    public long getId() {return id;}
    public Date getCreatedAt(){return createdAt;}
    public Date getUpdatedAt(){return updatedAt;}
    public Feedback getFeedback() { return feedback; }
    public EndUser getUser() { return user;}

    public void setCreatedAt(){this.createdAt = createdAt; }
    public void setUpdatedAt(){this.updatedAt = updatedAt;}
    public void setCommentText(){this.commentText = commentText;}
    public void setBool_is_developer(){this.bool_is_developer = bool_is_developer;}
    public void setActiveStatus(){this.activeStatus = activeStatus;}

    @PrePersist
    protected void onCreate(){ createdAt = new Date();}

    @PreUpdate
    protected void onUpdate(){ updatedAt = new Date();}

    public CommentFeedback(){
    }

    public CommentFeedback(Feedback feedback, EndUser user, boolean is_developer, String commentText, Date createdAt, Date updatedAt, boolean activeStatus){
        this.bool_is_developer = is_developer;
        this.commentText = commentText;
        this.user = user;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.activeStatus = activeStatus;
    }

}
