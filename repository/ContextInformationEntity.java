package repository;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by Aydinli on 02.11.2017.
 */
@Entity
@Table(name = "context_information", schema = "monitor_feedback", catalog = "")
public class ContextInformationEntity {
    private long id;
    private String androidVersion;
    private String country;
    private Double devicePixelRatio;
    private Timestamp localTime;
    private String region;
    private String resolution;
    private String timeZone;
    private String userAgent;
    private String url;
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
    @Column(name = "android_version")
    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

    @Basic
    @Column(name = "country")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Basic
    @Column(name = "device_pixel_ratio")
    public Double getDevicePixelRatio() {
        return devicePixelRatio;
    }

    public void setDevicePixelRatio(Double devicePixelRatio) {
        this.devicePixelRatio = devicePixelRatio;
    }

    @Basic
    @Column(name = "local_time")
    public Timestamp getLocalTime() {
        return localTime;
    }

    public void setLocalTime(Timestamp localTime) {
        this.localTime = localTime;
    }

    @Basic
    @Column(name = "region")
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Basic
    @Column(name = "resolution")
    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    @Basic
    @Column(name = "time_zone")
    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    @Basic
    @Column(name = "user_agent")
    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    @Basic
    @Column(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContextInformationEntity that = (ContextInformationEntity) o;

        if (id != that.id) return false;
        if (androidVersion != null ? !androidVersion.equals(that.androidVersion) : that.androidVersion != null)
            return false;
        if (country != null ? !country.equals(that.country) : that.country != null) return false;
        if (devicePixelRatio != null ? !devicePixelRatio.equals(that.devicePixelRatio) : that.devicePixelRatio != null)
            return false;
        if (localTime != null ? !localTime.equals(that.localTime) : that.localTime != null) return false;
        if (region != null ? !region.equals(that.region) : that.region != null) return false;
        if (resolution != null ? !resolution.equals(that.resolution) : that.resolution != null) return false;
        if (timeZone != null ? !timeZone.equals(that.timeZone) : that.timeZone != null) return false;
        if (userAgent != null ? !userAgent.equals(that.userAgent) : that.userAgent != null) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (androidVersion != null ? androidVersion.hashCode() : 0);
        result = 31 * result + (country != null ? country.hashCode() : 0);
        result = 31 * result + (devicePixelRatio != null ? devicePixelRatio.hashCode() : 0);
        result = 31 * result + (localTime != null ? localTime.hashCode() : 0);
        result = 31 * result + (region != null ? region.hashCode() : 0);
        result = 31 * result + (resolution != null ? resolution.hashCode() : 0);
        result = 31 * result + (timeZone != null ? timeZone.hashCode() : 0);
        result = 31 * result + (userAgent != null ? userAgent.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
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
