package repository;

import javax.persistence.*;

/**
 * Created by Aydinli on 02.11.2017.
 */
@Entity
@Table(name = "setting", schema = "monitor_feedback", catalog = "")
public class SettingEntity {
    private long id;
    private long applicationId;
    private String feedbackEmailReceivers;
    private String kafkaTopicId;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "application_id")
    public long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(long applicationId) {
        this.applicationId = applicationId;
    }

    @Basic
    @Column(name = "feedback_email_receivers")
    public String getFeedbackEmailReceivers() {
        return feedbackEmailReceivers;
    }

    public void setFeedbackEmailReceivers(String feedbackEmailReceivers) {
        this.feedbackEmailReceivers = feedbackEmailReceivers;
    }

    @Basic
    @Column(name = "kafka_topic_id")
    public String getKafkaTopicId() {
        return kafkaTopicId;
    }

    public void setKafkaTopicId(String kafkaTopicId) {
        this.kafkaTopicId = kafkaTopicId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SettingEntity that = (SettingEntity) o;

        if (id != that.id) return false;
        if (applicationId != that.applicationId) return false;
        if (feedbackEmailReceivers != null ? !feedbackEmailReceivers.equals(that.feedbackEmailReceivers) : that.feedbackEmailReceivers != null)
            return false;
        if (kafkaTopicId != null ? !kafkaTopicId.equals(that.kafkaTopicId) : that.kafkaTopicId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (applicationId ^ (applicationId >>> 32));
        result = 31 * result + (feedbackEmailReceivers != null ? feedbackEmailReceivers.hashCode() : 0);
        result = 31 * result + (kafkaTopicId != null ? kafkaTopicId.hashCode() : 0);
        return result;
    }
}
