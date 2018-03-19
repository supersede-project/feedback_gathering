package ch.fhnw.cere.repository.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * This class represents single messages exchanged between developers and end-users
 */
@Entity
public class FeedbackChatInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long feedbackChatId;

    @JsonIgnoreProperties({"title","userIdentification","createdAt","updatedAt",
            "applicationId","configurationId","language","commentCount","likeCount","dislikeCount",
            "blocked","iconPath","unreadCommentCount","visibility","published","application",
            "contextInformation","attachmentFeedbacks","audioFeedbacks","categoryFeedbacks",
            "ratingFeedbacks","screenshotFeedbacks","textFeedbacks"})
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "feedback_id", nullable = false)
    private Feedback feedback;

    @JsonIgnoreProperties({"createdAt","updatedAt","applicationId","username","email",
            "phoneNumber"})
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private EndUser user;

    private Date chatDate;
    private String chatText;
    private Boolean initatedByUser;

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public void setUser(EndUser user) {
        this.user = user;
    }

    public Boolean getInitatedByUser() { return initatedByUser;  }
    public Date getChatDate() { return chatDate; }
    public Feedback getFeedback() { return feedback; }
    public long getFeedbackChatId() { return feedbackChatId; }
    public String getChatText() { return chatText; }
    public EndUser getUser() { return user;}

    public void setChatDate(Date chatDate) { this.chatDate = chatDate;  }
    public void setChatText(String chatText) { this.chatText = chatText; }
    public void setInitatedByUser(Boolean initatedByUser) { this.initatedByUser = initatedByUser; }

    @PrePersist
    protected void onCreate(){ chatDate = new Date();}

    public FeedbackChatInformation() {
    }

    public FeedbackChatInformation(EndUser user, Feedback feedback, Date chatDate, String chatText, boolean initiatedByUser){
       this.chatDate = chatDate;
       this.chatText = chatText;
       this.user = user;
       this.feedback = feedback;
       this.initatedByUser = initiatedByUser;
    }

    public FeedbackChatInformation(EndUser user, Feedback feedback, String chatText){
        this.user = user;
        this.feedback = feedback;
        this.chatText = chatText;
    }
}
