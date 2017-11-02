package repository;

import javax.persistence.*;

/**
 * Created by Aydinli on 02.11.2017.
 */
@Entity
@Table(name = "text_feedback", schema = "monitor_feedback", catalog = "")
public class TextFeedbackEntity {
    private long id;
    private long mechanismId;
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

        TextFeedbackEntity that = (TextFeedbackEntity) o;

        if (id != that.id) return false;
        if (mechanismId != that.mechanismId) return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (mechanismId ^ (mechanismId >>> 32));
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
