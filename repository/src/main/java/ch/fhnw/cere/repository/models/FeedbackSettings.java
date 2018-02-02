package ch.fhnw.cere.repository.models;

import ch.fhnw.cere.repository.models.orchestrator.Application;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class FeedbackSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonIgnoreProperties({"title","userIdentification","createdAt","updatedAt",
            "applicationId","configurationId","language","commentCount","likeCount","dislikeCount",
            "blocked","iconPath","unreadCommentCount","visibility","published","application",
            "contextInformation","attachmentFeedbacks","audioFeedbacks","categoryFeedbacks",
            "ratingFeedbacks","screenshotFeedbacks","textFeedbacks"})
    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "feedback_id", nullable = false)
    private Feedback feedback;

    @JsonIgnoreProperties({"createdAt","updatedAt","applicationId","username","email",
            "phoneNumber"})
    @OneToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private EndUser user;

    @NotNull
    private Boolean statusUpdates;
    @NotNull
    private String statusUpdatesContactChannel;
    @NotNull
    private Boolean feedbackQuery;
    @NotNull
    private String feedbackQueryChannel;
    @NotNull
    private Boolean globalFeedbackSetting;

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public EndUser getUser() { return user;}
    public Feedback getFeedback() { return feedback; }
    public Application getApplication() { return application; }
    public Boolean getFeedbackQuery() { return feedbackQuery;  }
    public Boolean getGlobalFeedbackSetting() { return globalFeedbackSetting; }
    public Boolean getStatusUpdates() { return statusUpdates; }
    public long getId() { return id; }
    public String getFeedbackQueryChannel() { return feedbackQueryChannel; }
    public String getStatusUpdatesContactChannel() { return statusUpdatesContactChannel; }

    @Transient
    private Application application;

    public void setUser(EndUser user) {
        this.user = user;
    }

    public void setStatusUpdates(Boolean statusUpdates) {
        this.statusUpdates = statusUpdates;
    }

    public void setStatusUpdatesContactChannel(String statusUpdatesContactChannel) {
        this.statusUpdatesContactChannel = statusUpdatesContactChannel;
    }

    public void setFeedbackQuery(Boolean feedbackQuery) {
        this.feedbackQuery = feedbackQuery;
    }

    public void setFeedbackQueryChannel(String feedbackQueryChannel) {
        this.feedbackQueryChannel = feedbackQueryChannel;
    }

    public void setGlobalFeedbackSetting(Boolean globalFeedbackSetting) {
        this.globalFeedbackSetting = globalFeedbackSetting;
    }

    public FeedbackSettings(){}

    public FeedbackSettings(EndUser user, Feedback feedback, boolean statusUpdates, String statusUpdatesContactChannel, boolean feedbackQuery, String feedbackQueryChannel, boolean globalFeedbackSetting){
      this.user = user;
      this.feedback = feedback;
      this.feedbackQuery = feedbackQuery;
      this.feedbackQueryChannel = feedbackQueryChannel;
      this.statusUpdates = statusUpdates;
      this.statusUpdatesContactChannel = statusUpdatesContactChannel;
      this.globalFeedbackSetting = globalFeedbackSetting;
    }
}
