package com.example.matthias.feedbacklibrary.feedbacks;

import java.io.Serializable;

/**
 * Screenshot feedback
 */
public class ScreenshotFeedback implements Serializable {
    private String title;

    private String imagePath;

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getImagePath ()
    {
        return imagePath;
    }

    public void setImagePath (String imagePath)
    {
        this.imagePath = imagePath;
    }
}
