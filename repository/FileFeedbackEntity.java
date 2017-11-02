package repository;

import javax.persistence.*;

/**
 * Created by Aydinli on 02.11.2017.
 */
@Entity
@Table(name = "file_feedback", schema = "monitor_feedback", catalog = "")
public class FileFeedbackEntity {
    private String dtype;
    private long id;
    private String fileExtension;
    private String path;
    private int size;
    private Long mechanismId;
    private Integer duration;
    private FeedbackEntity feedbackByFeedbackId;

    @Basic
    @Column(name = "dtype")
    public String getDtype() {
        return dtype;
    }

    public void setDtype(String dtype) {
        this.dtype = dtype;
    }

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "file_extension")
    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    @Basic
    @Column(name = "path")
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Basic
    @Column(name = "size")
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Basic
    @Column(name = "mechanism_id")
    public Long getMechanismId() {
        return mechanismId;
    }

    public void setMechanismId(Long mechanismId) {
        this.mechanismId = mechanismId;
    }

    @Basic
    @Column(name = "duration")
    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileFeedbackEntity that = (FileFeedbackEntity) o;

        if (id != that.id) return false;
        if (size != that.size) return false;
        if (dtype != null ? !dtype.equals(that.dtype) : that.dtype != null) return false;
        if (fileExtension != null ? !fileExtension.equals(that.fileExtension) : that.fileExtension != null)
            return false;
        if (path != null ? !path.equals(that.path) : that.path != null) return false;
        if (mechanismId != null ? !mechanismId.equals(that.mechanismId) : that.mechanismId != null) return false;
        if (duration != null ? !duration.equals(that.duration) : that.duration != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = dtype != null ? dtype.hashCode() : 0;
        result = 31 * result + (int) (id ^ (id >>> 32));
        result = 31 * result + (fileExtension != null ? fileExtension.hashCode() : 0);
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + size;
        result = 31 * result + (mechanismId != null ? mechanismId.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
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
