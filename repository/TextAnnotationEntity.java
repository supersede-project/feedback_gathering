package repository;

import javax.persistence.*;

/**
 * Created by Aydinli on 02.11.2017.
 */
@Entity
@Table(name = "text_annotation", schema = "monitor_feedback", catalog = "")
public class TextAnnotationEntity {
    private long id;
    private Integer referenceNumber;
    private String text;
    private FileFeedbackEntity fileFeedbackByScreenshotFeedbackId;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "reference_number")
    public Integer getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(Integer referenceNumber) {
        this.referenceNumber = referenceNumber;
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

        TextAnnotationEntity that = (TextAnnotationEntity) o;

        if (id != that.id) return false;
        if (referenceNumber != null ? !referenceNumber.equals(that.referenceNumber) : that.referenceNumber != null)
            return false;
        if (text != null ? !text.equals(that.text) : that.text != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (referenceNumber != null ? referenceNumber.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "screenshot_feedback_id", referencedColumnName = "id")
    public FileFeedbackEntity getFileFeedbackByScreenshotFeedbackId() {
        return fileFeedbackByScreenshotFeedbackId;
    }

    public void setFileFeedbackByScreenshotFeedbackId(FileFeedbackEntity fileFeedbackByScreenshotFeedbackId) {
        this.fileFeedbackByScreenshotFeedbackId = fileFeedbackByScreenshotFeedbackId;
    }
}
