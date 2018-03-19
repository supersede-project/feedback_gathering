package ch.fhnw.cere.repository.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Aydinli on 20.02.2018.
 */
@Entity
public class ChatUnread {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonIgnoreProperties({"title","userIdentification","createdAt","updatedAt",
            "applicationId","configurationId","language","commentCount","likeCount","dislikeCount",
            "blocked","iconPath","unreadCommentCount","visibility","published","application",
            "contextInformation","attachmentFeedbacks","audioFeedbacks","categoryFeedbacks",
            "ratingFeedbacks","screenshotFeedbacks","textFeedbacks"})
    @ManyToOne(cascade = {CascadeType.MERGE})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

    @JsonIgnoreProperties({"createdAt","updatedAt","applicationId","username","email",
            "phoneNumber"})
    @ManyToOne(cascade = {CascadeType.MERGE})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private EndUser enduser;

    @JsonIgnoreProperties({"feedback","user","chatDate","chatText","initiatedByUser"})
    @ManyToOne(cascade = {CascadeType.MERGE})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "chat_message_id")
    private FeedbackChatInformation feedbackChatInformation;

    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    public ChatUnread(){

    }

    public ChatUnread(Feedback feedback, EndUser enduser, FeedbackChatInformation feedbackChatInformation) {
        this.feedback = feedback;
        this.enduser = enduser;
        this.feedbackChatInformation = feedbackChatInformation;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public EndUser getEnduser() {
        return enduser;
    }

    public void setEnduser(EndUser enduser) {
        this.enduser = enduser;
    }

    public FeedbackChatInformation getFeedbackChatInformation() {
        return feedbackChatInformation;
    }

    public void setFeedbackChatInformation(FeedbackChatInformation feedbackChatInformation) {
        this.feedbackChatInformation = feedbackChatInformation;
    }
}
