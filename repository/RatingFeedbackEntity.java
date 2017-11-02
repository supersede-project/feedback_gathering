package repository;

import javax.persistence.*;

/**
 * Created by Aydinli on 02.11.2017.
 */
@Entity
@Table(name = "rating_feedback", schema = "monitor_feedback", catalog = "")
public class RatingFeedbackEntity {
    private long id;
    private long mechanismId;
    private int rating;
    private String title;
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
    @Column(name = "rating")
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RatingFeedbackEntity that = (RatingFeedbackEntity) o;

        if (id != that.id) return false;
        if (mechanismId != that.mechanismId) return false;
        if (rating != that.rating) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (mechanismId ^ (mechanismId >>> 32));
        result = 31 * result + rating;
        result = 31 * result + (title != null ? title.hashCode() : 0);
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
