package com.example.matthias.feedbacklibrary.feedbacks;

import java.io.Serializable;

/**
 * Rating feedback
 */
public class RatingFeedback implements Serializable {
    private String title;

    private int rating;

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public int getRating ()
    {
        return rating;
    }

    public void setRating (int rating)
    {
        this.rating = rating;
    }
}
