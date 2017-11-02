package repository;

import javax.persistence.*;

/**
 * Created by Aydinli on 02.11.2017.
 */
@Entity
@Table(name = "status", schema = "monitor_feedback", catalog = "")
public class StatusEntity {
    private long id;
    private Long statusOptionId;
    private ApiUserEntity apiUserByApiUserId;
    private FeedbackEntity feedbackByFeedbackId;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "status_option_id")
    public Long getStatusOptionId() {
        return statusOptionId;
    }

    public void setStatusOptionId(Long statusOptionId) {
        this.statusOptionId = statusOptionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StatusEntity that = (StatusEntity) o;

        if (id != that.id) return false;
        if (statusOptionId != null ? !statusOptionId.equals(that.statusOptionId) : that.statusOptionId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (statusOptionId != null ? statusOptionId.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "api_user_id", referencedColumnName = "id")
    public ApiUserEntity getApiUserByApiUserId() {
        return apiUserByApiUserId;
    }

    public void setApiUserByApiUserId(ApiUserEntity apiUserByApiUserId) {
        this.apiUserByApiUserId = apiUserByApiUserId;
    }

    @ManyToOne
    @JoinColumn(name = "feedback_id", referencedColumnName = "id")
    public FeedbackEntity getFeedbackByFeedbackId() {
        return feedbackByFeedbackId;
    }

    public void setFeedbackByFeedbackId(FeedbackEntity feedbackByFeedbackId) {
        this.feedbackByFeedbackId = feedbackByFeedbackId;
    }
}
