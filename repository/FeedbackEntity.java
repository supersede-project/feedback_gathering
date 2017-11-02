package repository;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Aydinli on 02.11.2017.
 */
@Entity
@Table(name = "feedback", schema = "monitor_feedback", catalog = "")
public class FeedbackEntity {
    private long id;
    private long applicationId;
    private long configurationId;
    private Timestamp createdAt;
    private String language;
    private String title;
    private Timestamp updatedAt;
    private String userIdentification;

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
    @Column(name = "configuration_id")
    public long getConfigurationId() {
        return configurationId;
    }

    public void setConfigurationId(long configurationId) {
        this.configurationId = configurationId;
    }

    @Basic
    @Column(name = "created_at")
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Basic
    @Column(name = "language")
    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "updated_at")
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Basic
    @Column(name = "user_identification")
    public String getUserIdentification() {
        return userIdentification;
    }

    public void setUserIdentification(String userIdentification) {
        this.userIdentification = userIdentification;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FeedbackEntity that = (FeedbackEntity) o;

        if (id != that.id) return false;
        if (applicationId != that.applicationId) return false;
        if (configurationId != that.configurationId) return false;
        if (createdAt != null ? !createdAt.equals(that.createdAt) : that.createdAt != null) return false;
        if (language != null ? !language.equals(that.language) : that.language != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (updatedAt != null ? !updatedAt.equals(that.updatedAt) : that.updatedAt != null) return false;
        if (userIdentification != null ? !userIdentification.equals(that.userIdentification) : that.userIdentification != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (int) (applicationId ^ (applicationId >>> 32));
        result = 31 * result + (int) (configurationId ^ (configurationId >>> 32));
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (language != null ? language.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        result = 31 * result + (userIdentification != null ? userIdentification.hashCode() : 0);
        return result;
    }
}
