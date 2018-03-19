package ch.fhnw.cere.repository.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Aydinli on 08.11.2017.
 * This class represents feedback entries which are issued by the company. They are
 * comprised of a text and a status. User interaction in form of like/dislike or comment
 * are restricted. The main objective of this class is to notify users in terms of
 * upcoming events or features
 */
@Entity
public class FeedbackCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

//    @JsonIgnoreProperties({"title","userIdentification","createdAt","updatedAt",
//            "applicationId","configurationId","language","commentCount","likeCount","dislikeCount",
//            "blocked","iconPath","unreadCommentCount","visibility","published","application",
//            "contextInformation","attachmentFeedbacks","audioFeedbacks","categoryFeedbacks",
//            "ratingFeedbacks","screenshotFeedbacks","textFeedbacks"})
//    @ManyToOne(cascade = {CascadeType.PERSIST})
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    @JoinColumn(name = "feedback_id")
//    private Feedback feedback;

    private String text;
    private String status;

    private Date createdAt;
    private Date updatedAt;

    private Boolean promote;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    public FeedbackCompany() {
    }

//    public FeedbackCompany(Feedback feedback, String text, String status, Date createdAt, Date updatedAt) {
//        this.feedback = feedback;
//        this.text = text;
//        this.status = status;
//        this.createdAt = createdAt;
//        this.updatedAt = updatedAt;
//    }

    public FeedbackCompany(String text, String status,Boolean promote) {
        this.text = text;
        this.status = status;
        this.promote = promote;
    }

    public Boolean getPromote() {
        return promote;
    }

    public void setPromote(Boolean promote) {
        this.promote = promote;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

//    public Feedback getFeedback() {
//        return feedback;
//    }
//
//    public void setFeedback(Feedback feedback) {
//        this.feedback = feedback;
//    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
