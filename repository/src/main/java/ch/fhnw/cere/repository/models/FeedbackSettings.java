package ch.fhnw.cere.repository.models;

import ch.fhnw.cere.repository.models.orchestrator.Application;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class FeedbackSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonIgnore
    @OneToOne(cascade = {CascadeType.REMOVE, CascadeType.MERGE})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "feedback_id", nullable = false)
    private Feedback feedback;

    @JsonIgnore
    @OneToOne(cascade = {CascadeType.REMOVE, CascadeType.MERGE})
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

    public EndUser getUser() { return user;}
    public Feedback getFeedback() { return feedback; }
    public Application getApplication() { return application; }
    public Boolean getFeedbackQuery() { return feedbackQuery;  }
    public Boolean getGlobalFeedbackSetting() { return globalFeedbackSetting; }
    public Boolean getStatusUpdates() { return statusUpdates; }
    public long getId() { return id; }
    public String getFeedbackQueryChannel() { return feedbackQueryChannel; }
    public String getStatusUpdatesContactChannel() { return statusUpdatesContactChannel; }
    public void setFeedbackQuery(Boolean feedbackQuery) { this.feedbackQuery = feedbackQuery;}

    @Transient
    private Application application;

    public FeedbackSettings(){}

    public FeedbackSettings(EndUser user, Feedback feedback, boolean statusUpdates, String statusUpdatesContactChannel, boolean feedbackQuery, String feedbackQueryChannel, boolean globalFeedbackSetting, Application application){
      this.application =   application;
      this.feedback = feedback;
      this.feedbackQuery = feedbackQuery;
      this.feedbackQueryChannel = feedbackQueryChannel;
      this.statusUpdates = statusUpdates;
      this.feedbackQueryChannel = statusUpdatesContactChannel;
      this.globalFeedbackSetting = globalFeedbackSetting;
    }

}
