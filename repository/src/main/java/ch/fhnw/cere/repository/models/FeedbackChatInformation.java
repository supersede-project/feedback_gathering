package ch.fhnw.cere.repository.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
public class FeedbackChatInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long feedbackChatId;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "feedback_id")
    @Column(name = "feedback_id", nullable = false)
    private Feedback feedback;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    @Column(name = "user_id", nullable = false)
    private EndUser user;

    private Date chatDate;
    private String chatText;
    private Boolean initatedByUser;

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
}
