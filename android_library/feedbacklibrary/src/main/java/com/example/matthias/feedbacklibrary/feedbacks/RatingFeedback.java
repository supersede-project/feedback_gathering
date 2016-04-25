package com.example.matthias.feedbacklibrary.feedbacks;

import java.io.Serializable;

/**
 * Created by Matthias on 25.04.2016.
 */
public class RatingFeedback implements Serializable {
    private String title;

    private float rating;

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public float getRating ()
    {
        return rating;
    }

    public void setRating (float rating)
    {
        this.rating = rating;
    }
}
