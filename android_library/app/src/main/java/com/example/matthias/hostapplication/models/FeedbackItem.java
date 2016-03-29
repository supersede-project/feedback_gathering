package com.example.matthias.hostapplication.models;

import java.util.Map;

/**
 * Created by Matthias on 20.03.2016.
 */
public class FeedbackItem {
    private String status;
    private String title;
    private String text;
    private String image;
    private String component;
    private String appVersion;
    private String priority;
    private String platform;
    private String updatedAt;
    private int pk;
    private String dueDate;
    private String createdAt;

    public Map<String, Object> getUser() {
        return user;
    }

    public void setUser(Map<String, Object> user) {
        this.user = user;
    }

    public Map<String, Object> getDeveloper() {
        return developer;
    }

    public void setDeveloper(Map<String, Object> developer) {
        this.developer = developer;
    }

    private Map<String, Object> user;
    private Map<String, Object> developer;

    //private User user;
    //private Developer developer;

    public FeedbackItem() {}

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

/*    public Developer getDeveloper() {
        return developer;
    }

    public void setDeveloper(Developer developer) {
        this.developer = developer;
    }*/

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPk() {
        return pk;
    }

    public void setPk(int pk) {
        this.pk = pk;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

/*    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }*/
}
