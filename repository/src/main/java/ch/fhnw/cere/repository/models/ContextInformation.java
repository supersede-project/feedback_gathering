package ch.fhnw.cere.repository.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.stream.Collectors;


@Entity
public class ContextInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

    private String resolution;
    private String userAgent;
    private String androidVersion;
    private Timestamp localTime;
    private String timeZone;
    private Float devicePixelRatio;
    private String country;
    private String region;

    public ContextInformation() {
    }

    public ContextInformation(Feedback feedback, String resolution, String userAgent, String androidVersion, Timestamp localTime, String timeZone, Float devicePixelRatio, String country, String region) {
        this.feedback = feedback;
        this.resolution = resolution;
        this.userAgent = userAgent;
        this.androidVersion = androidVersion;
        this.localTime = localTime;
        this.timeZone = timeZone;
        this.devicePixelRatio = devicePixelRatio;
        this.country = country;
        this.region = region;
    }

    @Override
    public String toString() {
        return String.format(
                "ContextInformation[id=%d, userAgent='%s']",
                id, userAgent);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

    public Timestamp getLocalTime() {
        return localTime;
    }

    public void setLocalTime(Timestamp localTime) {
        this.localTime = localTime;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public Float getDevicePixelRatio() {
        return devicePixelRatio;
    }

    public void setDevicePixelRatio(Float devicePixelRatio) {
        this.devicePixelRatio = devicePixelRatio;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
