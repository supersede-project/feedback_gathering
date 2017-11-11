package ch.fhnw.cere.repository.models;


import javax.persistence.*;


@Entity
public class Setting {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long applicationId;
    private String feedbackEmailReceivers;
    private String kafkaTopicId;

    public Setting() {
    }

    public Setting(long applicationId, String feedbackEmailReceivers, String kafkaTopicId) {
        this.applicationId = applicationId;
        this.feedbackEmailReceivers = feedbackEmailReceivers;
        this.kafkaTopicId = kafkaTopicId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    public String getFeedbackEmailReceivers() {
        return feedbackEmailReceivers;
    }

    public void setFeedbackEmailReceivers(String feedbackEmailReceivers) {
        this.feedbackEmailReceivers = feedbackEmailReceivers;
    }

    public String getKafkaTopicId() {
        return kafkaTopicId;
    }

    public void setKafkaTopicId(String kafkaTopicId) {
        this.kafkaTopicId = kafkaTopicId;
    }
}