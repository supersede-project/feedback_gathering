package ch.fhnw.cere.repository.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import eu.supersede.integration.api.feedback.repository.types.FeedbackComment;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Aydinli on 23.01.2018.
 */
@Entity
public class CommentViewed {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonIgnoreProperties({"feedback","user","parentComment","children","commentText",
            "createdAt","updatedAt","bool_is_developer","activeStatus"})
    @ManyToOne(cascade = {CascadeType.MERGE})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "comment_id")
    private CommentFeedback comment;

    @JsonIgnoreProperties({"createdAt","updatedAt","applicationId","username","email",
            "phoneNumber"})
    @ManyToOne(cascade = {CascadeType.MERGE})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private EndUser enduser;

    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    public CommentViewed(){
    }

    public CommentViewed(EndUser endUser, CommentFeedback commentFeedback) {
        this.enduser = endUser;
        this.comment = commentFeedback;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CommentFeedback getComment() {
        return comment;
    }

    public void setComment(CommentFeedback commentFeedback) {
        this.comment = commentFeedback;
    }

    public EndUser getEnduser() {
        return enduser;
    }

    public void setEnduser(EndUser enduser) {
        this.enduser = enduser;
    }
}
