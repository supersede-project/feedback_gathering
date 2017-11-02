package repository;

import javax.persistence.*;

/**
 * Created by Aydinli on 02.11.2017.
 */
@Entity
@Table(name = "audio_feedback", schema = "monitor_feedback", catalog = "")
public class AudioFeedbackEntity {
    private long id;
    private int duration;
    private String fileExtension;
    private long mechanismId;
    private String path;
    private int size;
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
    @Column(name = "duration")
    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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
    @Column(name = "mechanism_id")
    public long getMechanismId() {
        return mechanismId;
    }

    public void setMechanismId(long mechanismId) {
        this.mechanismId = mechanismId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AudioFeedbackEntity that = (AudioFeedbackEntity) o;

        if (id != that.id) return false;
        if (duration != that.duration) return false;
        if (mechanismId != that.mechanismId) return false;
        if (size != that.size) return false;
        if (fileExtension != null ? !fileExtension.equals(that.fileExtension) : that.fileExtension != null)
            return false;
        if (path != null ? !path.equals(that.path) : that.path != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + duration;
        result = 31 * result + (fileExtension != null ? fileExtension.hashCode() : 0);
        result = 31 * result + (int) (mechanismId ^ (mechanismId >>> 32));
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + size;
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
