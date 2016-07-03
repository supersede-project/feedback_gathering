package com.example.matthias.feedbacklibrary.feedbacks;

import com.example.matthias.feedbacklibrary.models.Mechanism;
import com.example.matthias.feedbacklibrary.models.RatingMechanism;
import com.example.matthias.feedbacklibrary.models.ScreenshotMechanism;
import com.example.matthias.feedbacklibrary.models.TextMechanism;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Feedback
 */
public class Feedback implements Serializable {
    private String text;
    private String application;
    private String title;
    private float configVersion;
    private String user;

    private List<RatingFeedback> ratings;
    // private List<AudioFeedback> audios;
    // private List<ScreenshotFeedback> screenshots;


    public Feedback(List<Mechanism> allMechanisms) {
        ratings = new ArrayList<>();
        //screenshots = new ArrayList<>();
        //audios = new ArrayList<>();
        for(Mechanism mechanism : allMechanisms) {
            if(mechanism.isActive()) {
                String type = mechanism.getType();

                if (type.equals("TEXT_TYPE")) {
                    this.title = ((TextMechanism) mechanism).getTitle();
                    this.text = ((TextMechanism) mechanism).getInputText();
                } else if (type.equals("RATING_TYPE")) {
                    RatingFeedback ratingFeedback = new RatingFeedback();
                    ratingFeedback.setTitle(((RatingMechanism) mechanism).getTitle());
                    ratingFeedback.setRating((int)(((RatingMechanism) mechanism).getInputRating()));
                    ratings.add(ratingFeedback);
                } else if (type.equals("AUDIO_TYPE")) {
                    // TODO
                } else if (type.equals("SCREENSHOT_TYPE")) {
                    // TODO
                } else {
                    // should never happen!
                }

            }
        }
    }

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

    /*public List<AudioFeedback> getAudios() {
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
    }*/
}
