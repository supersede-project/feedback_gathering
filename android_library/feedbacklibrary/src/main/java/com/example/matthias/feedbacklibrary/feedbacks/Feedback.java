package com.example.matthias.feedbacklibrary.feedbacks;

import com.example.matthias.feedbacklibrary.models.ScreenshotMechanism;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Matthias on 25.04.2016.
 */
public class Feedback implements Serializable {
    private String text;
    private String application;
    private String title;
    private float configVersion;
    private String user;

    private List<RatingFeedback> ratings;
    private List<AudioFeedback> audios;
    private List<ScreenshotFeedback> screenshots;

    public String getText ()
    {
        return text;
    }

    public void setText (String text)
    {
        this.text = text;
    }

    public String getApplication ()
    {
        return application;
    }

    public void setApplication (String application)
    {
        this.application = application;
    }

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public float getConfigVersion ()
    {
        return configVersion;
    }

    public void setConfigVersion (float configVersion)
    {
        this.configVersion = configVersion;
    }

    public String getUser ()
    {
        return user;
    }

    public void setUser (String user)
    {
        this.user = user;
    }

    public List<RatingFeedback> getRatings ()
    {
        return ratings;
    }

    public void setRatings (List<RatingFeedback> ratings)
    {
        this.ratings = ratings;
    }

    public List<AudioFeedback> getAudios() {
        return audios;
    }

    public void setAudios(List<AudioFeedback> audios) {
        this.audios = audios;
    }

    public List<ScreenshotFeedback> getScreenshots() {
        return screenshots;
    }

    public void setScreenshots(List<ScreenshotFeedback> screenshots) {
        this.screenshots = screenshots;
    }
}
