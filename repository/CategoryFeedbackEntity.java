package repository;

import javax.persistence.*;

/**
 * Created by Aydinli on 02.11.2017.
 */
@Entity
@Table(name = "category_feedback", schema = "monitor_feedback", catalog = "")
public class CategoryFeedbackEntity {
    private long id;
    private long mechanismId;
    private Long parameterId;
    private String text;
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
    @Column(name = "mechanism_id")
    public long getMechanismId() {
        return mechanismId;
    }

    public void setMechanismId(long mechanismId) {
        this.mechanismId = mechanismId;
    }

    @Basic
    @Column(name = "parameter_id")
    public Long getParameterId() {
        return parameterId;
    }

    public void setParameterId(Long parameterId) {
        this.parameterId = parameterId;
    }

    @Basic
    @Column(name = "text")
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryFeedbackEntity that = (CategoryFeedbackEntity) o;

        if (id != that.id) return false;
        if (mechanismId != that.mechanismId) return false;
        if (parameterId != null ? !parameterId.equals(that.parameterId) : that.parameterId != null) return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (mechanismId ^ (mechanismId >>> 32));
        result = 31 * result + (parameterId != null ? parameterId.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
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
